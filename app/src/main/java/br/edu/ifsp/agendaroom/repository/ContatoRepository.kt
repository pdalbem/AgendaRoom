package br.edu.ifsp.agendaroom.repository


import br.edu.ifsp.agendaroom.data.ContatoDAO
import br.edu.ifsp.agendaroom.domain.Contato
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map


class ContatoRepository (private val contatoDAO: ContatoDAO) {

    suspend fun insert(contato: Contato){
        contatoDAO.insert(contato.toEntity())
    }

    suspend fun update(contato: Contato){
        contatoDAO.update(contato.toEntity())
    }

    suspend fun delete(contato: Contato){
        contatoDAO.delete(contato.toEntity())
    }

     fun getAllContacts(): Flow<List<Contato>> {
          return contatoDAO.getAllContacts().map { list ->
            list.map {
                it.toDomain()
            }
        }

    }


    fun getContactById(id: Int): Flow<Contato?>{
        return contatoDAO.getContactById(id).map {it?.toDomain()}
    }


}