package com.xiaoxun.apie.data_loader.loader

class APieDataLoaderException(message: String, val errorCode: Int = -1) : Exception(message)