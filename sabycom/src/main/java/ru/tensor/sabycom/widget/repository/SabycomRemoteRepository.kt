package ru.tensor.sabycom.widget.repository

import org.json.JSONObject
import ru.tensor.sabycom.data.ApiClient
import ru.tensor.sabycom.data.UserData
import java.util.concurrent.Executors

/**
 * @author am.boldinov
 */
internal class SabycomRemoteRepository : RemoteRepository {

    private val executor by lazy { Executors.newSingleThreadExecutor() }

    override fun getUnreadMessageCount(apiKey: String, userData: UserData, callback: (Int) -> Unit) {
        executor.submit {
            ApiClient.get("externalUser/${userData.id}/${apiKey}/unread/${apiKey}",
                object : ApiClient.ResultCallback {
                    override fun onSuccess(result: JSONObject) {
                        callback(result.getJSONObject("result").getInt("count"))
                    }

                    override fun onFailure(code: Int, errorBody: JSONObject) {

                    }
                })
        }
    }

    override fun performRegisterSync(apiKey: String, userData: UserData, token: String?) {
        executor.submit {
            val data = JSONObject().apply {
                put("id", userData.id.toString())
                put("service_id", apiKey)
                put("name", userData.name)
                put("surname", userData.surname)
                put("email", userData.email)
                put("phone", userData.phone)
                put("push_token", token)
                put("push_os", "android")
            }
            ApiClient.put("externalUser/${userData.id}/$apiKey", data, object : ApiClient.ResultCallback {
                override fun onSuccess(result: JSONObject) {

                }

                override fun onFailure(code: Int, errorBody: JSONObject) {

                }
            })
        }
    }

}