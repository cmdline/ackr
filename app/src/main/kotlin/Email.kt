package org.cmdline.ackr

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "email")
data class Email(
    // Actual Data
    @PrimaryKey
    val message_no: Int,
    val from:       String,
    val to:         String,
    val sent_date:  String,
    val recv_date:  String,
    val subject:    String,
    val body:       String,

    // Changeable Data
    var read:       Boolean = false,

    // Our state
    var open:           Boolean = false,
    var notifi_grade:   Int = 1
)
