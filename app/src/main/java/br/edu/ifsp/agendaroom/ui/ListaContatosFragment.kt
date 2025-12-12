package br.edu.ifsp.agendaroom.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.edu.ifsp.agendaroom.R
import br.edu.ifsp.agendaroom.adapter.ContatoAdapter
import br.edu.ifsp.agendaroom.databinding.FragmentListaContatosBinding
import br.edu.ifsp.agendaroom.viewmodel.ContatoViewModel
import br.edu.ifsp.agendaroom.viewmodel.ListaState
import kotlinx.coroutines.launch

class ListaContatosFragment : Fragment() {

    private var _binding: FragmentListaContatosBinding? = null
    private val binding get() = _binding!!

    private lateinit var contatoAdapter: ContatoAdapter

    private val viewModel: ContatoViewModel by viewModels { ContatoViewModel.contatoViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaContatosBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_listaContatosFragment_to_cadastroFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa o adapter de forma segura
        initRecyclerView()

        // Coleta dados do ViewModel
        setupViewModel()

        // Inicia a coleta do Flow do Room
        viewModel.getAllContacts()

        setupMenu()
    }

    private fun initRecyclerView() {
        contatoAdapter = ContatoAdapter()
        binding.recyclerview.adapter = contatoAdapter

        contatoAdapter.onItemClick = { contato ->
            val bundle = Bundle()
            bundle.putInt("idContato", contato.id)
            findNavController().navigate(
                R.id.action_listaContatosFragment_to_detalheFragment,
                bundle
            )
        }

        // Inicialmente lista vazia
        contatoAdapter.updateList(emptyList())
    }

    private fun setupViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateList.collect { state ->
                when (state) {
                    is ListaState.SearchAllSuccess -> {
                        binding.textEmptyList.visibility = View.GONE
                        contatoAdapter.updateList(state.contatos)
                    }
                    ListaState.EmptyState -> {
                        binding.textEmptyList.visibility = View.VISIBLE
                        contatoAdapter.updateList(emptyList()) // Limpa RecyclerView
                    }
                    ListaState.ShowLoading -> { }
                }
            }
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)

                val searchView = menu.findItem(R.id.action_search).actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean = false

                    override fun onQueryTextChange(newText: String?): Boolean {
                        contatoAdapter.filter.filter(newText)
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}