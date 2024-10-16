package br.edu.ifsp.agendaroom.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import br.edu.ifsp.agendaroom.ContatoApplication
import br.edu.ifsp.agendaroom.domain.Contato
import br.edu.ifsp.agendaroom.repository.ContatoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class CadastroState {
    data object InsertSuccess : CadastroState()
    data object ShowLoading : CadastroState()
}

sealed class DetalheState {
    data object UpdateSuccess : DetalheState()
    data object DeleteSuccess : DetalheState()
    data class GetByIdSuccess(val c: Contato) : DetalheState()
    data object ShowLoading : DetalheState()

}

sealed class ListaState {
    data class SearchAllSuccess(val contatos: List<Contato>) : ListaState()
    data object ShowLoading : ListaState()
    data object EmptyState : ListaState()
}

 class ContatoViewModel(private val repository: ContatoRepository) : ViewModel(){

     private val _stateCadastro = MutableStateFlow<CadastroState>(CadastroState.ShowLoading)
     val stateCadastro = _stateCadastro.asStateFlow()

     private val _stateDetail = MutableStateFlow<DetalheState>(DetalheState.ShowLoading)
     val stateDetail = _stateDetail.asStateFlow()

     private val _stateList = MutableStateFlow<ListaState>(ListaState.ShowLoading)
     val stateList = _stateList.asStateFlow()


    fun insert(contatoEntity: Contato) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(contatoEntity)
        _stateCadastro.value=CadastroState.InsertSuccess
    }

    fun update(contatoEntity: Contato) = viewModelScope.launch(Dispatchers.IO){
        repository.update(contatoEntity)
        _stateDetail.value=DetalheState.UpdateSuccess
    }

    fun delete(contatoEntity: Contato) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(contatoEntity)
        _stateDetail.value=DetalheState.DeleteSuccess
    }

     fun getAllContacts(){
         viewModelScope.launch {
             repository.getAllContacts().collect{result->
                 if (result.isEmpty())
                     _stateList.value=ListaState.EmptyState
                    else
                     _stateList.value=ListaState.SearchAllSuccess(result)

             }
         }
     }


     fun getContactById(id: Int) {
         viewModelScope.launch {
             repository.getContactById(id).collect{result->
                 _stateDetail.value=  DetalheState.GetByIdSuccess(result)
             }

         }

     }


     companion object {
         fun contatoViewModelFactory() : ViewModelProvider.Factory =
             object : ViewModelProvider.Factory {
                 override fun <T : ViewModel> create(
                     modelClass: Class<T>,
                     extras: CreationExtras
                 ): T {
                     val application = checkNotNull(
                         extras[APPLICATION_KEY]
                     )
                     return ContatoViewModel(
                         (application as ContatoApplication).repository
                     ) as T
                 }
             }
     }


}


