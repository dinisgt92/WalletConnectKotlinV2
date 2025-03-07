package com.walletconnect.dapp.ui.connect.chain_select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.walletconnect.dapp.R
import com.walletconnect.dapp.databinding.FragmentChainSelectionBinding
import com.walletconnect.dapp.ui.NavigationEvents
import com.walletconnect.dapp.ui.connect.ConnectViewModel
import com.walletconnect.walletconnectv2.client.WalletConnectClient
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ChainSelectionFragment : Fragment() {
    private val viewModel: ConnectViewModel by navGraphViewModels(R.id.connectGraph)
    private var _binding: FragmentChainSelectionBinding? = null
    private val binding: FragmentChainSelectionBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChainSelectionBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvChains.adapter = ChainSelectionAdapter(viewModel.listOfChainUI) { position, isChecked ->
            viewModel.updateSelectedChainUI(position, isChecked)
        }

        binding.btnConnect.setOnClickListener {
            if (viewModel.listOfChainUI.any { it.isSelected }) {
                if (WalletConnectClient.getListOfSettledPairings().isNotEmpty()) {
                    findNavController().navigate(R.id.action_fragment_chain_selection_to_dialog_pairing_selection)
                } else {
                    findNavController().navigate(R.id.action_fragment_chain_selection_to_dialog_pairing_generation)
                }

            } else {
                Toast.makeText(requireContext(), "Please select a chain", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.navigation
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
            .onEach { events ->
                when (events) {
                    is NavigationEvents.SessionApproved -> findNavController().navigate(R.id.action_global_fragment_session)
                    is NavigationEvents.SessionRejected -> Snackbar.make(binding.root, "Session was Rejected", Snackbar.LENGTH_LONG).show()
                    else -> Unit
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}