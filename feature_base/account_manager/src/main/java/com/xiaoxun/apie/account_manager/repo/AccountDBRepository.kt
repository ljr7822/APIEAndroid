package com.xiaoxun.apie.account_manager.repo

import android.content.Context
import androidx.annotation.WorkerThread
import com.xiaoxun.apie.account_manager.db.AccountDBConfig
import com.xiaoxun.apie.account_manager.db.AccountDatabase
import com.xiaoxun.apie.account_manager.db.AccountRecord
import com.xiaoxun.apie.common.db.APieDatabaseHolder
import com.xiaoxun.apie.common.utils.ThreadUtil
import io.reactivex.Observable

class AccountDBRepository(context: Context) : IAccountDBRepository {

    init {
        APieDatabaseHolder.initDb<AccountDatabase>(context, AccountDBConfig())
    }

    override fun findAccountDataByUserId(userId: String): Observable<AccountDataDescriptor> {
        return APieDatabaseHolder
            .getInstance(AccountDatabase::class.java)
            .accountDataDao()
            .findAccountDataRecord(userId)
            .map {
                AccountDataDescriptor(
                    userId = it.userId,
                    userName = it.userName,
                    userPortrait = it.userPortrait,
                    phoneNumber = it.phoneNumber,
                    token = it.token,
                    desc = it.desc,
                    sex = it.sex,
                    address = it.address,
                    grade = it.grade,
                    userType = it.userType
                )
            }
            .toObservable()
    }

    override fun findAccountTokenByUserId(userId: String): Observable<String> {
        return APieDatabaseHolder
            .getInstance(AccountDatabase::class.java)
            .accountDataDao()
            .findAccountTokenByUserId(userId)
            .toObservable()
    }

    override fun findAccountTokenByPhoneNum(phoneNumber: String): Observable<String> {
        return APieDatabaseHolder
            .getInstance(AccountDatabase::class.java)
            .accountDataDao()
            .findAccountTokenByPhoneNum(phoneNumber)
            .toObservable()
    }

    @WorkerThread
    override fun insertAccountData(data: AccountDataDescriptor) {
        ThreadUtil.runOnChildThread {
            val record = AccountRecord()
            record.userId = data.userId
            record.userName = data.userName
            record.userPortrait = data.userPortrait
            record.phoneNumber = data.phoneNumber
            record.token = data.token
            record.desc = data.desc
            record.sex = data.sex
            record.address = data.address
            record.grade = data.grade
            record.userType = data.userType
            APieDatabaseHolder
                .getInstance(AccountDatabase::class.java)
                .accountDataDao()
                .insert(record)
        }
    }
}