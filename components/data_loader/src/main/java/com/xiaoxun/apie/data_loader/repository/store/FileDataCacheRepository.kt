package com.xiaoxun.apie.data_loader.repository.store

import com.xiaoxun.apie.data_loader.repository.cache.DataCacheDescriptor
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheRepository
import io.reactivex.Observable
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheManager
import java.io.File
import java.util.concurrent.TimeUnit
import com.xiaoxun.apie.common.utils.FileUtils
import com.xiaoxun.apie.data_loader.utils.DataLoaderLogger

class FileDataCacheRepository : DataCacheRepository {
    override fun findDataCacheByID(subDir: String, dataType: String, timeout: Long): Observable<DataCacheDescriptor> {
        return Observable.create {
            val dir = if (subDir.isNotEmpty()) subDir + File.separator else subDir
            val file = File(
                DataCacheManager.getFileCacheDir() + File.separator + dir + DataCacheManager.getFileCacheName(
                    dataType
                )
            )
            if (file.exists() && file.isFile && isTimeout(file.lastModified(), timeout).not()) {
                val dataCacheDescriptor = DataCacheDescriptor(dataType = dataType, data = FileUtils.readFileContent(file))
                if (it.isDisposed.not()) {
                    it.onNext(dataCacheDescriptor)
                    it.onComplete()
                }
            } else {
                DataLoaderLogger.d("文件缓存没有数据")
                if (it.isDisposed.not()) it.onError(Exception("文件缓存没有数据"))
            }
        }
    }


    override fun insertDataCache(data: DataCacheDescriptor) {
        kotlin.runCatching {
            val subDir = data.subDir
            val dir = if (subDir.isNotEmpty()) subDir + File.separator else subDir
            val file = File(
                DataCacheManager.getFileCacheDir() + File.separator + dir + DataCacheManager.getFileCacheName(
                    data.dataType
                )
            )
            if (file.exists()){
                file.delete()
            }
            if (file.parentFile?.exists() == false){
                file.parentFile?.mkdirs()
            }
            file.createNewFile()
            FileUtils.writeContentToFile(file, data.data ?: "")
        }
    }

    companion object {
        private const val MIN_TIMEOUT = 10

        private fun isTimeout(lastModified: Long, timeout: Long): Boolean {
            if (timeout < MIN_TIMEOUT) {
                return false
            }
            return System.currentTimeMillis() - lastModified >= TimeUnit.SECONDS.toMillis(timeout)
        }
    }
}