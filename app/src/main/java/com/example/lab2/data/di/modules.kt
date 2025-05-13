package com.example.lab2.data.di

import com.example.lab2.data.model.user_model.UserModel
import com.example.lab2.data.model.exchanger_model.ExchangerModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserModule {

    @Provides
    @Singleton
    fun providesUserModel(): UserModel {
        return UserModel()
    }
}


@Module
@InstallIn(SingletonComponent::class)
class ExchangerModel {

    @Provides
    @Singleton
    fun providesExchangerModel(): ExchangerModel {
        return ExchangerModel()
    }
}

