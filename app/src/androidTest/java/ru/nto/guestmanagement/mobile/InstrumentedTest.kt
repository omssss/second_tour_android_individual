package ru.nto.guestmanagement.mobile

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.nto.guestmanagement.mobile.data.DemoPassRepository
import ru.nto.guestmanagement.mobile.data.TimeProvider
import ru.nto.guestmanagement.mobile.model.PassDetails
import ru.nto.guestmanagement.mobile.model.Visitor
import ru.nto.guestmanagement.mobile.task.a1.PassHolderCard
import ru.nto.guestmanagement.mobile.ui.PassScreen
import ru.nto.guestmanagement.mobile.ui.PassViewModel
import ru.nto.guestmanagement.mobile.ui.TestIds.PASS_ENTRIES_LIMIT
import ru.nto.guestmanagement.mobile.ui.TestIds.PASS_HOLDER
import ru.nto.guestmanagement.mobile.ui.TestIds.PASS_ORGANIZATION
import ru.nto.guestmanagement.mobile.ui.TestIds.PASS_VALID_PERIOD
import ru.nto.guestmanagement.mobile.ui.theme.GuestManagementTheme
import java.time.LocalDateTime


@RunWith(AndroidJUnit4::class)
class InstrumentedTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private class FakeTimeProvider(
        var currentTime: LocalDateTime = LocalDateTime.of(2026, 11, 26, 12, 0)
    ) : TimeProvider {
        override fun now(): LocalDateTime = currentTime
    }

    @Test
    fun normal() {
        val fixedNow = LocalDateTime.of(2026, 11, 26, 12, 0)
        val timeProvider = FakeTimeProvider(fixedNow)
        val repository = DemoPassRepository(timeProvider)
        val viewModel = PassViewModel(repository)

        composeTestRule.setContent {
            GuestManagementTheme {
                val state by viewModel.state.collectAsState()
                PassScreen(
                    state = state,
                    timeProvider = timeProvider,
                    onRefresh = viewModel::refresh,
                    onRetry = viewModel::retry,
                )
            }
        }

        // 1. Wait for DemoPassRepository's 700ms delay on initial load (call #1)
        composeTestRule.waitUntil(timeoutMillis = 3_000) {
            composeTestRule.onAllNodesWithText("Иванов Иван Иванович")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // 2. Assert loaded visitor & pass details
        composeTestRule.onNodeWithText("Иванов Иван Иванович").assertIsDisplayed()
        composeTestRule.onNodeWithText("ООО «НТО»").assertIsDisplayed()

    }

    private fun createPass(
        organization: String? = "ООО «НТО»",
        validFrom: LocalDateTime = LocalDateTime.of(2026, 11, 26, 10, 0),
        validTo: LocalDateTime = LocalDateTime.of(2026, 11, 26, 18, 0),
        maxEntries: Int = 2,
    ) = PassDetails(
        id = 17,
        qrCode = "PASS-004347",
        validFrom = validFrom,
        validTo = validTo,
        maxEntries = maxEntries,
        revokedAt = null,
        visitor = Visitor(42, "Иванов Иван Иванович", organization),
    )

    @Test
    fun displaysHolderOrganizationAndSameDayPeriod() {
        composeTestRule.setContent {
            GuestManagementTheme {
                PassHolderCard(pass = createPass())
            }
        }

        composeTestRule.onNodeWithTag(PASS_HOLDER)
            .assertIsDisplayed()
            .assertTextEquals("Иванов Иван Иванович")

        composeTestRule.onNodeWithTag(PASS_ORGANIZATION)
            .assertIsDisplayed()
            .assertTextEquals("ООО «НТО»")

        composeTestRule.onNodeWithTag(PASS_VALID_PERIOD)
            .assertIsDisplayed()
            .assertTextEquals("Действует 26.11.2026, 10:00–18:00") // Match your pass_valid_same_day string

        composeTestRule.onNodeWithTag(PASS_ENTRIES_LIMIT)
            .assertIsDisplayed()
            .assertTextEquals("Лимит: 2 прохода") // Match your pass_entries_limit_few string
    }

    @Test
    fun hidesOrganizationWhenNullOrBlank() {
        composeTestRule.setContent {
            GuestManagementTheme {
                PassHolderCard(pass = createPass(organization = "   "))
            }
        }

        composeTestRule.onNodeWithTag(PASS_ORGANIZATION).assertDoesNotExist()
    }

    @Test
    fun formatsMultiDayPeriodAndCrossYearDates() {
        val crossYearPass = createPass(
            validFrom = LocalDateTime.of(2026, 12, 31, 22, 0),
            validTo = LocalDateTime.of(2027, 1, 1, 6, 0),
            maxEntries = 1,
        )

        composeTestRule.setContent {
            GuestManagementTheme {
                PassHolderCard(pass = crossYearPass)
            }
        }

        composeTestRule.onNodeWithTag(PASS_VALID_PERIOD)
            .assertTextEquals("Действует с 31.12.2026 22:00 по 01.01.2027 06:00") // Match your pass_valid_range string

        composeTestRule.onNodeWithTag(PASS_ENTRIES_LIMIT)
            .assertTextEquals("Разовый пропуск") // Match your pass_single_entry string
    }
}