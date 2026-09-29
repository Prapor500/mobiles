package ru.mtuci.drivenext.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.mtuci.drivenext.data.AndroidNetworkRepository
import ru.mtuci.drivenext.data.SharedPreferencesSettingsRepository
import ru.mtuci.drivenext.domain.CheckInternetUseCase
import ru.mtuci.drivenext.domain.CompleteOnboardingUseCase
import ru.mtuci.drivenext.domain.HasAccessTokenUseCase
import ru.mtuci.drivenext.domain.IsOnboardingCompletedUseCase

sealed interface StartupState {
    data object Splash : StartupState
    data object NoConnection : StartupState
    data object Onboarding : StartupState
    data object Login : StartupState
    data object Main : StartupState
    data class Error(val message: String) : StartupState
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val networkRepository = AndroidNetworkRepository(application)
    private val settingsRepository = SharedPreferencesSettingsRepository(application)

    private val checkInternet = CheckInternetUseCase(networkRepository)
    private val isOnboardingCompleted = IsOnboardingCompletedUseCase(settingsRepository)
    private val completeOnboarding = CompleteOnboardingUseCase(settingsRepository)
    private val hasAccessToken = HasAccessTokenUseCase(settingsRepository)

    private val _state = MutableStateFlow<StartupState>(StartupState.Splash)
    val state: StateFlow<StartupState> = _state.asStateFlow()

    private val _retrying = MutableStateFlow(false)
    val retrying: StateFlow<Boolean> = _retrying.asStateFlow()

    private var started = false

    fun start() {
        if (started) return
        started = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(2500)
                routeFromStartup()
            } catch (e: Exception) {
                _state.value = StartupState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun retryConnection() {
        if (_retrying.value) return
        viewModelScope.launch(Dispatchers.IO) {
            _retrying.value = true
            try {
                if (checkInternet()) {
                    routeAfterConnection()
                } else {
                    _state.value = StartupState.NoConnection
                }
            } catch (e: Exception) {
                _state.value = StartupState.Error(e.message ?: "Ошибка проверки подключения")
            } finally {
                _retrying.value = false
            }
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch(Dispatchers.IO) {
            completeOnboarding()
            routeAfterOnboarding()
        }
    }

    private fun routeFromStartup() {
        if (!checkInternet()) {
            _state.value = StartupState.NoConnection
            return
        }
        routeAfterConnection()
    }

    private fun routeAfterConnection() {
        if (!isOnboardingCompleted()) {
            _state.value = StartupState.Onboarding
            return
        }
        _state.value = if (hasAccessToken()) StartupState.Main else StartupState.Login
    }

    private fun routeAfterOnboarding() {
        _state.value = if (hasAccessToken()) StartupState.Main else StartupState.Login
    }
}
