package br.edu.ifsp.agendaroom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.agendaroom.databinding.ContatoCelulaBinding
import br.edu.ifsp.agendaroom.domain.Contato


class ContatoAdapter: RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder>(),
    Filterable {

    var onItemClick: ((Contato)->Unit) ?= null

    var contatosLista = ArrayList<Contato>()
    var contatosListaFilterable = ArrayList<Contato>()

    private lateinit var binding: ContatoCelulaBinding


  fun updateList(newList: List<Contato> ){
        contatosLista = newList as ArrayList<Contato>
        contatosListaFilterable = contatosLista
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContatoViewHolder {

        binding = ContatoCelulaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return  ContatoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        holder.nomeVH.text = contatosListaFilterable[position].nome
        holder.foneVH.text = contatosListaFilterable[position].fone
    }

    override fun getItemCount(): Int {
        return contatosListaFilterable.size
    }

    inner class ContatoViewHolder(view: ContatoCelulaBinding): RecyclerView.ViewHolder(view.root)
    {
        val nomeVH = view.nome
        val foneVH = view.fone

        init {
            view.root.setOnClickListener {
                onItemClick?.invoke(contatosListaFilterable[adapterPosition])
            }
        }

    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                if (p0.toString().isEmpty())
                    contatosListaFilterable = contatosLista
                else
                {
                    val resultList = ArrayList<Contato>()
                    for (row in contatosLista)
                        if (row.nome.lowercase().contains(p0.toString().lowercase()))
                            resultList.add(row)
                    contatosListaFilterable = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = contatosListaFilterable
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                contatosListaFilterable = p1?.values as ArrayList<Contato>
                notifyDataSetChanged()
            }

        }
    }

}