package com.task.mvvmkotlin.di

import com.task.mvvmkotlin.network.CountriesService
import com.task.mvvmkotlin.viewmodel.ListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: CountriesService)
    fun inject(viewModel: ListViewModel)
}