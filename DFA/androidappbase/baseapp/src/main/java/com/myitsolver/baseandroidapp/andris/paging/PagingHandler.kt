//package com.myitsolver.baseandroidapp.andris.paging
//
//import androidx.lifecycle.MutableLiveData
//import retrofit2.Call
//
//
////TODO általánosítani kéne...
//abstract class LiveDataPagingHandler<T>(val data: MutableLiveData<MutableList<T>>) : PagingHandler<T>() {
//    override fun dispatchInitalList(resultList: MutableList<T>?) {
//        data.value = resultList
//    }
//
//    override fun dispatchMoreList(resultList: MutableList<T>?) {
//        val newList =  mutableListOf<T>()
//        data.value?.let {
//            newList.addAll(it)
//        }
//        resultList?.let {
//            newList.addAll(it)
//        }
//        data.value = newList
//    }
//
//}
//
//abstract class PagingHandler<T> {
//    var nextPage: String? = null
//
//    val hasNextPage: Boolean
//        get() {
//            return nextPage != null
//        }
//
//    fun loadInitialData() {
//        getInitalDataCall().send {
//            nextPage = it.nextPage
//            dispatchInitalList(it.resultList)
//        }
//    }
//
//    fun loadNextPage() {
//        if (hasNextPage) {
//            getMoreDataCall(nextPage ?: return).send(showLoadingIndicator = true) {
//                nextPage = it.nextPage
//                dispatchMoreList(it.resultList)
//            }
//        }
//    }
//
//
//    abstract fun dispatchInitalList(resultList: MutableList<T>?)
//    abstract fun dispatchMoreList(resultList: MutableList<T>?)
//
//    abstract fun getInitalDataCall(): Call<ResultListWrapper<T>>
//    abstract fun getMoreDataCall(nextPage: String): Call<ResultListWrapper<T>>
//
//}
//
//class data class ResultListWrapper<T>(
//    val resultList: MutableList<T>? = null,
//    val nextPage: String?
//)