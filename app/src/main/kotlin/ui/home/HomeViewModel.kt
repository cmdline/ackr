package org.cmdline.ackr.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.cmdline.ackr.Email
import org.cmdline.ackr.ImapConnection

class HomeViewModel : ViewModel() {
    private val imap = ImapConnection()

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    private val _mail = MutableLiveData<List<Email>>().apply {
        value = listOf(
            Email("fake@email.ackr", "Save on your student loans NOW!"),
            Email("DevBot", "Top 10 ways C is better, number 6 will STUN you!"),
            Email("e0f", "Free newsletters about top 10 lists, subscribe today!")
        )
    }

    fun fetchMail(host: String, user: String, password: String) = GlobalScope.launch {
        _mail.postValue(imap.fetchMail(host, user, password))
    }

    val text: LiveData<String> = _text
    val mail: LiveData<List<Email>> = _mail
}
