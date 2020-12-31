package com.zeus.app.model

import com.joel.http.JHttp
import com.zeus.app.model.entities.*
import com.zeus.app.model.utils.ZeusException
import com.zeus.app.model.utils.ioMain
import com.zeus.app.utils.*
import io.reactivex.Observable

val accountService = JHttp.instance.buildService(AccountService::class.java)

fun saveAccount(account: Account) {
    put("accountId", toBase64(account.userId))
    put("token", toBase64(account.token))
    putObject("account", account)
}

fun getAccountId(): String {
   val id = fromBase64( getString("accountId", ""))
    logd(id)
    return id
}


fun getToken(): String {
    val token = fromBase64(getString("token", ""))
    logd(token)
    return token
}

fun getAccount(): Account? {

    return getObject<Account>("account")
}

/**
 * 发送手机验证码
 * [phone] 手机号码
 * [token] 人机验证token
 */
fun sendSmsCode(phone: String, token: String): Observable<Result<Any>> {
    if (phone.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.PHONE_EMPTY.code, ZeusException.PHONE_EMPTY.message))
        }
    }
    return accountService.sendSms(phone, token).compose(ioMain())
}

/**
 * 发送邮箱验证码
 * [email] 邮箱
 * [token] 人机验证token
 */
fun sendEmailCode(email: String, token: String): Observable<Result<Any>> {
    if (email.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.EMAIL_EMPTY.code, ZeusException.EMAIL_EMPTY.message))
        }
    }
    return accountService.sendSms(email, token, 1).compose(ioMain())
}

/**
 * 手机号码注册
 * [phone] 手机号码
 * [pwd] 密码
 * [verifyCode] 验证码
 * [payPassword] 支付密码
 * [inviteCode] 邀请码
 */
fun phoneRegister(
    phone: String,
    pwd: String,
    verifyCode: String,
    payPassword: String,
    inviteCode: String
): Observable<Result<Account>> {

    if (phone.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.PHONE_EMPTY.code, ZeusException.PHONE_EMPTY.message))
        }
    }

    if (pwd.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.PWD_EMPTY.code, ZeusException.PWD_EMPTY.message))
        }
    }
    if (pwd.length < 6) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.PWD_LENGTH_ERROR.code,
                    ZeusException.PWD_LENGTH_ERROR.message
                )
            )
        }
    }

    if (verifyCode.isEmpty()) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.VERIFYCODE_EMPTY.code,
                    ZeusException.VERIFYCODE_EMPTY.message
                )
            )
        }
    }

    if (payPassword.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.PAYPWD_EMPTY.code, ZeusException.PAYPWD_EMPTY.message))
        }
    }

    if (inviteCode.isEmpty()) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.INVITECODE_EMPTY.code,
                    ZeusException.INVITECODE_EMPTY.message
                )
            )
        }
    }

    return accountService.accountRegister(
        phone,
        toBase64(pwd),
        verifyCode,
        toBase64(payPassword),
        inviteCode,
        0
    )
        .compose(ioMain())
}

/**
 * 邮箱注册
 * [email] 邮箱
 * [pwd] 密码
 * [verifyCode] 验证码
 * [payPassword] 支付密码
 * [inviteCode] 邀请码
 */
fun emailRegister(
    email: String,
    pwd: String,
    verifyCode: String,
    payPassword: String,
    inviteCode: String
): Observable<Result<Account>> {

    if (email.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.EMAIL_EMPTY.code, ZeusException.EMAIL_EMPTY.message))
        }
    }

    if (pwd.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.PWD_EMPTY.code, ZeusException.PWD_EMPTY.message))
        }
    }
    if (pwd.length < 6) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.PWD_LENGTH_ERROR.code,
                    ZeusException.PWD_LENGTH_ERROR.message
                )
            )
        }
    }


    if (verifyCode.isEmpty()) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.VERIFYCODE_EMPTY.code,
                    ZeusException.VERIFYCODE_EMPTY.message
                )
            )
        }
    }

    if (payPassword.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.PAYPWD_EMPTY.code, ZeusException.PAYPWD_EMPTY.message))
        }
    }

    if (inviteCode.isEmpty()) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.INVITECODE_EMPTY.code,
                    ZeusException.INVITECODE_EMPTY.message
                )
            )
        }
    }

    return accountService.accountRegister(
        email,
        toBase64(pwd),
        verifyCode,
        toBase64(payPassword),
        inviteCode,
        1
    )
        .compose(ioMain())
}

/**
 *账号登录
 * [loginName] 账号
 * [pwd] 密码
 */
fun accountLogin(loginName: String, pwd: String): Observable<Result<Account>> {
    if (loginName.isEmpty()) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.LOGINNAME_EMPTY.code,
                    ZeusException.LOGINNAME_EMPTY.message
                )
            )
        }
    }

    if (pwd.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.PWD_EMPTY.code, ZeusException.PWD_EMPTY.message))
        }
    }

    return accountService.accountLogin(loginName, toBase64(pwd)).compose(ioMain())
}

/**
 * 验证码登录
 * [phone] 手机号码
 * [verifyCode] 验证码
 */
