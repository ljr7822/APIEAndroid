package com.xiaoxun.apie.apie_data_loader

import android.content.Context
import com.xiaoxun.apie.apie_data_loader.repository.APieDataLoaderRepository
import com.xiaoxun.apie.apie_data_loader.request.account.login.password.LoginByPasswordRequest
import com.xiaoxun.apie.apie_data_loader.request.account.login.smscode.LoginBySmsCodeRequest
import com.xiaoxun.apie.apie_data_loader.request.account.sms.STSTokenParams
import com.xiaoxun.apie.apie_data_loader.request.plan.LoadPlans
import com.xiaoxun.apie.apie_data_loader.request.account.sms.SendSmsCodeParams
import com.xiaoxun.apie.apie_data_loader.request.account.user.QueryUser
import com.xiaoxun.apie.apie_data_loader.request.desire.CreateDesire
import com.xiaoxun.apie.apie_data_loader.request.desire.ExchangeDesire
import com.xiaoxun.apie.apie_data_loader.request.desire.LoadDesire
import com.xiaoxun.apie.apie_data_loader.request.desire.LoadDesireGroupRequest
import com.xiaoxun.apie.apie_data_loader.request.plan.CreatePlan
import com.xiaoxun.apie.apie_data_loader.request.group.CreatePlanGroup
import com.xiaoxun.apie.apie_data_loader.request.group.DeleteGroup
import com.xiaoxun.apie.apie_data_loader.request.plan.DeletePlan
import com.xiaoxun.apie.apie_data_loader.request.group.LoadPlanGroups
import com.xiaoxun.apie.apie_data_loader.request.plan.UpdatePlanCompletedCount
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.common_model.home_page.desire.DesireModel
import com.xiaoxun.apie.common_model.home_page.desire.DesireRespModel
import com.xiaoxun.apie.common_model.home_page.desire.group.DesireGroupRespModel
import com.xiaoxun.apie.common_model.home_page.group.DeleteGroupRespModel
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupRespModel
import com.xiaoxun.apie.common_model.home_page.plan.DeletePlanRespModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanRespModel
import com.xiaoxun.apie.common_model.sms.STSTokenModel
import com.xiaoxun.apie.common_model.sms.SmsCodeModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.loader.APieSimpleDataLoader
import com.xiaoxun.apie.data_loader.loader.IDataLoader
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheManager
import com.xiaoxun.apie.data_loader.utils.CacheStrategy
import io.reactivex.Observable

class DataLoaderManager private constructor() {
    private var repository: APieDataLoaderRepository? = null

    companion object {
        const val TAG = "DataLoaderManager"
        val instance: DataLoaderManager by lazy { DataLoaderManager() }
    }

    fun initWithContext(context: Context) {
        val dataCacheManager = DataCacheManager(context)
        repository = APieDataLoaderRepository(dataCacheManager)
    }

    private fun <Data> buildDataLoader(
        loaderName: String,
        memoryCacheEnable: Boolean = true
    ): IDataLoader<Data>? =
        repository?.let { APieSimpleDataLoader<Data>(loaderName, it, memoryCacheEnable) }

    private fun <T> error(): Observable<T> {
        return Observable.error<T>(
            Exception("loader is null, please call registerLoader in DataLoaderManager#initWithContext()")
        )
    }

