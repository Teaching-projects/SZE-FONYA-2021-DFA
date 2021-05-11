package com.myitsolver.inappbilling

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.SkuDetails
import com.anjlab.android.iab.v3.TransactionDetails



abstract class BillingViewModel : ViewModel(), BillingProcessor.IBillingHandler {
    lateinit var billingProcessor: BillingProcessor
    /**
     * call get*ListingDetails first
     */
    val availableProducts = MutableLiveData<MutableList<SkuDetails>>()
    /**
     * call get*ListingDetails first
     */
    val availableSubscriptions = MutableLiveData<MutableList<SkuDetails>>()
    /**
     * products and subscriptions
     * call get*ListingDetails first
     */
    val availableItems = MediatorLiveData<MutableList<SkuDetails>>()

    init {
        availableItems.addSource(availableProducts) {
            val list = mutableListOf<SkuDetails>()
            list.addAll(it)
            availableSubscriptions.value?.let {
                list.addAll(it)
            }
            availableItems.value = list
        }
        availableItems.addSource(availableSubscriptions) {
            val list = mutableListOf<SkuDetails>()
            list.addAll(it)
            availableProducts.value?.let {
                list.addAll(it)
            }
            availableItems.value = list
        }
    }

    private val autoConsume = true

    var productPurchasedListener: ((sku: String, details: TransactionDetails?) -> Unit)? = null
    var productAppliedListener: ((sku: String) -> Unit)? = null
    var billingErrorListener: ((errorCode: Int) -> Unit)? = null

    fun initializeBilling(activity: Activity, licenseKey: String) {
        if (::billingProcessor.isInitialized.not()) {
            billingProcessor = BillingProcessor.newBillingProcessor(activity, licenseKey, this)
        }
        if (billingProcessor.isInitialized.not()) {
            billingProcessor.initialize()
        }
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, d: Intent?): Boolean {
        return billingProcessor.handleActivityResult(requestCode, resultCode, d)
    }

    fun autoPurchase(activity: Activity, item: SkuDetails) {
        if (item.isSubscription) {
            subscribeTo(activity, item.productId)
        } else {
            purchaseItem(activity, item.productId)
        }
    }

    fun purchaseItem(activity: Activity, sku: String) {
        billingProcessor.purchase(activity, sku)
    }

    fun subscribeTo(activity: Activity, sku: String) {
        billingProcessor.subscribe(activity, sku)
    }

    fun getAllListingDetails(listOfProductIds: ArrayList<String>? = null, listOfSubscriptionIds: ArrayList<String>? = null) {
        listOfProductIds?.let {
            getProductListingDetails(it)
        }
        listOfSubscriptionIds?.let {
            getSubscriptionListingDetails(it)
        }
    }

    fun getProductListingDetails(listOfProductIds: ArrayList<String>) {
        availableProducts.value = billingProcessor.getPurchaseListingDetails(listOfProductIds)
    }

    fun getSubscriptionListingDetails(listOfSubscriptionIds: ArrayList<String>) {
        billingProcessor.getSubscriptionListingDetails(listOfSubscriptionIds)
    }

    override fun onBillingInitialized() {

    }

    override fun onPurchaseHistoryRestored() {
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        if (autoConsume) {
            if (details?.purchaseInfo?.purchaseData?.autoRenewing != true) {
                billingProcessor.consumePurchase(productId)
            }
        }
        productPurchasedListener?.invoke(productId, details)

    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        billingErrorListener?.invoke(errorCode)
    }

    abstract fun applyPurchase(productId: String, details: TransactionDetails?)
//    fun applyPurchase(productId: String, details: TransactionDetails?) {
//        details?.let {
//            remote {
//               applyPurchase(PurchaseWrapper(it.purchaseInfo.purchaseData.purchaseToken, productId)).send(showLoadingIndicator = true) {
//                   productAppliedListener?.invoke(productId)
//               }
//            }
//        }
//    }
}
