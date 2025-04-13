package com.pegio.uploadmanager.di

import com.pegio.uploadmanager.core.FileUploadManager
import com.pegio.uploadmanager.core.UploadManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UploadManagerModule {

    @Binds
    abstract fun bindFileUploadManager(uploadManager: UploadManager): FileUploadManager
}