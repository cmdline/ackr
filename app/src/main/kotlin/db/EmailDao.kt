package org.cmdline.ackr.db

import androidx.lifecycle.LiveData
import androidx.room.*
import org.cmdline.ackr.Folder
import org.cmdline.ackr.Email

@Dao
interface EmailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(emails: List<Email>)

    @Update
    fun update(email: Email)

    @Delete
    fun delete(emails: List<Email>)

    @Query("SELECT * FROM email WHERE folder = :name ORDER BY recv_date DESC")
    fun load(name: String): LiveData<List<Email>>

    @Query("SELECT * FROM email ORDER BY recv_date DESC")
    fun load_all(): LiveData<List<Email>>
}


@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(emails: List<Folder>)

    @Update
    fun update(email: Folder)

    @Delete
    fun delete(emails: List<Folder>)

    @Query("SELECT * FROM folders ORDER BY name DESC")
    fun load(): LiveData<List<Folder>>
}
