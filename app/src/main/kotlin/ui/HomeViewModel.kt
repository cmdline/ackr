package org.cmdline.ackr.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.cmdline.ackr.Folder
import org.cmdline.ackr.Email
import org.cmdline.ackr.db.EmailRepository

class HomeViewModel : ViewModel() {
    lateinit var ctx: Context
    var search: String = ""


    private val emailRepository by lazy { EmailRepository(ctx) }

    suspend fun syncMail(host: String, user: String, password: String) {
//        isRefreshing = true
        emailRepository.sync_folder(host, user, password)
        emailRepository.sync_email(host, user, password)
//        isRefreshing = false
    }

    val mail: List<Email> by lazy {
        if (search == "") {
            emailRepository.get_all()
        } else {
            emailRepository.get_byFolder(search)
        }
    }

    fun get_search(search: String): List<Email>? {
        return emailRepository.get_byFolder(search)
    }

    val folder: LiveData<List<Folder>> by lazy { emailRepository.get_folder() }
}
