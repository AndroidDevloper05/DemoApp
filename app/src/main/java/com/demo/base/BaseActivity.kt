package com.demo.base

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.demo.R
import com.demo.api.ApiResponseListener
import com.demo.api.RestClient
import com.demo.databinding.ActBaseBinding
import com.demo.util.Utility

/**
 * Base class for all screens
 */
abstract class BaseActivity<out T : ViewDataBinding> : AppCompatActivity(), View.OnClickListener,ApiResponseListener{
    private lateinit var childBinding: T
    private lateinit var actBaseBinding: ActBaseBinding
    // variable to track event time
    private var mLastClickTime: Long = 0
    val screenTitle=MutableLiveData<String>()
    val prgVisibility=MutableLiveData<Boolean>()

    /**
     * Init the layout
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actBaseBinding = DataBindingUtil.setContentView(this, R.layout.act_base)!!
        performDataBinding()
        initMethod()
        setHeaderTitle()
        progressVisibility()
    }

    /**
     * abstract method should override in child class
     */
    protected abstract fun initMethod()

    private fun performDataBinding() {
        childBinding = DataBindingUtil.inflate(LayoutInflater.from(this), getLayoutId(), actBaseBinding.frmBaseContainer, false)
        actBaseBinding.frmBaseContainer.addView(childBinding.root)
    }

    /**
     * Get Binding Object
     */

    fun getViewDataBinding(): T = childBinding

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Override the click event to hide the keyboard when event click
     */
    override fun onClick(view: View) {
        Utility.hideKeyBoard(this, view)
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }

    /**
     * @see ApiResponseListener interface
     */
    @Throws(Exception::class)
    override fun onApiResponse(response:Any, reqCode: Int) {
        //Implemented in child class
    }

    /**
     * @see ApiResponseListener interface
     */
    @Throws(Exception::class)
    override fun onApiError(response: Any, reqCode: Int) {

    }

    /**
     * @see ApiResponseListener interface
     */
    @Throws(Exception::class)
    override fun onApiNetwork(response: Any, reqCode: Int) {
    }

    /**
     * Set Screen Title
     */
    private  fun setHeaderTitle()
    {
        screenTitle.observe(this, Observer<String> {
            actBaseBinding.headerTitle=it
        })

    }
    /**
     * Set ProgressVisibility
     */
    private fun progressVisibility()
    {
        prgVisibility.observe(this, Observer<Boolean> {
            actBaseBinding.prgVisibility=it
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        RestClient.cancelApiCall()
    }
}