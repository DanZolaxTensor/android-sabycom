package ru.tensor.sabycom.widget.repository

import ru.tensor.sabycom.data.UserData

/**
 * @author ma.kolpakov
 */
internal class Repository(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {
    fun sendPushToken(token: String) {
        localRepository.savePushToken(token)
        syncUserData()
    }

    fun registerUser(userData: UserData) {
        localRepository.saveUser(userData)
        syncUserData()
    }

    fun setApiKey(apiKey: String) {
        localRepository.saveApiKey(apiKey)
    }

    fun getUserData() = localRepository.getUserData()

    private fun syncUserData() {
        remoteRepository.performRegisterSync(
            localRepository.getApiKey(),
            localRepository.getUserData(),
            localRepository.getPushToken()
        )
    }

    fun getUnreadMessageCount(callback: (Int) -> Unit) {
        remoteRepository.getUnreadMessageCount(localRepository.getApiKey(), localRepository.getUserData(), callback)
    }

    fun getApiKey() = localRepository.getApiKey()
}