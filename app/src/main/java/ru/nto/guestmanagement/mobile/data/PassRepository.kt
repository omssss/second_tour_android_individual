package ru.nto.guestmanagement.mobile.data


import ru.nto.guestmanagement.mobile.model.PassData

interface PassRepository {
    /** Ошибки сети и сервера не пробрасываются исключением, а приходят как [Result.failure] с текстом сообщения. */
    suspend fun load(): Result<PassData>
}
