package data.helper

import data.module.Item

fun resolver(str: String): Item {
    val item = str.split(":")
    return Item(item[0], item[1])
}

fun Int.isNegative(): Boolean {
    return compareTo(0) < 0
}

fun Int.isPositive(): Boolean {
    return compareTo(0) > 0
}