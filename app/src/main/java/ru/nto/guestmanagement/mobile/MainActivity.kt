package ru.nto.guestmanagement.mobile


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.nto.guestmanagement.mobile.data.DemoPassRepository
import ru.nto.guestmanagement.mobile.data.SystemTimeProvider
import ru.nto.guestmanagement.mobile.ui.PassScreen
import ru.nto.guestmanagement.mobile.ui.PassViewModel
import ru.nto.guestmanagement.mobile.ui.theme.GuestManagementTheme

class MainActivity : ComponentActivity() {
    private val viewModel: PassViewModel by viewModels {
        PassViewModel.Factory(DemoPassRepository(SystemTimeProvider))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuestManagementTheme {
                val state by viewModel.state.collectAsState()
                PassScreen(
                    state = state,
                    timeProvider = SystemTimeProvider,
                    onRefresh = viewModel::refresh,
                    onRetry = viewModel::retry,
                )
            }
        }
    }
}
