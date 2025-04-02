package com.pegio.gymbro.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.pegio.gymbro.data.remote.core.FirebaseConstants.USERS
import com.pegio.gymbro.data.remote.core.FirestoreUtils
import com.pegio.gymbro.data.remote.model.mapper.UserDtoMapper
import com.pegio.gymbro.data.remote.model.UserDto
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.core.convert
import com.pegio.gymbro.domain.core.map
import com.pegio.gymbro.domain.model.User
import com.pegio.gymbro.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDtoMapper: UserDtoMapper,
    private val firestoreUtils: FirestoreUtils
) : UserRepository {

    private val db = Firebase.firestore

    override suspend fun fetchUser(id: String): Resource<User, DataError.Firestore> {
        val documentRef = db.collection(USERS).document(id)

        val result = firestoreUtils.readDocument(documentRef, UserDto::class.java)

        return result.map(userDtoMapper::mapToDomain)
    }

    override fun fetchUserSteam(id: String): Flow<Resource<User, DataError.Firestore>> {
        val documentRef = db.collection(USERS).document(id)

        val result = firestoreUtils.observeDocument(documentRef, UserDto::class.java)

        return result.convert(userDtoMapper::mapToDomain)
    }

    override fun saveUser(user: User) {
        val userDto = userDtoMapper.mapFromDomain(user)
        db.collection(USERS).document(user.id).set(userDto)
    }

}