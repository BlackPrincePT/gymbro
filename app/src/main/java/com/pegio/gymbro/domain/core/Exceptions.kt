package com.pegio.gymbro.domain.core

class UserAuthenticationException(message: String = "User is not authenticated. Please log in to continue.") : Exception(message)
class InvalidDocumentIdException(message: String = "The document identifier cannot be null or empty.") : Exception(message)
class DataRetrievalException(message: String = "Unable to retrieve data from the server. Please try again later.") : Exception(message)

