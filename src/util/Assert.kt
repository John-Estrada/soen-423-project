package util

fun assert(value: Boolean, message: String) {
    if (!value) throw Exception(message);
}