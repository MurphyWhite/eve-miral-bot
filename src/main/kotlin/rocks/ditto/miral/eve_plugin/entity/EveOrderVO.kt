package rocks.ditto.miral.eve_plugin.entity

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

class EveOrderVO(
    val orderId: BigDecimal, val typeId: String, val price: BigDecimal,
    val minVolume: Int, val volumeTotal: Int, val volumeRemain: Int,
    val systemId: BigDecimal, val locationId: Int,
    val range: String, val duration: Int,
    val issued: Date, val isBuyOrder: Boolean
) : Comparable<EveOrderVO> {

    /**
     * 用于对订单进行价格排序
     */
    override fun compareTo(other: EveOrderVO): Int {
        if (isBuyOrder) {
            return other.price.compareTo(price)
        } else {
            return price.compareTo(other.price)
        }
    }



}