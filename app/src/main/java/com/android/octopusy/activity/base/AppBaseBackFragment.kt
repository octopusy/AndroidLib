package com.whty.xzfpos.base

import android.content.Context
import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import com.axl.android.frameworkbase.ui.BaseBackFragment
import com.axl.android.frameworkbase.utils.PreferencesUtil

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2017/11/7
 * Time: 15:22
 */
open abstract class AppBaseBackFragment : BaseBackFragment() {
    protected val sp: PreferencesUtil by lazy {
        PreferencesUtil(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        ButterKnife.bind(this, this.view!!)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed(): Boolean = false
}