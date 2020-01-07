package org.cmdline.ackr.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.cmdline.ackr.Email

@Database(entities = [Email::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun emailDao(): EmailDao
}
