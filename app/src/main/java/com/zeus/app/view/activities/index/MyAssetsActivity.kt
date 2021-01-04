package com.zeus.app.view.activities.index

import android.content.Context
import android.content.Intent
import com.zeus.app.R
import com.zeus.app.model.entities.Assets
import com.zeus.app.model.entities.Transfer
import com.zeus.app.presenter.index.MyAssetsPresenter
import com.zeus.app.view.adapters.TokenAssetsAdapter
import com.zeus.app.view.adapters.TransferRecordAdapter
import com.zeus.app.view.base.BaseActivity
import com.zeus.app.view.ivews.index.IMyAssetsView
import kotlinx.android.synthetic.main.activity_my_assets.*
import kotlinx.android.synthetic.main.include_toolbar.*

class MyAssetsActivity : BaseActivity<IMyAssetsView, MyAssetsPresenter>(), IMyAssetsView {

    companion object {
        fun startMyAssetsActivity(context: Context) {
            context.startActivity(Intent(context, MyAssetsActivity::class.java))
        }
    }

    private lateinit var assetsAdapter: TokenAssetsAdapter
    private lateinit var transferRecordAdapter: TransferRecordAdapter
    override fun getLayout(): Int {
        return R.layout.activity_my_assets
    }

    override fun getTitleText(): String {
        return "我的资产"
    }

    private var clickTime = 0L
    override fun initViews() {
        initToolbar(commToolBar)
        assetsAdapter = TokenAssetsAdapter()
        tokenList.adapter = assetsAdapter
        assetsAdapter.setOnItemClickListener { _, _, position ->
            val time = System.currentTimeMillis()
            if (time - clickTime < 2000) {
                return@setOnItemClickListener
            }
            clickTime = time
            when (position) {
                0 -> ExtractActivity.startExtractActivity(mContext)
                1 -> RechargeCoinActivity.startRechargeCoinActivity(mContext)
                2 -> RechargeCoinActivity.startRechargeCoinActivity(mContext,"USDT")
            }
        }

        transferRecordAdapter = TransferRecordAdapter()
        transferRecord.adapter = transferRecordAdapter
        transferRecordAdapter.loadMoreModule.setOnLoadMoreListener {
            pageNum++
            presenter?.getTransferRecord()
        }
    }

    override fun initData() {
        presenter?.getAssets()
        presenter?.getTransferRecord()
    }

    override fun createPresenter(): MyAssetsPresenter? {
        return MyAssetsPresenter()
    }

    override fun createView(): IMyAssetsView? {
        return this
    }

    private var pageNum = 1
    override fun getPage(): Int {
        return pageNum
    }

    override fun setAssets(list: List<Assets>) {
        assetsAdapter.setList(list)
    }

    override fun setTransferRecord(list: List<Transfer>) {
        if (getPage() == 1) {
            transferRecordAdapter.setList(list)
        } else {
            transferRecordAdapter.addData(list)
        }

        if (list.size < 20) {
            transferRecordAdapter.loadMoreModule.loadMoreEnd()
        } else {
            transferRecordAdapter.loadMoreModule.loadMoreComplete()
        }
    }
}