package org.cmdline.ackr

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "folders")
data class Folder(
    @PrimaryKey
    val name: String,

    var open: Boolean = false
)