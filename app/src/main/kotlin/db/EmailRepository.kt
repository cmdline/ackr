package org.cmdline.ackr.db

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.cmdline.ackr.*

class EmailRepository(ctx: Context) {
    private val email_dao = App.db(ctx).emailDao()
    private val folder_dao = App.db(ctx).folderDao()

    fun get(): LiveData<List<Email>> = email_dao.load()

    fun get_folder(): LiveData<List<Folder>> = folder_dao.load()

    suspend fun sync_email(host: String, user: String, password: String) =
        email_dao.save(ImapConnection().fetchMail(host, user, password))

    suspend fun sync_folder(host: String, user: String, password: String) =
        folder_dao.save(ImapConnection().fetchFolder(host, user, password))
}
