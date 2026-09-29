package ru.mtuci.drivenext.domain

import ru.mtuci.drivenext.data.NetworkRepository

class CheckInternetUseCase(private val repository: NetworkRepository) {
    operator fun invoke(): Boolean = repository.hasInternet()
}
