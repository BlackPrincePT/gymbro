package com.pegio.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.convert
import com.pegio.common.core.map
import com.pegio.data.model.UserDto
import com.pegio.data.model.mapper.UserDtoMapper
import com.pegio.domain.model.User
import com.pegio.domain.repository.UserRepository
import com.pegio.firestore.core.FirestoreConstants.USERS
import com.pegio.firestore.util.FirestoreUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val userDtoMapper: UserDtoMapper,
    private val firestoreUtils: FirestoreUtils
) : UserRepository {

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