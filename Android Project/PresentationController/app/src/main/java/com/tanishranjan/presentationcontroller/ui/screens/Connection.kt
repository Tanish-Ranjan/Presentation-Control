package com.tanishranjan.presentationcontroller.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.tanishranjan.presentationcontroller.R
import com.tanishranjan.presentationcontroller.data.navigation.ControllerNav
import com.tanishranjan.presentationcontroller.data.network.Network
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionScreen(
    controller: NavHostController
) {

    val context = LocalContext.current
    var permGranted by remember {
        mutableStateOf(false)
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            permGranted = true
        }
    }
    LaunchedEffect(key1 = true) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) -> {
                permGranted = true
            }
            else -> {
                launcher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Presentation Controller", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("Scan or enter the ip to connect")

        Spacer(Modifier.height(16.dp))

        var qrMode by remember {
            mutableStateOf(false)
        }

        Surface(
            Modifier
                .fillMaxWidth()
                .weight(0.4f),
            RoundedCornerShape(8.dp),
            Color.LightGray
        ) {

            AnimatedVisibility(qrMode && permGranted, enter = fadeIn(), exit = fadeOut()) {
                CameraPreview(context, controller)
            }

        }

        Spacer(Modifier.height(8.dp))

        Row(
            Modifier.fillMaxWidth()
        ) {

            var ipTxt by remember {
                mutableStateOf("")
            }

            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                value = ipTxt,
                onValueChange = { ipTxt = it },
                placeholder = {
                    Text("IP Address")
                },
                shape = RoundedCornerShape(12.dp, 0.dp, 0.dp, 12.dp),
                singleLine = true
            )

            Spacer(Modifier.width(4.dp))

            val scope = rememberCoroutineScope()

            Button(
                {
                    scope.launch {
                        if (!Network.connect(ipTxt)) {
                            Toast.makeText(context, "Failed to Connect", Toast.LENGTH_SHORT).show()
                            return@launch
                        }
                        controller.navigate(ControllerNav.ControllerScreen.route) {
                            launchSingleTop = true
                            popUpTo(ControllerNav.ConnectionScreen.route) {
                                inclusive = true
                            }
                        }
                    }
                },
                Modifier
                    .height(52.dp)
                    .padding(0.dp),
                shape = RoundedCornerShape(0.dp, 12.dp, 12.dp, 0.dp)
            ) {
                Text("Connect")
            }

        }

        Spacer(Modifier.height(8.dp))

        FilledIconToggleButton(qrMode, onCheckedChange = { qrMode = it }, Modifier.size(56.dp)) {
            Icon(
                painterResource(R.drawable.ic_qr_code_scanner),
                "QR Scanner",
                Modifier.size(24.dp)
            )
        }

    }

}

@Composable
fun CameraPreview(
    context: Context,
    controller: NavHostController
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val cpFuture = remember { ProcessCameraProvider.getInstance(context) }
    val scope = rememberCoroutineScope()

    AndroidView({ ctx ->

        val previewView = PreviewView(ctx)
        val executor = ContextCompat.getMainExecutor(ctx)

        cpFuture.addListener({
            val cameraProvider = cpFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            // Incoming Image Feed Handling
            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(ctx)) {

                val bitmap = it.toBitmap()
                val frame = Frame.Builder().setBitmap(bitmap).build()
                val detector =
                    BarcodeDetector.Builder(ctx).setBarcodeFormats(Barcode.QR_CODE).build()
                val result = detector.detect(frame)

                if (result.size() > 0) {
                    val data = result.get(result.keyAt(0))

                    scope.launch {
                        if (!processQR(data)) {
                            Toast.makeText(context, "Failed to Connect", Toast.LENGTH_SHORT).show()
                            return@launch
                        }
                        controller.navigate(ControllerNav.ControllerScreen.route) {
                            launchSingleTop = true
                            popUpTo(ControllerNav.ConnectionScreen.route) {
                                inclusive = true
                            }
                        }
                    }

                }

            }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )

        }, executor)

        previewView

    })

}

fun ImageProxy.toBitmap(): Bitmap {

    val yBuffer = planes[0].buffer
    val vuBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val vuSize = vuBuffer.remaining()

    val nv21 = ByteArray(ySize + vuSize)

    yBuffer.get(nv21, 0, ySize)
    vuBuffer.get(nv21, ySize, vuSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
    val imageBytes = out.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

}

suspend fun processQR(data: Barcode): Boolean {

    val ip = data.displayValue
    return Network.connect(ip)

}