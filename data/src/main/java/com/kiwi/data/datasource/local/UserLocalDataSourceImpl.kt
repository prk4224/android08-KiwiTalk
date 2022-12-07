package com.kiwi.data.datasource.local

import com.kiwi.data.AppPreference
import com.kiwi.data.Const
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val pref: AppPreference,
    private val ioDispatcher: CoroutineDispatcher,
) : UserLocalDataSource {

    override suspend fun saveToken(
        id: String?, name: String?, imageUrl: String?
    ): Unit = withContext(ioDispatcher) {
        id?.let { pref.setString(Const.LOGIN_ID_KEY, it) }
        name?.let { pref.setString(Const.LOGIN_NAME_KEY, it) }
        imageUrl?.let { pref.setString(Const.LOGIN_URL_KEY, it) }
    }

    override suspend fun deleteToken() {
        pref.setString(Const.LOGIN_ID_KEY, Const.EMPTY_STRING)
        //pref.setString(Const.LOGIN_NAME_KEY, Const.EMPTY_STRING)
        //pref.setString(Const.LOGIN_URL_KEY, Const.EMPTY_STRING)
    }

    override fun getToken(): String {
        return pref.getString(
            Const.LOGIN_ID_KEY, Const.EMPTY_STRING
        )
    }

}