package org.cmdline.ackr

import com.sun.mail.util.MailSSLSocketFactory
import java.util.*
import javax.mail.*

class ImapConnection {
    private val socketFactory = MailSSLSocketFactory().apply {
//        isTrustAllHosts = true // TODO: Terrible. Remove.
    }

    private val properties = Properties().apply {
        this["mail.imap.ssl.enable"] = "true"
        this["mail.imap.ssl.socketFactory"] = socketFactory
    }

    fun fetchMail(host: String, user: String, password: String): List<Email> {
        val email = mutableListOf<Email>()

        val session: Session = Session.getInstance(properties, null)
        val store: Store = session.getStore("imap")
        store.connect(host, -1, user, password)

        val allFolders = store.defaultFolder.list("INBOX")
        allFolders.forEach {
            if (!it.isOpen) {
                it.open(Folder.READ_ONLY)
            }

            it.messages.forEach { m -> email.add(read_users_email(m)) }

            it.close()
        }

        store.close()

        return email
    }

    private fun read_users_email(m: Message): Email {
        val body = if (m.content is Multipart) {
            val messageContent = StringBuffer()
            val multipart = m.content as Multipart
            for (i in 0 until multipart.count) {
                messageContent.append(multipart.getBodyPart(i).content)
            }
            messageContent.toString()
        } else {
            m.content.toString()
        }
        return Email(
            m.messageNumber,
            m.from.firstOrNull()?.toString() ?: "",
            "is broken FIXME ",
            m.sentDate ?: Date(0),
            m.receivedDate ?: Date(0),
            m.subject,
            body,
            m.flags.contains(Flags.Flag.SEEN)
        )
    }
}
