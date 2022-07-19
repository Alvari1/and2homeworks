package ru.netology.nmedia.enum

enum class Action(
    val key: String
) {
    LIKE("LIKE"),
    NEW_POST("NEW_POST");

    companion object {
        const val KEY = "action"
    }
}
