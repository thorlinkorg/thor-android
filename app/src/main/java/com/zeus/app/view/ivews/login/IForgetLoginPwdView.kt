package com.zeus.app.view.ivews.login

import com.zeus.app.view.ivews.base.ISendCodeView

interface IForgetLoginPwdView : ISendCodeView {
    fun getSMS(): String
    fun getPwd(): String
    fun getResetPwd(): String
    fun loginName(): String
    fun reserSuccess()
}