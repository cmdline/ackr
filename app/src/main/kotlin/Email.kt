package org.cmdline.ackr

data class Email(
    // Actual Data
    val from:       String,
    val to:         String,
    val subject:    String,
    val body:       String,

    // Changeable Data
    var read:       Boolean = false,

    // Our state
    var open:           Boolean = false,
    var notifi_grade:   Int = 1
)
