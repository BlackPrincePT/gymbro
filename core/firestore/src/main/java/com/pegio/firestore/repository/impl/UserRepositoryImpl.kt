package com.pegio.firestore.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.convert
import com.pegio.common.core.map
import com.pegio.firestore.repository.UserRepository
import com.pegio.firestore.core.FirestoreConstants.USERS
import com.pegio.firestore.model.UserDto
import com.pegio.firestore.model.mapper.UserDtoMapper
import com.pegio.firestore.util.FirestoreUtils
import com.pegio.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val userDtoMapper: UserDtoMapper,
    private val firestoreUtils: FirestoreUtils
) : UserRepository {

    override suspend fun fetchUserById(id: String): Resource<User, DataError.Firestore> {
        val documentRef = db.collection(USERS).document(id)

        val result = firestoreUtils.readDocument(documentRef, UserDto::class.java)

        return result.map(userDtoMapper::mapToDomain)
    }

    override fun fetchUserSteamById(id: String): Flow<Resource<User, DataError.Firestore>> {
        val documentRef = db.collection(USERS).document(id)

        val result = firestoreUtils.observeDocument(documentRef, UserDto::class.java)

        return result.convert(userDtoMapper::mapToDomain)
    }

    override fun saveUser(user: User) {
        val userDto = userDtoMapper.mapFromDomain(user)
        db.collection(USERS).document(user.id).set(userDto)
    }
}