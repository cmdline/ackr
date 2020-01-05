package org.cmdline.ackr

import androidx.lifecycle.MutableLiveData
import com.sun.mail.util.MailSSLSocketFactory
import java.util.*
import javax.mail.Folder as JFolder
import javax.mail.AuthenticationFailedException
import javax.mail.Flags
import javax.mail.Store
import javax.mail.Session
import javax.mail.Message



class ImapConnection {
    private val socketFactory = MailSSLSocketFactory().apply {
//        isTrustAllHosts = true // TODO: Terrible. Remove.
    }

    private val properties = Properties().apply {
        this["mail.imap.ssl.enable"] = "true"
        this["mail.imap.ssl.socketFactory"] = socketFactory
    }

    var session: Session = Session.getInstance(properties, null)
    var store: Store = session.getStore("imap")


    fun testServer(host: String, user: String, password: String): Boolean {
        if (store.isConnected) {
            return store.isConnected
        }

        try {
            store.connect(host, -1, user, password)
        } catch (e: AuthenticationFailedException) {
            return false
        }

        return store.isConnected
    }

    fun fetchFolders(host: String, user: String, password: String): List<Folder> {
        val folders = mutableListOf<Folder>()

        if (!store.isConnected) {
            store.connect(host, -1, user, password)
        }

        val allFolders = store.defaultFolder.list("*")
        allFolders.forEach {
            folders.add(Folder(it.fullName, MutableLiveData<List<Email>>()))
        }

        store.close()

        return folders
    }

    fun fetchMail(folder: String): List<Email> {
        val email = mutableListOf<Email>()

        if (!store.isConnected) {
            return listOf()
        }

        val allFolders = store.defaultFolder.list(folder)
        allFolders.forEach {
            if (!it.isOpen) {
                it.open(JFolder.READ_ONLY)
            }

            it.messages.forEach { m -> email.add(read_users_email(m)) }
            it.close()
        }

        store.close()

        return email
    }

    private fun read_users_email(m: Message): Email {
        return Email(
            m.from.firstOrNull()?.toString() ?: "",
            "is broken FIXME ",
            m.subject,
            m.content.toString() ?: "No Content!",
            m.flags.contains(Flags.Flag.SEEN)
        )
    }
}
