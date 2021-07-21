package rocks.ditto.miral.eve_plugin.entity

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.*

class EveOrderVO(
    @SerializedName("order_id")
    val orderId: BigDecimal,
    @SerializedName("type_id")
    val typeId: String,
    val price: BigDecimal,
    @SerializedName("min_volume")
    val minVolume: Int,
    @SerializedName("volume_total")
    val volumeTotal: Int,
    @SerializedName("volume_remain")
    val volumeRemain: Int,
    val systemId: BigDecimal, val locationId: Int,
    val range: String, val duration: Int,
    val issued: Date,
    @SerializedName("is_buy_order")
    val isBuyOrder: Boolean
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