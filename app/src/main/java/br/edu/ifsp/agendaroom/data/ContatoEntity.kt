package br.edu.ifsp.agendaroom.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.edu.ifsp.agendaroom.domain.Contato


@Entity(tableName = "contatos")
data class ContatoEntity (
 @PrimaryKey(autoGenerate = true)
 val id: Int,
 val nome:String,
 val fone:String,
 val email:String
 )
{
 fun toDomain():Contato{
  return Contato(id, nome, fone,email)
 }
}

