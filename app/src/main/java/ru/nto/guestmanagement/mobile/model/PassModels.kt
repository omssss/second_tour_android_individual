package ru.nto.guestmanagement.mobile.model


import java.time.LocalDateTime

data class Visitor(val id: Long, val fullName: String, val organization: String?)

data class PassDetails(
    val id: Long,
    val qrCode: String,
    val validFrom: LocalDateTime,
    val validTo: LocalDateTime,
    val maxEntries: Int,
    val revokedAt: LocalDateTime?,
    val visitor: Visitor,
)

enum class ScanDirection { ENTRY, EXIT }

data class Scan(val id: Long, val scannedAt: LocalDateTime, val direction: ScanDirection)

/** [scans] содержит сканирования только данного пропуска: входы, выходы и сканирования из будущего. */
data class PassData(val pass: PassDetails, val scans: List<Scan>)

enum class PassStatus { ACTIVE, NOT_STARTED, EXPIRED, EXHAUSTED, BLOCKED }
