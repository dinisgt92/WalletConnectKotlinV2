package com.walletconnect.walletconnectv2.core.model.vo.clientsync.common

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RelayProtocolOptionsVO(
    val protocol: String = "waku",
    val data: String? = null
)