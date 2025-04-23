package com.pegio.domain.usecase.feed

import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.common.core.getOrElse
import com.pegio.common.core.getOrNull
import com.pegio.common.core.retryableCall
import com.pegio.firestore.repository.FollowRecordRepository
import com.pegio.firestore.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class FetchNextFollowersRecordPageUseCase @Inject constructor(
    private val followRecordRepository: FollowRecordRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String) = coroutineScope {
        retryableCall { followRecordRepository.fetchFollowers(userId) }
            .getOrElse { return@coroutineScope it.asFailure() }
            .map { followRecord ->
                async {
                    retryableCall { userRepository.fetchUserById(id = followRecord.userId) }
                        .getOrNull()
                }
            }
            .awaitAll()
            .filterNotNull()
            .asSuccess()

    }
}