package com.pegio.firestore.core

import com.google.firebase.firestore.DocumentSnapshot

internal data class FirestorePagingResult<T>(
    val objects: List<T>,
    val lastDocument: DocumentSnapshot?
)