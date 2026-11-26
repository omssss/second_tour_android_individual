package ru.nto.guestmanagement.mobile.task.a1


//import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import ru.nto.guestmanagement.mobile.R
import ru.nto.guestmanagement.mobile.model.PassDetails
import ru.nto.guestmanagement.mobile.ui.TestIds.PASS_ENTRIES_LIMIT
import ru.nto.guestmanagement.mobile.ui.TestIds.PASS_HOLDER
import ru.nto.guestmanagement.mobile.ui.TestIds.PASS_ORGANIZATION
import ru.nto.guestmanagement.mobile.ui.TestIds.PASS_VALID_PERIOD
import java.time.format.DateTimeFormatter

/**
 * A1. Карточка владельца: ФИО, организация, срок действия, лимит проходов.
 * Подробности и теги — в README.md и в техническом задании.
 */
@Composable
fun PassHolderCard(pass: PassDetails, modifier: Modifier = Modifier) {
    // TODO A1
    Column {
        Text(
            text = pass.visitor.fullName, modifier = modifier.testTag(PASS_HOLDER)
        )
        if (!pass.visitor.organization.isNullOrBlank()) {
            Text(
                text = pass.visitor.organization, modifier = modifier.testTag(PASS_ORGANIZATION)
            )
        }
        val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
        val datetimeFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        Text(
            text = when {
                pass.validTo.dayOfYear == pass.validFrom.dayOfYear && pass.validTo.year == pass.validFrom.year -> stringResource(
                    R.string.pass_valid_same_day,
                    pass.validFrom.format(dateFormat),
                    pass.validFrom.format(timeFormat),
                    pass.validTo.format(timeFormat)
                )

                else -> stringResource(
                    R.string.pass_valid_range,
                    pass.validFrom.format(datetimeFormat),
                    pass.validTo.format(datetimeFormat)
                )
            }, modifier = modifier.testTag(PASS_VALID_PERIOD)
        )
        val endsWith = pass.maxEntries % 10
        Text(
            text = when {
                pass.maxEntries == 1 -> stringResource(R.string.pass_single_entry)
                else -> when (pass.maxEntries % 100) {
                    11, 12, 13, 14 -> stringResource(
                        R.string.pass_entries_limit_many, pass.maxEntries
                    )

                    else -> when (endsWith) {
                        1 -> stringResource(
                            R.string.pass_entries_limit_one, pass.maxEntries
                        )

                        2, 3, 4 -> stringResource(
                            R.string.pass_entries_limit_few, pass.maxEntries
                        )

                        else -> stringResource(
                            R.string.pass_entries_limit_many, pass.maxEntries
                        )
                    }
                }
            }, modifier = modifier.testTag(PASS_ENTRIES_LIMIT)
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GameScreenPreview() {
//    GuestManagementTheme {
//        val now = SystemTimeProvider.now()
//        val pass = PassDetails(
//            id = 17,
//            qrCode = "PASS-004347",
//            validFrom = now.minusHours(2),
//            validTo = now.plusHours(6),
//            maxEntries = 2,
//            revokedAt = null,
//            visitor = Visitor(42, "Иванов Иван Иванович", "ООО «НТО»"),
//        )
//        PassHolderCard(pass)
//    }
//}