fun verCodeLogin(phone: String, verifyCode: String): Observable<Result<Account>> {
    if (phone.isEmpty()) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.LOGINNAME_EMPTY.code,
                    ZeusException.LOGINNAME_EMPTY.message
                )
            )
        }
    }

    if (verifyCode.isEmpty()) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.VERIFYCODE_EMPTY.code,
                    ZeusException.VERIFYCODE_EMPTY.message
                )
            )
        }
    }
    return accountService.verCodeLogin(phone, verifyCode).compose(ioMain())
}

/**
 * 重置密码
 * [phone] 手机号码
 * [verifyCode] 验证码
 * [newPassword] 新密码
 */
fun resetPassword(
    phone: String,
    verifyCode: String,
    newPassword: String,
    rePwd: String,
    loginName: String
): Observable<Result<Any>> {
    if (phone.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.PHONE_EMPTY.code, ZeusException.PHONE_EMPTY.message))
        }
    }

    if (verifyCode.isEmpty()) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.VERIFYCODE_EMPTY.code,
                    ZeusException.VERIFYCODE_EMPTY.message
                )
            )
        }
    }

    if (newPassword.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.PWD_EMPTY.code, ZeusException.PWD_EMPTY.message))
        }
    }


    if (newPassword != rePwd) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.PWD_NOT_EQUECE.code,
                    ZeusException.PWD_NOT_EQUECE.message
                )
            )
        }
    }
    return accountService.resetPassword(phone, toBase64(newPassword), verifyCode, loginName)
        .compose(ioMain())
}

/**
 * 退出登录
 */
fun logout(): Observable<Result<Any>> {
    return accountService.logout().compose(ioMain())
}

/**
 * 修改登录密码
 * [oldPwd] 旧密码
 * [newPwd] 新密码
 * [phone] 手机号码
 *
 */
fun updatePwd(
    oldPwd: String,
    newPwd: String,
    rePwd: String,
    phone: String
): Observable<Result<Any>> {
    if (oldPwd.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.PWD_OLD.code, ZeusException.PWD_OLD.message))
        }
    }
    if (newPwd.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.PWD_NEW.code, ZeusException.PWD_NEW.message))
        }
    }
    if (newPwd.length < 6) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.PWD_LENGTH_ERROR.code,
                    ZeusException.PWD_LENGTH_ERROR.message
                )
            )
        }
    }
    if (newPwd != rePwd) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.PWD_NOT_EQUECE.code,
                    ZeusException.PWD_NOT_EQUECE.message
                )
            )
        }
    }

    return accountService.updatePwd(toBase64(oldPwd), toBase64(newPwd), phone).compose(ioMain())
}

/**
 * 修改支付密码
 * [oldPwd] 旧密码
 * [newPwd] 新密码
 * [phone] 手机号码
 *
 */
fun updatePayPwd(
    oldPwd: String,
    newPwd: String,
    rePwd: String,
    phone: String
): Observable<Result<Any>> {
    if (oldPwd.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.PWD_OLD.code, ZeusException.PWD_OLD.message))
        }
    }
    if (newPwd.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.PWD_NEW.code, ZeusException.PWD_NEW.message))
        }
    }
    if (newPwd != rePwd) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.PWD_NOT_EQUECE.code,
                    ZeusException.PWD_NOT_EQUECE.message
                )
            )
        }
    }
    return accountService.updatePayPwd(toBase64(oldPwd), toBase64(newPwd), phone).compose(ioMain())
}

/**
 * 重置支付密码
 * [phone] 手机号码
 * [newPwd] 新密码
 * [verifyCode] 验证码
 */
fun resetPayPwd(
    phone: String,
    newPwd: String,
    rePwd: String,
    verifyCode: String,
    LoginName: String
): Observable<Result<Any>> {
    if (phone.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.PHONE_EMPTY.code, ZeusException.PHONE_EMPTY.message))
        }
    }

    if (verifyCode.isEmpty()) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.VERIFYCODE_EMPTY.code,
                    ZeusException.VERIFYCODE_EMPTY.message
                )
            )
        }
    }

    if (newPwd.isEmpty()) {
        return Observable.create {
            it.onNext(Result(ZeusException.PWD_EMPTY.code, ZeusException.PWD_EMPTY.message))
        }
    }

    if (newPwd != rePwd) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.PWD_NOT_EQUECE.code,
                    ZeusException.PWD_NOT_EQUECE.message
                )
            )
        }
    }
    return accountService.resetPayPwd(phone, toBase64(newPwd), verifyCode,LoginName).compose(ioMain())
}

fun getAccountBalance(): Observable<Result<Balance>> {
    return accountService.getAccountBalance().compose(ioMain())
}

fun getRechargeAddress(): Observable<Result<Address>> {
    return accountService.getRechargeAddress().compose(ioMain())
}

fun getQueryAccount(loginName: String): Observable<Result<QueryAccount>> {
    if (loginName.isEmpty()) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.USER_LOGIN_NAME.code,
                    ZeusException.USER_LOGIN_NAME.message
                )
            )
        }
    }
    if (isLoginName(loginName)) {
        return Observable.create {
            it.onNext(
                Result(
                    ZeusException.LOGIN_NAME.code,
                    ZeusException.LOGIN_NAME.message
                )
            )
        }
    }
    return accountService.queryAccount(loginName).compose(ioMain())
}

fun getQueryId(type: String, phone: String): Observable<Result<QueryId>> {
    return accountService.queryId(type, phone).compose(ioMain())
}






