package com.pegio.auth.di

import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.firebase.auth.FirebaseAuth
import com.pegio.auth.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object FirebaseAuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideGetSignInWithGoogleOption(): GetSignInWithGoogleOption {
        return GetSignInWithGoogleOption
            .Builder(BuildConfig.DEFAULT_WEB_CLIENT_ID)
            .build()
    }

    @Provides
    @Singleton
    fun provideGetCredentialRequest(signInWithGoogleOption: GetSignInWithGoogleOption): GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()
    }
}