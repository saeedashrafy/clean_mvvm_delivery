package com.example.market.domain.entity

sealed class Failure : Throwable()


object NetworkConnection : Failure()
object NetworkError : Failure()
object EmptyList : Failure()

sealed class ServerError : Failure() {


    sealed class AuthError : ServerError() {

        object InvalidCustomToken : AuthError()
        object CustomTokenMismatch : AuthError()
        object InvalidCredential : AuthError()
        object InvalidEmail : AuthError()
        object WrongPassword : AuthError()
        object UserMismatch : AuthError()
        object RequiresRecentLogin : AuthError()
        object AccountExistsWithDifferenceCredential : AuthError()
        object EmailAlreadyInUse : AuthError()
        object CredentialAlreadyInUse : AuthError()
        object UserDisabled : AuthError()
        object TokenExpired : AuthError()
        object UserNotFound : AuthError()
        object InvalidUserToken : AuthError()
        object OperationNotAllowed : AuthError()
        object WeakPassword : AuthError()

        object UploadFile : AuthError()

        object Unauthenticated : AuthError()
    }
}
/*
data class UnexpectedError(
        override val message: String,
        override val cause: Throwable?,
) : Failure()*/
