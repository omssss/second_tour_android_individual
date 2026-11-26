package ru.nto.guestmanagement.mobile.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.nto.guestmanagement.mobile.R
import ru.nto.guestmanagement.mobile.data.TimeProvider
import ru.nto.guestmanagement.mobile.model.PassScreenState
import ru.nto.guestmanagement.mobile.task.a2.PassAccessPanel
import ru.nto.guestmanagement.mobile.task.a1.PassHolderCard
import ru.nto.guestmanagement.mobile.task.a3.PassLoadControls

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassScreen(
    state: PassScreenState,
    timeProvider: TimeProvider,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.screen_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (state is PassScreenState.Content) {
                PassHolderCard(pass = state.data.pass)
                PassAccessPanel(data = state.data, timeProvider = timeProvider)
            }
            PassLoadControls(state = state, onRefresh = onRefresh, onRetry = onRetry)
        }
    }
}
