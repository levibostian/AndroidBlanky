package com.levibostian.androidblanky.service.error.network.type

sealed class ConflictResponseErrorType(val type: String) {

    class AlreadyRedeemed: ConflictResponseErrorType("already-redeemed")

    override fun equals(other: Any?): Boolean {
        return other is ConflictResponseErrorType && other.type == this.type
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }

    companion object {
        fun fromString(from: String): ConflictResponseErrorType {
            return when (from) {
                AlreadyRedeemed().type -> AlreadyRedeemed()
                else -> throw RuntimeException("You did not catch a case")
            }
        }
    }
}