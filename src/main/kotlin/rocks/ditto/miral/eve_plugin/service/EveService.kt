package rocks.ditto.miral.eve_plugin.service

import org.json.JSONObject
import java.text.DecimalFormat
import khttp.get as httpGet

object EveService {

    private val MONEY_DEC = DecimalFormat("#,###.00")

    fun fetchJita(name: String): String {
        if (name == "") {
            return "请输入物品名字"
        }
        var id = fetchTypId(name)
        val jitaBuyOrder =  fetchOrder(id, "buy")
        val jitaSellOrder = fetchOrder(id, "sell")
        var jita = "$name\n" +
            "jitaBuy: $jitaBuyOrder\n" +
            "jitaSell: $jitaSellOrder"
        return jita
    }

    fun fetchTypId(propertyName: String): Int {
        var response = khttp.get("https://www.fuzzwork.co.uk/api/typeid.php?typename=$propertyName")
        var id = response.jsonObject.get("typeID")
        if (id !is Int) {
            throw Exception("没有找到该物品:$propertyName")
        }
        return id
    }

    fun fetchOrder(typeId: Int, orderType: String): String {
        var response = httpGet(
            "https://esi.evetech.net/dev/markets/10000002/orders/?" +
                "datasource=tranquility" +
                "&order_type=$orderType" +
                "&page=1" +
                "&type_id=$typeId"
        )
        if (response.statusCode == 200) {
            var orders = response.jsonArray
            for (order in orders) {
                if (order is JSONObject) {
                    println(order)
                    var price = MONEY_DEC.format(order.get("price"))
                    var amount = order.get("volume_remain")
                    return "price: $price amount: $amount"
                }
            }
        }
        return ""
    }
}