package org.cmdline.ackr.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.cmdline.ackr.Email
import org.cmdline.ackr.db.EmailRepository

class HomeViewModel : ViewModel() {
    lateinit var ctx: Context
    var isRefreshing = false

    private val emailRepository by lazy { EmailRepository(ctx) }

    suspend fun syncMail(host: String, user: String, password: String) {
        isRefreshing = true
        emailRepository.sync(host, user, password)
        isRefreshing = false
    }

    val mail: LiveData<List<Email>> by lazy { emailRepository.get() }
}
