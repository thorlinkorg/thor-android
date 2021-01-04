package com.zeus.app.presenter.index

import com.zeus.app.model.entities.Assets
import com.zeus.app.model.entities.Balance
import com.zeus.app.model.entities.Transfer
import com.zeus.app.model.getAccountBalance
import com.zeus.app.model.getTransferRecord
import com.zeus.app.model.utils.Jobserver
import com.zeus.app.presenter.base.BasePresenter
import com.zeus.app.view.ivews.index.IMyAssetsView

class MyAssetsPresenter : BasePresenter<IMyAssetsView>() {
    fun getAssets() {
        getAccountBalance()
            .subscribe(object : Jobserver<Balance>(view) {
                override fun onSuccess(data: Balance?) {
                    if (data == null) {
                        return
                    }

                    val assetsList = arrayListOf<Assets>()

                    val thor = Assets("THOR", data.availableThor)
                    val odin = Assets("ODIN", data.availableOdin)
                    val usdt = Assets("USDT", data.availableUsdt)
                    assetsList.add(thor)
                    assetsList.add(odin)
                    assetsList.add(usdt)
                    view?.setAssets(assetsList)
                }
            })
    }

    fun getTransferRecord() {
        getTransferRecord(view!!.getPage())
            .subscribe(object : Jobserver<List<Transfer>>(view,false) {
                override fun onSuccess(data: List<Transfer>?) {
                    if (data != null) {
                        view?.setTransferRecord(data)
                    }
                }
            })
    }
}