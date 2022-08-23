package rocks.ditto.miral.eve_plugin.service

import org.json.JSONObject
import java.text.DecimalFormat
import khttp.get as httpGet
import com.google.gson.Gson
import khttp.responses.Response
import rocks.ditto.miral.eve_plugin.entity.EveOrderVO
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

/**
 * eve市场实例
 */
object EveService {

    private val MONEY_DEC = DecimalFormat("#,###.00")

    private val gson = Gson()

    fun fetchJita(name: String): String {
        try {
            if (name == "") {
                return "请输入物品名字"
            }
            var id = fetchTypId(name)
            val jitaBuyOrder = fetchOrder(id, "buy")
            val jitaSellOrder = fetchOrder(id, "sell")
            var jita = "$name\n" +
                "jitaSell: $jitaSellOrder\n" +
                "jitaBuy: $jitaBuyOrder\n"
            return jita
        } catch (e: Exception) {
            return e.message.toString()
        }
    }

    fun fetchJitaNew(name: String): String {
        try {
            if (name == "") {
                return "请输入物品名字"
            }
            var inv = fetchInv(name)
            if (inv !is JSONObject) {
                return "找不到该物品！"
            }
            var id = inv?.get("id") as Int
            var name = inv?.get("name")
            val jitaBuyOrder = fetchOrder(id, "buy")
            val jitaSellOrder = fetchOrder(id, "sell")
            var jita = "$name\n" +
                "jitaSell: $jitaSellOrder\n" +
                "jitaBuy: $jitaBuyOrder\n"
            return jita
        } catch (e: Exception) {
            return e.message.toString()
        }
    }

    fun fetchTypId(propertyName: String): Int {
        var response = khttp.get("https://www.fuzzwork.co.uk/api/typeid.php?typename=$propertyName")
        var id = response.jsonObject.get("typeID")
        val typeName = response.jsonObject.get("typeName")
        if (id == 0 || typeName.equals("bad item")) {
            throw Exception("没有找到该物品:$propertyName")
        }
        return id as Int
    }

    // eve marketer
    // https://evemarketer.com/api/v1/types/search?q=%E4%B9%8C%E9%B8%A6&language=zh&important_names=false
    fun fetchInv(propertyName: String): JSONObject? {
        var headers = mapOf(
            "user-agent" to "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36",
            "accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
            "accept-language" to "zh-CN,zh;q=0.9,en-GB;q=0.8,en;q=0.7,zh-HK;q=0.6,zh-TW;q=0.5"
        )
        var response = khttp.get("https://evemarketer.com/api/v1/types/search?q=$propertyName", headers = headers)
        if (response.statusCode == 200) {
            var invs = response.jsonArray
            for (inv in invs) {
                print(inv)
                if (inv is JSONObject) {
                    return inv
                }
            }
        }
        return null
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
            var orderVOs: ArrayList<EveOrderVO> = ArrayList()
            for (order in orders) {
                if (order is JSONObject) {
                    println(order)
                    var orderVO = gson.fromJson(order.toString(), EveOrderVO::class.javaObjectType)
                    var price = MONEY_DEC.format(order.get("price"))
                    var amount = order.get("volume_remain")
                    orderVOs.add(orderVO)
                }
            }
            orderVOs.sort()
            if (orderVOs.size == 0) {
                return "no orders"
            } else {
                var first = orderVOs.get(0)
                return "price: ${MONEY_DEC.format(first.price)}"
            }
        }
        return ""
    }

    fun fetchPrice(typeId: Int, orderType: String): Int {
        var response = httpGet(
            "https://esi.evetech.net/dev/markets/10000002/orders/?" +
                "datasource=tranquility" +
                "&order_type=$orderType" +
                "&page=1" +
                "&type_id=$typeId"
        )
        if (response.statusCode == 200) {
            var orders = response.jsonArray
            var orderVOs: ArrayList<EveOrderVO> = ArrayList()
            for (order in orders) {
                if (order is JSONObject) {
                    println(order)
                    var orderVO = gson.fromJson(order.toString(), EveOrderVO::class.javaObjectType)
                    var price = MONEY_DEC.format(order.get("price"))
                    var amount = order.get("volume_remain")
                    orderVOs.add(orderVO)
                }
            }
            orderVOs.sort()
            if ("buy".equals(orderType)) {
                orderVOs.reverse()
            }
            if (orderVOs.size == 0) {
                return 0
            } else {
                var first = orderVOs.get(0)
                return first.price as Int
            }
        }
        return 0
    }


    fun fetchPlexPrice(): String {
        var plexId = 44992
        var response = httpGet(
            "https://esi.evetech.net/dev/markets/10000002/orders/?" +
                "datasource=tranquility" +
                "&page=1" +
                "&type_id=$plexId"
        )
        var orders = processOrder(response)
        var sellOrders = orders.filter { !it.isBuyOrder }
        var buyOrders = orders.filter { it.isBuyOrder }


        var sell = BigDecimal.ZERO
        if (sellOrders.size > 0) {
            Collections.sort(sellOrders)
            sell = sellOrders[0].price
        }
        var buy = BigDecimal.ZERO
        if (buyOrders.size > 0) {
            Collections.sort(buyOrders)
            buy = buyOrders[0].price
        }

        return "Plex\n" +
            "jitaSell: "+ MONEY_DEC.format(sell)+ "\n" +
            "jitaBuy: "+ MONEY_DEC.format(buy)+ "\n" +
            "500 Plex\n" +
            "jitaSell: " + MONEY_DEC.format(sell.multiply(BigDecimal.valueOf(500))) + "\n" +
            "jitaBuy: " + MONEY_DEC.format(buy.multiply(BigDecimal.valueOf(500))) + "\n"
    }

    fun processOrder(response: Response): List<EveOrderVO> {
        if (response.statusCode == 200) {
            var orders = response.jsonArray
            var orderVOs: ArrayList<EveOrderVO> = ArrayList()
            for (order in orders) {
                if (order is JSONObject) {
                    println(order)
                    var orderVO = gson.fromJson(order.toString(), EveOrderVO::class.javaObjectType)
                    orderVOs.add(orderVO)
                }
            }
            return orderVOs
        }
        throw Exception("Fetch order failed!")
    }


}