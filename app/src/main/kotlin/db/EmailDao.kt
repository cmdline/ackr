package org.cmdline.ackr.db

import androidx.lifecycle.LiveData
import androidx.room.*
import org.cmdline.ackr.Email

@Dao
interface EmailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(emails: List<Email>)

    @Update
    fun update(email: Email)

    @Delete
    fun delete(emails: List<Email>)

    @Query("SELECT * FROM email")
    fun load(): LiveData<List<Email>>
}
