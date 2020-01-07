package org.cmdline.ackr

import android.content.Context
import androidx.room.Room
import org.cmdline.ackr.db.Database

object App {
    fun db(ctx: Context): Database {
        if (instance == null) {
            instance = Room.databaseBuilder(ctx, Database::class.java, "db")
                .build()
        }

        return instance!!
    }

    private var instance: Database? = null
}
