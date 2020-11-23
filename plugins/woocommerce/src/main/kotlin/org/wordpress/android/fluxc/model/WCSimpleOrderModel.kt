package org.wordpress.android.fluxc.model

import com.google.gson.Gson
import com.yarolegovich.wellsql.core.Identifiable
import com.yarolegovich.wellsql.core.annotation.Column
import com.yarolegovich.wellsql.core.annotation.PrimaryKey
import com.yarolegovich.wellsql.core.annotation.Table
import org.wordpress.android.fluxc.model.order.OrderIdentifier
import org.wordpress.android.fluxc.persistence.WellSqlConfig

@Table(addOn = WellSqlConfig.ADDON_WOOCOMMERCE)
data class WCSimpleOrderModel(@PrimaryKey @Column private var id: Int = 0) : Identifiable {
    @Column var localSiteId = 0
    @Column var localOrderId = 0
    @Column var remoteOrderId = 0L // The unique identifier for this order on the server
    @Column var number = "" // The order number to display to the user
    @Column var status = ""
    @Column var currency = ""
    @Column var dateCreated = "" // ISO 8601-formatted date in UTC, e.g. 1955-11-05T14:15:00Z
    @Column var dateModified = "" // ISO 8601-formatted date in UTC, e.g. 1955-11-05T14:15:00Z
    @Column var total = "" // Complete total, including taxes

    @Column var billingFirstName = ""
    @Column var billingLastName = ""

    companion object {
        private val gson by lazy { Gson() }
    }

    override fun getId() = id

    override fun setId(id: Int) {
        this.id = id
    }

    /**
     * Returns an [OrderIdentifier], representing a unique identifier for this [WCSimpleOrderModel].
     */
    fun getIdentifier() = OrderIdentifier(this)
}
