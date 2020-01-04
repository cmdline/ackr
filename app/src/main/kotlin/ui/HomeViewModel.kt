package org.cmdline.ackr.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.cmdline.ackr.Email
import org.cmdline.ackr.ImapConnection

class HomeViewModel : ViewModel() {
    private val imap = ImapConnection()

    private val _mail = MutableLiveData<List<Email>>().apply {
        value = listOf(
            Email("fake@email.ackr", "real@email.ackr", "Save on your student loans NOW!", "lol"),
            Email(
                "DevBot",
                "themoose@theroad.ohno",
                "Top 10 ways C is better, number 6 will STUN you!",
                "1. Weeds out the weak by containing pointers\n" +
                        "2\n3\n4\n" +
                        "I needed a long list, okay?"
            ),
            Email(
                "e0f",
                "endoffile",
                "Free newsletters about top 10 lists, subscribe today!",
                "1. How to stop worrying and love C\n" +
                        "2. Dealing with crippling social ineptitude\n"
            )
        )
    }

    private val _connstate = MutableLiveData<Int>().apply {
        value = -1
    }

    fun testServer(host: String, user: String, password: String) = GlobalScope.launch {
        if (imap.testServer(host, user, password)) {
            _connstate.postValue(1)
        } else {
            _connstate.postValue(0)
        }
    }

    fun fetchMail(host: String, user: String, password: String) = GlobalScope.launch {
        _mail.postValue(imap.fetchMail(host, user, password))
    }

    val mail: LiveData<List<Email>> = _mail
    val connstate: LiveData<Int> = _connstate

}
