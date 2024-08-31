package br.edu.ifsp.agendaroom.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.edu.ifsp.agendaroom.R
import br.edu.ifsp.agendaroom.databinding.FragmentCadastroBinding
import br.edu.ifsp.agendaroom.domain.Contato
import br.edu.ifsp.agendaroom.viewmodel.ContatoViewModel
import br.edu.ifsp.agendaroom.viewmodel.CadastroState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class CadastroFragment : Fragment() {
    private var _binding: FragmentCadastroBinding? = null
    private val binding get() = _binding!!

    val viewModel : ContatoViewModel by viewModels { ContatoViewModel.contatoViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateCadastro.collect {
                when (it) {
                    CadastroState.InsertSuccess -> {
                        Snackbar.make(
                            binding.root,
                            "Contato inserido com sucesso",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }

                    CadastroState.ShowLoading -> {}
                }
            }
        }


        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.cadastro_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_salvarContato -> {
                        val nome = binding.commonLayout.editTextNome.text.toString()
                        val fone = binding.commonLayout.editTextFone.text.toString()
                        val email = binding.commonLayout.editTextEmail.text.toString()

                        val contato = Contato(nome=nome, fone=fone, email=email)
                        viewModel.insert(contato)

                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }
}