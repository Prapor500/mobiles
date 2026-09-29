package ru.mtuci.drivenext.domain

import ru.mtuci.drivenext.data.LocalSettingsRepository

class IsOnboardingCompletedUseCase(private val repository: LocalSettingsRepository) {
    operator fun invoke(): Boolean = repository.isOnboardingCompleted()
}

class CompleteOnboardingUseCase(private val repository: LocalSettingsRepository) {
    operator fun invoke() = repository.setOnboardingCompleted()
}

class HasAccessTokenUseCase(private val repository: LocalSettingsRepository) {
    operator fun invoke(): Boolean = repository.hasAccessToken()
}
