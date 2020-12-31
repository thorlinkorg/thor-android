package com.zeus.app.presenter.login

import com.zeus.app.model.accountLogin
import com.zeus.app.model.entities.Account
import com.zeus.app.model.saveAccount
import com.zeus.app.model.utils.Jobserver
import com.zeus.app.presenter.base.BasePresenter
import com.zeus.app.view.ivews.login.ILoginView

class LoginPresenter : BasePresenter<ILoginView>() {
    fun login() {
        accountLogin(
            view!!.getAccount(),
            view!!.getPwd()
        ).subscribe(object : Jobserver<Account>(view) {
            override fun onSuccess(data: Account?) {
                if (data != null) {
                    saveAccount(data)
                    view?.loginSuccess()
                }
            }
        })
    }
}

