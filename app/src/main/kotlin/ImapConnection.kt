package org.cmdline.ackr

import com.sun.mail.util.MailSSLSocketFactory
import java.util.*
import javax.mail.Flags
import javax.mail.Folder
import javax.mail.Session
import javax.mail.Store


class ImapConnection {
    private val socketFactory = MailSSLSocketFactory().apply {
        isTrustAllHosts = true // TODO: Terrible. Remove.
    }

    private val properties = Properties().apply {
        this["mail.imap.ssl.enable"] = "true"
        this["mail.imap.ssl.socketFactory"] = socketFactory
    }

    fun fetchMail(
        host: String,
        user: String,
        password: String
    ): List<Email> {
        val email = mutableListOf<Email>()

        val session: Session = Session.getInstance(properties, null)
        val store: Store = session.getStore("imap")
        store.connect(host, -1, user, password)

        val allFolders = store.defaultFolder.list("*")
        allFolders.forEach {
            if (!it.isOpen) {
                it.open(Folder.READ_ONLY)
            }

            it.messages.forEach { msg ->
                email.add(Email(
                    msg.from.firstOrNull()?.toString() ?: "",
                    "is broken FIXME ",
                    msg.subject,
                    msg.content.toString() ?: "No Content!",
                    msg.flags.contains(Flags.Flag.SEEN)))
            }

            it.close()
        }

        store.close()

        return email
    }
}
