package org.cmdline.ackr.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.cmdline.ackr.Folder
import org.cmdline.ackr.Email


@Database(entities = [Email::class, Folder::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun emailDao(): EmailDao
    abstract fun folderDao(): FolderDao
}
