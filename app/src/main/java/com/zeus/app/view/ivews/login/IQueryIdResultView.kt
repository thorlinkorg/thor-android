package com.zeus.app.view.ivews.login

import com.zeus.app.view.ivews.base.IBaseView

interface IQueryIdResultView : IBaseView {
    fun setPhone(): String
    fun getLoginName(loginNameList: List<String>)
    fun resultFail()
}