package com.xiaoxun.apie.data_loader.loader

import android.database.Observable
import android.provider.ContactsContract.Data

interface IRequest {
    fun request(): Observable<Data>
}