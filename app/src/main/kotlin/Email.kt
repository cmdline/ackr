package org.cmdline.ackr

import androidx.lifecycle.MutableLiveData

data class Folder(
    val name:   String,
    val emails: MutableLiveData<List<Email>>,

    // Our state, in the middle our app
    var open:   Boolean = false
)

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
