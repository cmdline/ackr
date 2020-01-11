package org.cmdline.ackr

import androidx.room.*
import java.util.Date
import java.util.*


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}


@Entity(tableName = "email")
@TypeConverters(Converters::class)
data class Email(
    // Actual Data
    @PrimaryKey
    val msgid: Int,

    @ForeignKey(entity = Folder::class, parentColumns = arrayOf("name"),
        childColumns = arrayOf("folder"),
        onDelete = ForeignKey.CASCADE)
    val folder: String,

    val from: String,
    val to: String,


    val sent_date: Date,
    val recv_date: Date,


    val subject: String,
    val body: String,

    // Changeable Data
    var read: Boolean = false,

    // Our state
    var open: Boolean = false,
    var notifi_grade: Int = 1
)
