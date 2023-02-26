package com.ahmetcanerol.acecrypto.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmetcanerol.acecrypto.api.ApiService
import com.ahmetcanerol.acecrypto.model.CoinDailyModel
import com.ahmetcanerol.acecrypto.model.CoinListModel
import com.ahmetcanerol.acecrypto.repository.CoinListRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

import retrofit2.Call
import retrofit2.Response


class CoinListViewModel(): ViewModel() {
    val coinListLiveData = MutableLiveData<List<CoinListModel>>()
    var dailyList = MutableLiveData<CoinDailyModel?>()
    var top_5=MutableLiveData<List<CoinListModel>>()
    val loading=MutableLiveData<Boolean>()
    val repository= CoinListRepository()
    private val api=repository.getData()
    private  val api2=repository.getTop_5()
    private val disposable=CompositeDisposable()
    private var compositeDisposable: CompositeDisposable? = null
    //verileri netten al
     fun getAllCoin(){
      loading.value=true
        disposable.add(
            api.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<CoinListModel>>(),
                    Disposable {
                    override fun onSuccess(t: List<CoinListModel>) {
                        //basarılı olursa
                        coinListLiveData.postValue(t)
                        loading.value=false
                    }
                    override fun onError(e: Throwable) {
                        //basarısız olursa
                        e.printStackTrace()
                        loading.value=false
                    }
                })
        )
    }
    fun getTop_5(){
        disposable.add(
            api2.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<List<CoinListModel>>(),
                  Disposable{
                    override fun onSuccess(t: List<CoinListModel>) {
                        top_5.postValue(t)
                        loading.value=false
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loading.value=false
                    }

                })
        )
    }
    fun getDay(id:String,vs_currency:String,days:String){
        ApiService.ApiClient.getApiService().getDaily(id,vs_currency,days)
            .enqueue(object : retrofit2.Callback<CoinDailyModel> {
                override fun onResponse(call: Call<CoinDailyModel>, response: Response<CoinDailyModel>) {
                    dailyList.postValue(response.body())
                }
                override fun onFailure(call: Call<CoinDailyModel>, t: Throwable) {
                    dailyList.postValue(null)
                }
            })
    }
}