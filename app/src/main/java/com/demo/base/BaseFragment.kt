package com.demo.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.demo.api.ApiResponseListener
import com.demo.util.Utility

/**
 * A [BaseFragment] class.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
abstract class BaseFragment<out T : ViewDataBinding, out V : ViewModel> : Fragment(), View.OnClickListener,
    ApiResponseListener {

    private lateinit var fragmentChildBinding: T
    protected var mActivity: BaseActivity<*>? = null
    private var mRootView: View? = null
    private var mLastClickTime: Long = 0
    private lateinit var mViewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
    }


    /**
     * Called to do initial creation of a fragment. This is called after onAttach and before onCreateView.
     *
     * @param inflater           the LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     *                           The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState Bundle: If the fragment is being re-created from a previous saved state, this is the state.
     *                           This value may be null
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            fragmentChildBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
            mRootView = fragmentChildBinding.root
            initFragment()
        }
        return mRootView
    }

    /**
     * This method is respectively associates the Fragment with/from the Activity
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is BaseActivity<*> -> {
                val activity = context as BaseActivity<*>?
                this.mActivity = activity
            }
        }
    }

    /**
     * It will call when fragment will attach
     */
    protected abstract fun initFragment()

    /**
     * @return layout genericModel id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int
    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): V


    /**
     * This method is detaches the Fragment with/from the Activity
     */
    override fun onDetach() {
        mActivity = null
//        listener = null
        super.onDetach()

    }

    /**
     * Get Binding Object
     */
    fun getFragmentDataBinding(): T = fragmentChildBinding

    /**
     * Click method of any view
     */
    override fun onClick(view: View) {
        if (mActivity != null) {
            Utility.hideKeyBoard(mActivity as Activity,view)
        }
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }

    /**
     * Override method of activity
     */
    override fun onResume() {
        super.onResume()
    }

    /**
     * @see ApiResponseListener interface
     */
    @Throws(Exception::class)
    override fun onApiResponse(response: Any, reqCode: Int) {
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


}