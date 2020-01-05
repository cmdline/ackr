package org.cmdline.ackr.db

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.cmdline.ackr.App
import org.cmdline.ackr.Email
import org.cmdline.ackr.ImapConnection

class EmailRepository(ctx: Context) {
    private val dao = App.db(ctx).emailDao()

    fun get(): LiveData<List<Email>> = dao.load()
    fun sync(host: String, user: String, password: String) = GlobalScope.launch {
        dao.save(ImapConnection().fetchMail(host, user, password))
    }
}
