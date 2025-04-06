package com.pegio.gymbro.data.remote.model

import com.google.firebase.firestore.DocumentSnapshot

data class FirestorePagingResult<T>(
    val objects: List<T>,
    val lastDocument: DocumentSnapshot?
)