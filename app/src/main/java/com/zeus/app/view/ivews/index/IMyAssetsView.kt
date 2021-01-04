package com.zeus.app.view.ivews.index

import com.zeus.app.model.entities.Assets
import com.zeus.app.model.entities.Transfer
import com.zeus.app.view.ivews.base.IBaseView

interface IMyAssetsView : IBaseView {
    fun getPage():Int
    fun setAssets(list: List<Assets>)
    fun setTransferRecord(list: List<Transfer>)
}