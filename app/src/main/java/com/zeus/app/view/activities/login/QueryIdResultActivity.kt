package com.zeus.app.view.activities.login

import android.content.Context
import android.content.Intent
import android.text.InputType
import com.zeus.app.R
import com.zeus.app.presenter.login.QueryIdResultPresenter
import com.zeus.app.utils.copy
import com.zeus.app.view.activities.shop.GoodsDetailActivity
import com.zeus.app.view.adapters.QueryIdResultAdapter
import com.zeus.app.view.base.BaseActivity
import com.zeus.app.view.ivews.login.IQueryIdResultView
import com.zeus.app.view.weidget.JClickListener
import kotlinx.android.synthetic.main.activity_query_id_result.*
import kotlinx.android.synthetic.main.include_toolbar.*

class QueryIdResultActivity : BaseActivity<IQueryIdResultView, QueryIdResultPresenter>(),
    IQueryIdResultView {

    companion object {
        fun startQueryIdResultActivity(context: Context) {
            context.startActivity(Intent(context, QueryIdResultActivity::class.java))
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_query_id_result
    }

    override fun getTitleText(): String {
        return "查询ID"
    }

    var type: Int = 0;
    lateinit var queryIdResultAdapter: QueryIdResultAdapter
    override fun initViews() {
        initToolbar(commToolBar)
        queryIdResultAdapter = QueryIdResultAdapter()
        recycleView.adapter = queryIdResultAdapter
        btnLogin.text = "确认"
        queryIdResultAdapter.setOnItemClickListener { _, _, position ->
            val loginName = queryIdResultAdapter.getItem(position)
            copy(this, loginName)
            showToast("复制成功")
        }
        btnLogin.setOnClickListener(JClickListener {
            when (type) {
                0 -> {
                    presenter?.queryIdResult()
                }
                1 -> {
                    AccountLoginActivity.startLoginActivity(this)
                }
            }
        })
    }

    override fun initData() {
    }

    override fun createPresenter(): QueryIdResultPresenter? {
        return QueryIdResultPresenter()
    }

    override fun createView(): IQueryIdResultView? {
        return this
    }

    override fun setPhone(): String {
        return edtShowAccount.text.toString().trim()
    }

    override fun getLoginName(loginNameList: List<String>) {
        btnLogin.text = "立即登录"
        type = 1
        edtShowAccount.setText("账号${
            edtShowAccount.text.toString().trim()
        }下有${loginNameList.size}个ID")
        edtShowAccount.inputType = InputType.TYPE_NULL
        queryIdResultAdapter.setNewInstance(loginNameList as MutableList<String>)
    }

    override fun resultFail() {
        showToast("该账号暂未注册")
    }
}