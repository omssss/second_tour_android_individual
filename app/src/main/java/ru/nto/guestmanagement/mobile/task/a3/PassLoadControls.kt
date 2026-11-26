package ru.nto.guestmanagement.mobile.task.a3


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import ru.nto.guestmanagement.mobile.R
import ru.nto.guestmanagement.mobile.model.PassScreenState
import ru.nto.guestmanagement.mobile.ui.TestIds

/** A3. Индикатор загрузки, ошибка с кнопкой «Повторить», кнопка «Обновить», надпись «Нет связи». */
@Composable
fun PassLoadControls(
    state: PassScreenState,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is PassScreenState.InitialLoading -> {
            CircularProgressIndicator(modifier = modifier.testTag(TestIds.PASS_LOADING))
        }

        is PassScreenState.Content -> {
            Column {
                if (state.isRefreshing) {
                    CircularProgressIndicator(modifier = modifier.testTag(TestIds.PASS_LOADING))
                    Button(onClick = onRefresh, enabled = false) {
                        Text(
                            text = stringResource(R.string.action_refresh),
                            modifier = modifier.testTag(
                                TestIds.PASS_REFRESH
                            )
                        )
                    }
                } else {
                    Button(onClick = onRefresh) {
                        Text(
                            text = stringResource(R.string.action_refresh),
                            modifier = modifier.testTag(
                                TestIds.PASS_REFRESH
                            )
                        )
                    }
                    if (state.isOffline) {
                        Text(
                            text = stringResource(R.string.offline),
                            modifier = modifier.testTag(TestIds.PASS_OFFLINE)
                        )
                    }
                }
            }
        }

        is PassScreenState.InitialError -> {
            Column {
                Text(text = state.message, modifier = modifier.testTag(TestIds.PASS_ERROR))
                Button(onClick = onRetry) {
                    Text(
                        text = stringResource(R.string.action_retry),
                        modifier = modifier.testTag(TestIds.PASS_RETRY)
                    )
                }
            }
        }
    }
}
