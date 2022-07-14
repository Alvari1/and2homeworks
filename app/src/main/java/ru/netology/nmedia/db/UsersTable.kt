package ru.netology.nmedia.db

object UsersTable {
    const val USERS_NAME = "users"

    val USERS_DDL = """
        CREATE TABLE $USERS_NAME (
            ${UsersColumn.ID.columnName} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${UsersColumn.firstName.columnName} TEXT NOT NULL,
            ${UsersColumn.lastName.columnName} TEXT,
            ${UsersColumn.createdAt.columnName} INTEGER NOT NULL DEFAULT 0,
            ${UsersColumn.email.columnName} TEXT,
            ${UsersColumn.phone.columnName} TEXT
        )
    """.trimIndent()

    val ALL_USERS_COLUMNS_NAMES = UsersColumn.values().map {
        it.columnName
    }.toTypedArray()

    enum class UsersColumn(val columnName: String) {
        ID("id"),
        firstName("firstName"),
        lastName("lastName"),
        createdAt("createdAt"),
        email("email"),
        phone("phone"),
    }
}