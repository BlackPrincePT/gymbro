package com.pegio.gymbro.domain.core

class InvalidDocumentIdException(message: String = "The document identifier cannot be null or empty.") : Exception(message)