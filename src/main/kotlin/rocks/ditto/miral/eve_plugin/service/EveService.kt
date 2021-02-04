package rocks.ditto.miral.eve_plugin.service

import org.json.JSONObject
import khttp.get as httpGet

object EveService {

    fun fetchJitaBuy(msg: String): String {
        if (msg == "") {
            return "请输入物品名字"
        }
        var id = fetchTypId(msg)
        return fetchOrder(id)
    }

    fun fetchTypId(propertyName: String): Int {
        var response = khttp.get("https://www.fuzzwork.co.uk/api/typeid.php?typename=$propertyName")
        var id = response.jsonObject.get("typeID")
        if (id !is Int) {
            throw Exception("没有找到该物品:$propertyName")
        }
        return id
    }

    fun fetchOrder(typeId: Int): String {
        var response = httpGet(
            "https://esi.evetech.net/dev/markets/10000002/orders/?datasource=tranquility&order_type=buy&page=1" +
                "&type_id=$typeId"
        )
        if (response.statusCode == 200) {
            var orders = response.jsonArray
            for (order in orders) {
                if (order is JSONObject) {
                    println(order)
                    var price = order.get("price")
                    return "price: $price"
                }
            }
        }
        return ""
    }
}