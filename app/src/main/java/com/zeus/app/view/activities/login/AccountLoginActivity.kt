package com.zeus.app.view.activities.login

import android.content.Context
import android.content.Intent
import com.zeus.app.R
import com.zeus.app.presenter.login.LoginPresenter
import com.zeus.app.view.activities.MainActivity
import com.zeus.app.view.base.BaseActivity
import com.zeus.app.view.ivews.login.ILoginView
import com.zeus.app.view.weidget.JClickListener
import kotlinx.android.synthetic.main.activity_login.*

class AccountLoginActivity : BaseActivity<ILoginView, LoginPresenter>(), ILoginView {
    companion object {
        fun startLoginActivity(context: Context, from: Int = 0) {
            context.startActivity(Intent(context, AccountLoginActivity::class.java).apply {
                putExtra("from", from)
            })
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_login
    }

    override fun initViews() {
        btnLogin.setOnClickListener(JClickListener {
            presenter?.login()
        })
        tvForgetPwd.setOnClickListener(JClickListener {
            ResetLoginPwdQueryActivity.startResetLoginPwdQueryActivity(this)
        })
        btnSmsLogin.setOnClickListener(JClickListener {
            QueryIdResultActivity.startQueryIdResultActivity(this)
        })
        tvRegister.setOnClickListener(JClickListener {
            RegisterActivity.startRegisterActivity(this)
        })
    }

    override fun initData() {
    }

    override fun createPresenter(): LoginPresenter? {
        return LoginPresenter()
    }

    override fun createView(): ILoginView? {
        return this
    }

    override fun getAccount(): String {
        return edAccount.text.toString().trim()
    }

    override fun getPwd(): String {
        return edPwd.text.toString().trim()
    }

    override fun loginSuccess() {
        MainActivity.startMainActivity(mContext)
        finish()
    }
}