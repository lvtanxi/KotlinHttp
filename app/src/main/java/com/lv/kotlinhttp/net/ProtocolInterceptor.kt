package com.lv.kotlinhttp.net

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject

/**
 * Date: 2017-06-21
 * Time: 15:30
 * Description:
 */
class ProtocolInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        chain?.let {
            val response = chain.proceed(chain.request())
            val medizType = MediaType.parse("application/json; chartset='utf-8'")
            val data = parseDataFromBody(response.body()?.string())
            return response.newBuilder().body(ResponseBody.create(medizType, data)).build()
        }
        throw RuntimeException("this Interceptor.Chain is empty")
    }

    private fun parseDataFromBody(body: String?): String {
        var message: String
        try {
            val json = JSONObject(body)
            val code = json.optInt("code")
            message = json.optString("msg")
            if (code == 100)
                return json.optString("data")
            throw HttpException(code, message)
        } catch (e: Exception) {
            message = "数据解析错误,请稍后重试!"
        }
        throw  RuntimeException(message)
    }

}