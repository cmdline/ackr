package org.cmdline.ackr.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.cmdline.ackr.Email
import org.cmdline.ackr.Folder
import org.cmdline.ackr.ImapConnection

class HomeViewModel : ViewModel() {
    private val imap = ImapConnection()

    private val _folder = MutableLiveData<List<Folder>>().apply {
        value = listOf(
            Folder(
                "INBOX",
                MutableLiveData<List<Email>>().apply {
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

    fun fetchFolders(host: String, user: String, password: String) = GlobalScope.launch {
        _folder.postValue(imap.fetchFolders(host, user, password))
    }

    fun fetchMail(folder: String) = GlobalScope.launch {
        _folder.value?.forEach {
            if (it.name == folder) {
                it.emails.postValue(imap.fetchMail(folder))
            }

        }
    }

    val folders:    LiveData<List<Folder>> = _folder
    val connstate:  LiveData<Int> = _connstate

}
