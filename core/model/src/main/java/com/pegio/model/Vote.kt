package com.pegio.model

data class Vote(
    val voterId: String,
    val vote: Type,
    val timestamp: Long
) {

    enum class Type(val value: Int) {
        UP_VOTE(value = +1),
        DOWN_VOTE(value = -1);

        companion object {

            fun valueOf(value: Int): Type {
                return when (value) {
                    +1 -> UP_VOTE
                    -1 -> DOWN_VOTE
                    else -> throw IllegalArgumentException()
                }
            }
        }
    }
}