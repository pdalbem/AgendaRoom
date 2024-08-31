package br.edu.ifsp.agendaroom.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContatoDAO {
    @Insert
    suspend fun insert(contatoEntity: ContatoEntity)

    @Update
    suspend fun update (contatoEntity: ContatoEntity)

    @Delete
    suspend fun delete(contatoEntity: ContatoEntity)

    @Query("SELECT * FROM contatos ORDER BY nome")
    fun getAllContacts(): Flow<List<ContatoEntity>>

    @Query("SELECT * FROM contatos WHERE id=:id")
    fun getContactById(id: Int): Flow<ContatoEntity>


}