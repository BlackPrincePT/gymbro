package com.pegio.gymbro.data.repository

import android.net.Uri
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.pegio.gymbro.data.remote.core.FirebaseConstants.USERS
import com.pegio.gymbro.data.remote.core.FirestoreUtils
import com.pegio.gymbro.data.remote.mapper.UserDtoMapper
import com.pegio.gymbro.data.remote.model.UserDto
import com.pegio.gymbro.data.workmanager.UploadWorker
import com.pegio.gymbro.data.workmanager.UploadWorker.Companion.FILE_TYPE_KEY
import com.pegio.gymbro.data.workmanager.UploadWorker.Companion.URI_KEY
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.core.map
import com.pegio.gymbro.domain.manager.upload.FileType
import com.pegio.gymbro.domain.model.User
import com.pegio.gymbro.domain.repository.UserRepository
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDtoMapper: UserDtoMapper,
    private val firestoreUtils: FirestoreUtils,
    private val workManager: WorkManager
) : UserRepository {

    private val db = Firebase.firestore

    override fun getCurrentUserId(): String? = Firebase.auth.currentUser?.uid

    override suspend fun fetchUserById(id: String): Resource<User, DataError.Firestore> {
        val documentRef = db.collection(USERS).document(id)

        val result = firestoreUtils.readDocument(documentRef, UserDto::class.java)

        return result.map(userDtoMapper::mapToDomain)
    }

    override fun saveUser(user: User) {
        val userDto = userDtoMapper.mapFromDomain(user)
        db.collection(USERS).document(user.id).set(userDto)
    }
}