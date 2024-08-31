package br.edu.ifsp.agendaroom.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.edu.ifsp.agendaroom.R
import br.edu.ifsp.agendaroom.databinding.FragmentDetalheBinding
import br.edu.ifsp.agendaroom.domain.Contato
import br.edu.ifsp.agendaroom.viewmodel.ContatoViewModel
import br.edu.ifsp.agendaroom.viewmodel.DetalheState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class DetalheFragment : Fragment() {
    private var _binding: FragmentDetalheBinding? = null
    private val binding get() = _binding!!

   lateinit var contato: Contato

    lateinit  var nomeEditText: EditText
    lateinit var foneEditText: EditText
    lateinit var emailEditText: EditText

    val viewModel : ContatoViewModel by viewModels { ContatoViewModel.contatoViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetalheBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nomeEditText = binding.commonLayout.editTextNome
        foneEditText = binding.commonLayout.editTextFone
        emailEditText = binding.commonLayout.editTextEmail

        val idContato = requireArguments().getInt("idContato")
        viewModel.getContactById(idContato)

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.stateDetail.collect {
                when (it) {
                    DetalheState.DeleteSuccess -> {
                        Snackbar.make(
                            binding.root,
                            "Contato removido com sucesso",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }

                    is DetalheState.GetByIdSuccess -> {
                        fillFields(it.c)
                    }

                    DetalheState.ShowLoading -> {}
                    DetalheState.UpdateSuccess -> {
                        Snackbar.make(
                            binding.root,
                            "Contato alterado com sucesso",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
                }
            }
        }


        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
              menuInflater.inflate(R.menu.detalhe_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_alterarContato -> {

                        contato.nome=nomeEditText.text.toString()
                        contato.fone=foneEditText.text.toString()
                        contato.email=emailEditText.text.toString()

                        viewModel.update(contato)

                        true
                    }
                    R.id.action_excluirContato ->{
                        viewModel.delete(contato)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun fillFields(c: Contato) {
        contato=c
        nomeEditText.setText(contato.nome)
        foneEditText.setText(contato.fone)
        emailEditText.setText(contato.email)

    }

}