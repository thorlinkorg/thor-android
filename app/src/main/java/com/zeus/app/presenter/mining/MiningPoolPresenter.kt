package com.zeus.app.presenter.mining

import com.zeus.app.model.entities.Block
import com.zeus.app.model.entities.PoolBlock
import com.zeus.app.model.getPoolBlockData
import com.zeus.app.model.getThorBlockHeightsList
import com.zeus.app.model.utils.Jobserver
import com.zeus.app.presenter.base.BasePresenter
import com.zeus.app.view.ivews.mining.IMiningPoolView
import java.math.BigDecimal
import java.math.RoundingMode

class MiningPoolPresenter : BasePresenter<IMiningPoolView>() {
    fun getBlockData() {
        getPoolBlockData()
            .subscribe(object : Jobserver<PoolBlock>(view) {
                override fun onSuccess(data: PoolBlock?) {
                    if (data != null) {
                        view?.setBlockData(data.lineChartData)
                        view?.setMiningAddress(data.miningAddress)
                        view?.setPoolPower(data.poolPower)
                        view?.setTotalBlock(data.totalBlock)
                        val zfx = BigDecimal(data.totalReward)
                        var totalReward =
                            "${zfx.divide(BigDecimal(10000), 4, RoundingMode.HALF_UP)}万"
                        view?.setTotalReward(totalReward)
                    }
                }
            })
    }

    fun getThorBlockHeightsListData() {
        view?.getPageNum()?.let {
            getThorBlockHeightsList(it)
                .subscribe(object : Jobserver<List<Block>>(view, false) {
                    override fun onSuccess(data: List<Block>?) {
                        if (data != null) {
                            view?.setBlock(data)
                        }
                    }
                })
        }
    }
}