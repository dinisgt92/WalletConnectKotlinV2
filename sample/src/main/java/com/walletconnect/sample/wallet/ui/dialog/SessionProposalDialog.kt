package com.walletconnect.sample.wallet.ui.dialog

import android.content.Context
import android.net.Uri
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.walletconnect.sample.databinding.SessionProposalDialogBinding
import com.walletconnect.walletconnectv2.client.WalletConnectClientData

class SessionProposalDialog(
    context: Context,
    val approve: () -> Unit,
    val reject: () -> Unit,
    private val proposal: WalletConnectClientData.SessionProposal
) : BottomSheetDialog(context) {

    private val binding = SessionProposalDialogBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
        setContent()
    }

    private fun setContent() = with(binding) {
        Glide.with(context)
            .load(Uri.parse(proposal.icons.first().toString()))
            .into(icon)
        name.text = proposal.name
        uri.text = proposal.url
        description.text = proposal.description

        val chainsString = proposal.chains.joinToString(separator = "\n") { it }
        chains.text = chainsString

        val methodsString = proposal.methods.joinToString(separator = "\n") { it }
        methods.text = methodsString

        approve.setOnClickListener {
            dismiss()
            approve()
        }

        reject.setOnClickListener {
            dismiss()
            reject()
        }
    }
}