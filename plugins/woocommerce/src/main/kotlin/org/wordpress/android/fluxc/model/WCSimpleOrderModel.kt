package org.wordpress.android.fluxc.model

class WCSimpleOrderModel(
    val localSiteId: Int,
    val localOrderId: Int,
    val remoteOrderId: Long,
    val number: Int,
    val status: String,
    val currency: String,
    val dateCreated: String,
    val dateModified: String,
    val total: String,
    val billingFirstName: String,
    val billingLastName: String
)
