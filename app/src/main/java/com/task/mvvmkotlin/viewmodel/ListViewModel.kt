package com.task.mvvmkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.task.mvvmkotlin.di.DaggerApiComponent
import com.task.mvvmkotlin.model.Country
import com.task.mvvmkotlin.network.CountriesService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel : ViewModel() {

    @Inject
    lateinit var countriesService: CountriesService
    private val disposable = CompositeDisposable()

    val countries = MutableLiveData<List<Country>>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun refresh() {
        fetchCountries()
    }

    private fun fetchCountries() {
        loading.value = true

        disposable.add(
            countriesService.getCountries()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onFailure)
        )
    }

    private fun onSuccess(countriesList: List<Country>) {
        countries.postValue(countriesList)
        countryLoadError.postValue(false)
        loading.postValue(false)
    }

    private fun onFailure(throwable: Throwable) {
        countryLoadError.postValue(true)
        loading.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}