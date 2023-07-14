package com.vp.favorites.di

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteActivityModule {
    @ContributesAndroidInjector(modules = [RoomModule::class])
    abstract fun bindFavoriteActivity(): FavoriteActivityModule
}