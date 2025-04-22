package com.pegio.firestore.repository

interface FcmTokenRepository {
    fun saveToken(token: String, ownerId: String)
}