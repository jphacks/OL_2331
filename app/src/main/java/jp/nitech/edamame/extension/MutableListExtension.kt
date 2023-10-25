package jp.nitech.edamame.extension

inline fun <reified T> MutableList<T>.clone(): MutableList<T> {
    return mutableListOf(*this.toTypedArray())
}