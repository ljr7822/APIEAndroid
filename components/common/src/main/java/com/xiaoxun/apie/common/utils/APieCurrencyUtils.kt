package com.xiaoxun.apie.common.utils

import com.xiaoxun.apie.common.config.APieConfig

object APieCurrencyUtils {

    fun priceFormatting(currencyType: CurrencyType = APieConfig.getCurrencyType(), price: Double): String {
        return "${currencyType.symbol}${"%,.2f".format(price)}"
    }

    fun getThingAveragePrice(currencyType: CurrencyType = APieConfig.getCurrencyType(), price: Double, count: Int): String {
        return "${currencyType.symbol}${"%,.2f".format(price / count)}"
    }
}

enum class CurrencyType(val fullName: String, val symbol: String) {
    RMB("Chinese Yuan", "¥"),
    USD("US Dollar", "$"),
    EUR("Euro", "€"),
    JPY("Japanese Yen", "¥"),
    GBP("British Pound", "£"),
    AUD("Australian Dollar", "A$"),
    CAD("Canadian Dollar", "C$"),
    CHF("Swiss Franc", "CHF"),
    HKD("Hong Kong Dollar", "HK$"),
    INR("Indian Rupee", "₹")
}
