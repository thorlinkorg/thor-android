package com.zeus.app.model

import com.joel.http.JHttp
import com.zeus.app.model.entities.*
import com.zeus.app.model.utils.ZeusException
import com.zeus.app.model.utils.ioMain
import com.zeus.app.utils.isEthAddress
import io.reactivex.Observable
import java.math.BigDecimal

val indexService = JHttp.instance.buildService(IndexService::class.java)

fun getIndexBanner(): Observable<Result<List<Banner>>> {
    return indexService.getIndexBanner().compose(ioMain())
}

fun getTokenPrice(): Observable<Result<List<TokenPrice>>> {
    return indexService.getTokenPrice().compose(ioMain())
}

fun getVideoList(page: Int, pageSize: Int = 20): Observable<Result<List<Video>>> {
    return indexService.getVideo(page, pageSize).compose(ioMain())
}

fun getFeeOdin(): Observable<Result<Fee>> {
    return indexService.getFeeOdin().compose(ioMain())
}

fun withdrawThor(
    num: BigDecimal,
    toaddress: String,
    pwd: String,
    code: String,
    phone: String
): Observable<Result<Any>> {

    if (toaddress.isEmpty()) {
        return Observable.create<Result<Any>> {
            it.onNext(
                Result(
                    ZeusException.TO_ADDRESS_EMPTY.code,
                    ZeusException.TO_ADDRESS_EMPTY.message
                )
            )
        }
    }

    if (!isEthAddress(toaddress)) {
        return Observable.create<Result<Any>> {
            it.onNext(
                Result(
                    ZeusException.ADDRESS_INVALID.code,
                    ZeusException.ADDRESS_INVALID.message
                )
            )
        }
    }

//    if (num == BigDecimal(0)) {
//        return Observable.create<Result<Any>> {
//            it.onNext(Result(ZeusException.WITHDRAW_NUM.code, ZeusException.WITHDRAW_NUM.message))
//        }
//    }

    return indexService.withdrawThor(num.toString(), toaddress, pwd, 3, code, phone)
        .compose(ioMain())
}

fun getTransferRecord(page: Int): Observable<Result<List<Transfer>>> {
    return indexService.getTransferRecord(page).compose(ioMain())
}

fun getInviteBanner(): Observable<Result<List<Banner>>> {
    return indexService.getInviteBanner().compose(ioMain())
}


fun getNotice(page: Int): Observable<Result<List<Notice>>> {
    return indexService.getNotice(page).compose(ioMain())
}

fun getVersion(): Observable<Result<Version>> {
    return indexService.getVersion().compose(ioMain())
}

fun getAgreement(): Observable<Result<Agreement>> {
    return indexService.getAgreement().compose(ioMain())
}

fun getHomeActivity(): Observable<Result<RewardPlanImg>> {
    return indexService.getHomeActivity().compose(ioMain())
}

fun getRewardRecord(page: Int, pageSize: Int = 20): Observable<Result<List<RewardPlanRecord>>> {
    return indexService.getRewardRecord(page, pageSize).compose(ioMain())
}

fun getRewardTotal(): Observable<Result<RewardPlan>> {
    return indexService.getRewardTotal().compose(ioMain())
}
