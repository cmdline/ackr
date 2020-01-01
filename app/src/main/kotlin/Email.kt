package org.cmdline.ackr

data class Email(
    val from:       String,
    val to:         String,
    val subject:    String,
    val body:       String,

    var open: Boolean = false
)