    /**
     * 密码登录接口
     */
    fun loginByPassword(
        loginByPasswordRequestParams: LoginByPasswordRequest,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<AccountModel>> {
        val loader = buildDataLoader<AccountModel>(APieUrl.ACCOUNT_LOGIN_PASSWORD.name)
        return loader?.getData(loginByPasswordRequestParams, cacheStrategy) ?: Observable.error(
            Exception("loader is null")
        )
    }

    /**
     * 验证码登录接口
     */
    fun loginBySmsCode(
        loginBySmsCodeRequestParams: LoginBySmsCodeRequest,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<AccountModel>> {
        val loader = buildDataLoader<AccountModel>(APieUrl.ACCOUNT_LOGIN_SMS_CODE.name)
        return loader?.getData(loginBySmsCodeRequestParams, cacheStrategy) ?: Observable.error(
            Exception("loader is null")
        )
    }

    /**
     * 发送验证码接口
     */
    fun sendSmsCode(
        sendSmsCodeParams: SendSmsCodeParams,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<SmsCodeModel>> {
        val loader = buildDataLoader<SmsCodeModel>(APieUrl.ACCOUNT_SEND_SMS_CODE.name)
        return loader?.getData(sendSmsCodeParams, cacheStrategy)
            ?: Observable.error(Exception("loader is null"))
    }

    /**
     * 获取阿里云的token
     */
    fun getSTSToken(
        stsTokenParams: STSTokenParams,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<STSTokenModel>> {
        val loader = buildDataLoader<STSTokenModel>(APieUrl.ACCOUNT_GET_STS_TOKEN.name)
        return loader?.getData(stsTokenParams, cacheStrategy)
            ?: Observable.error(Exception("loader is null"))
    }

    /**
     * 查询用户的基本信息
     */
    fun getUserInfo(
        queryUser: QueryUser,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<AccountModel>> {
        val loader = buildDataLoader<AccountModel>(APieUrl.ACCOUNT_GET_USER_INFO.name)
        return loader?.getData(queryUser, cacheStrategy)
            ?: Observable.error(Exception("loader is null"))
    }

    /**
     * 获取用户的所有计划
     */
    fun getAllPlanByUserId(
        loadPlans: LoadPlans,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<PlanRespModel>> {
        val loader = buildDataLoader<PlanRespModel>(APieUrl.ACCOUNT_GET_ALL_PLAN_BY_USER_ID.name)
        return loader?.getData(loadPlans, cacheStrategy)
            ?: Observable.error(Exception("loader is null"))
    }

    /**
     * 创建一个分组
     */
    fun createPlanGroup(
        createPlanGroup: CreatePlanGroup,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<PlanGroupModel>> {
        val loader = buildDataLoader<PlanGroupModel>(APieUrl.CREATE_GROUP.name)
        return loader?.getData(createPlanGroup, cacheStrategy)
            ?: Observable.error(Exception("loader is null"))
    }

    /**
     * 获取用户的所有计划分组
     */
    fun getAllPlanGroupByUserId(
        loadPlanGroups: LoadPlanGroups,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<PlanGroupRespModel>> {
        val loader = buildDataLoader<PlanGroupRespModel>(APieUrl.GET_ALL_PLAN_GROUP_BY_USER_ID.name)
        return loader?.getData(loadPlanGroups, cacheStrategy)
            ?: Observable.error(Exception("loader is null"))
    }

    /**
     * 创建计划
     */
    fun createPlan(
        createPlan: CreatePlan,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<PlanModel>> {
        val loader = buildDataLoader<PlanModel>(APieUrl.CREATE_PLAN.name)
        return loader?.getData(createPlan, cacheStrategy)
            ?: Observable.error(Exception("loader is null"))
    }

    /**
     * 更新一个计划完成次数
     */
    fun updatePlanCompletedCount(
        updatePlanCompletedCount: UpdatePlanCompletedCount,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<PlanModel>> {
        val loader = buildDataLoader<PlanModel>(APieUrl.UPDATE_PLAN_COMPLETED_COUNT.name)
        return loader?.getData(updatePlanCompletedCount, cacheStrategy) ?: Observable.error(
            Exception("loader is null")
        )
    }

    /**
     * 删除一个计划
     */
    fun deletePlan(
        deletePlan: DeletePlan,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<DeletePlanRespModel>> {
        val loader = buildDataLoader<DeletePlanRespModel>(APieUrl.DELETE_PLAN.name)
        return loader?.getData(deletePlan, cacheStrategy)
            ?: Observable.error(Exception("loader is null"))
    }

    /**
     * 删除一个分组
     */
    fun deleteGroup(
        deleteGroup: DeleteGroup,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<DeleteGroupRespModel>> {
        val loader = buildDataLoader<DeleteGroupRespModel>(APieUrl.DELETE_GROUP.name)
        return loader?.getData(deleteGroup, cacheStrategy)
            ?: Observable.error(Exception("loader is null"))
    }

    /**
     * 创建心愿
     */
    fun createDesire(
        createDesire: CreateDesire,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<DesireModel>> {
        val loader = buildDataLoader<DesireModel>(APieUrl.CREATE_DESIRE.name)
        return loader?.getData(createDesire, cacheStrategy)
            ?: Observable.error(Exception("loader is null"))
    }

    /**
     * 获取心愿列表
     */
    fun loadDesireListByUserId(
        loadDesire: LoadDesire,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<DesireRespModel>> {
        val loader = buildDataLoader<DesireRespModel>(APieUrl.GET_DESIRE_BY_USER_ID.name)
        return loader?.getData(loadDesire, cacheStrategy)
            ?: Observable.error(Exception("loader is null"))
    }

    /**
     * 兑换心愿
     */
    fun exchangeDesire(
        exchangeDesire: ExchangeDesire,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<DesireModel>> {
        val loader = buildDataLoader<DesireModel>(APieUrl.EXCHANGE_DESIRE.name)
        return loader?.getData(exchangeDesire, cacheStrategy)
            ?: Observable.error(Exception("loader is null"))
    }

    /**
     * 拉取心愿分组
     */
    fun loadDesireGroupListByUserId(
        loadDesireGroupRequest: LoadDesireGroupRequest,
        cacheStrategy: CacheStrategy
    ): Observable<BaseResponse<DesireGroupRespModel>> {
        val loader = buildDataLoader<DesireGroupRespModel>(APieUrl.GET_DESIRE_GROUP_BY_USER_ID.name)
        return loader?.getData(loadDesireGroupRequest, cacheStrategy)
            ?: Observable.error(Exception("loader is null"))
    }
}