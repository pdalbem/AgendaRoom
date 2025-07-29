package br.edu.ifsp.agendaroom

import android.app.Application
import br.edu.ifsp.agendaroom.data.ContatoDatabase
import br.edu.ifsp.agendaroom.repository.ContatoRepository

class ContatoApplication:Application() {
    private val database by lazy { ContatoDatabase.getDatabase(this) }
    val repository by lazy { ContatoRepository(database.contatoDAO()) }
}