package com.kylix.table

import org.jetbrains.exposed.sql.Table

object UserTable: Table() {
    val uid = varchar("uid", 1024)
    val username = varchar("username", 64)
    val password = varchar("password", 1024)
    val salt = varchar("salt", 1024)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(uid)
}