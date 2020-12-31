package com.zeus.app.view.ivews.login

import com.zeus.app.view.ivews.base.IBaseView

interface ILoginView : IBaseView {
    fun getAccount():String
    fun getPwd():String
    fun loginSuccess()
}