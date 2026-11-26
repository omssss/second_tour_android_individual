package ru.nto.guestmanagement.mobile.task.a2


import ru.nto.guestmanagement.mobile.model.PassDetails
import ru.nto.guestmanagement.mobile.model.PassStatus
import ru.nto.guestmanagement.mobile.model.Scan
import ru.nto.guestmanagement.mobile.model.ScanDirection
import java.time.LocalDateTime

/** A2. Чистая функция: статус пропуска на момент [now] по правилу из технического задания. */
fun calculatePassStatus(pass: PassDetails, scans: List<Scan>, now: LocalDateTime): PassStatus {
    return when {
        pass.revokedAt != null -> PassStatus.BLOCKED
        now < pass.validFrom -> PassStatus.NOT_STARTED
        now > pass.validTo -> PassStatus.EXPIRED
        scans.count { it.direction != ScanDirection.EXIT && it.scannedAt <= now } >= pass.maxEntries -> PassStatus.EXHAUSTED
        else -> PassStatus.ACTIVE
    }
}
