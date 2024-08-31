package br.edu.ifsp.agendaroom.domain

import br.edu.ifsp.agendaroom.data.ContatoEntity

data class Contato (
    var id:Int=0,
    var nome:String,
    var fone:String,
    var email:String

){
    fun toEntity():ContatoEntity{
        return ContatoEntity(id,nome,fone,email)
    }
}
