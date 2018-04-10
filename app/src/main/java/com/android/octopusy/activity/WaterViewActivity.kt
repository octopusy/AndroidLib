package com.android.octopusy.activity

import android.os.Bundle
import android.view.View
import com.android.octopusy.R
import com.octopusy.uilib.custom_ui.model.Water
import com.whty.xzfpos.base.AppBaseActivity
import kotlinx.android.synthetic.main.activity_water_view.*

/**
 * @project：AndroidLib
 * @author：- octopusy on 2018/4/10 16:31
 * @email：zhangh@qtopay.cn
 */
class WaterViewActivity: AppBaseActivity() {

    var waterList: ArrayList<Water> = ArrayList<Water>()

    override fun getBundleExtras(extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContentViewLayoutID(): Int {
        return R.layout.activity_water_view
    }

    override fun initViewsAndEvents() {
        for (i in 0..9) {
            waterList.add(Water((i + Math.random() * 4).toInt(), "item" + i))
        }

        waterView.setWaters(waterList)

        btn_reset.setOnClickListener { _ ->
            waterView.setWaters(waterList)
        }
    }

    override fun getLoadingTargetView(): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}