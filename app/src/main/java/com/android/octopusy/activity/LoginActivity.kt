package com.android.octopusy.activity

import android.os.Bundle
import android.view.View
import com.android.octopusy.R
import com.whty.xzfpos.base.AppBaseActivity
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 登录页面
 */
class LoginActivity : AppBaseActivity() {

    override fun getBundleExtras(extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContentViewLayoutID(): Int {
        return R.layout.activity_login
    }

    override fun initViewsAndEvents() {
        btn_customView.setOnClickListener { _ ->
            openActivity(WaterViewActivity::class.java)
        }
    }

    override fun getLoadingTargetView(): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
