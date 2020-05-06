package com.demo.ui

import com.demo.R
import com.demo.base.BaseActivity
import com.demo.databinding.ActMainBinding

/**
 * Set the fragment by this activity
 */
class ActivityMain : BaseActivity<ActMainBinding>() {
    private lateinit var binding: ActMainBinding

    override fun initMethod() {
        binding = getViewDataBinding()
    }

    override fun getLayoutId(): Int {
        return R.layout.act_main
    }
}