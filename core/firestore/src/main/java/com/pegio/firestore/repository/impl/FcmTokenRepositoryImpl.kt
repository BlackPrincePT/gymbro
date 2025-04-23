package com.pegio.firestore.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pegio.firestore.core.FirestoreConstants.FCM_TOKENS
import com.pegio.firestore.core.FirestoreConstants.USERS
import com.pegio.firestore.model.FcmTokenDto
import com.pegio.firestore.repository.FcmTokenRepository
import javax.inject.Inject

internal class FcmTokenRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : FcmTokenRepository {

    override fun saveToken(token: String, ownerId: String) {
        val tokenDto = FcmTokenDto()
        db.collection(USERS).document(ownerId).collection(FCM_TOKENS).document(token).set(tokenDto)
    }
}