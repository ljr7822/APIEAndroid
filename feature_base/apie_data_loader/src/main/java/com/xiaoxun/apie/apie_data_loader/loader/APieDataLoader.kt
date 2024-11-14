package com.xiaoxun.apie.apie_data_loader.loader

import com.xiaoxun.apie.data_loader.DataLoaderRepository
import com.xiaoxun.apie.data_loader.loader.APieSimpleDataLoader

class APieDataLoader<Data>(
    private val loaderName: String,
    private val repository: DataLoaderRepository
) : APieSimpleDataLoader<Data>(loaderName, repository) {
}