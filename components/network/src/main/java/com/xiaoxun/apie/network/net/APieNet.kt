package com.xiaoxun.apie.network.net

import com.xiaoxun.apie.network.net.client.APieRetrofit

class APieNet {
    companion object {
        private val retrofitMap: HashMap<String, APieRetrofit> = hashMapOf()

        /**
         * 存放RetrofitClient.
         *
         * @param retrofitClientKey 用来标记存放的[APieRetrofit]
         * @param retrofit 存放的的[APieRetrofit]实例
         */
        @JvmStatic
        fun putAPieRetrofit(retrofitClientKey: String, retrofit: APieRetrofit) {
            retrofitMap[retrofitClientKey] = retrofit
        }

        /**
         *  获取对应RetrofitClient.
         *
         * @param retrofitClientKey 存放RetrofitClient时设置的key
         * @return 返回存放的Retrofit实例
         * @throws IllegalStateException 根据{@link retrofitClientKey}获取的实力为空时将抛出这个异常
         */
        @Throws(IllegalStateException::class)
        @JvmStatic
        fun getAPieRetrofit(retrofitClientKey: String): APieRetrofit {
            if (retrofitMap[retrofitClientKey] == null) {
                val retrofit = APieRetrofit(APieNetwork.getRetrofit())
                putAPieRetrofit(retrofitClientKey, retrofit)
            }
            return checkNotNull(retrofitMap[retrofitClientKey]) { "$retrofitClientKey retrofitClient is absent." }
        }

        /**
         * 获取一个@{link T}类型的实现，这个实现中的接口可以用来发送http请求
         *
         * @param T  接口类型
         * @param retrofitClientKey 存放xyRetrofit时用到的key
         * @param service 实现了@{link T}类型的class
         * @return 返回实现了@{link T} 类型的实例
         */
        @Throws(IllegalStateException::class)
        @JvmStatic
        fun <T> getService(retrofitClientKey: String, service: Class<T>): T {
            return getAPieRetrofit(retrofitClientKey).create(service)
        }
    }
}