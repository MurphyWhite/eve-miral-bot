package rocks.ditto.miral.eve_plugin.utils

import java.net.URL

object HttpUtils {

    fun request(url: String): String {
        val requestData = URL(url).readText()
        println(requestData)
        return requestData
    }


}