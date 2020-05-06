package com.demo.ui

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.R
import com.demo.base.BaseFragment
import com.demo.databinding.FragmentUsersBinding
import com.demo.model.UsersResponse
import com.demo.ui.adapter.UsersAdapter
import com.demo.util.Constant.PAGE_START
import com.demo.util.PaginationListener
import com.demo.util.Utility
import java.util.concurrent.Executors


class FragmentUsers : BaseFragment<FragmentUsersBinding, FragmentUserVM>() {
    private var usersAdapter:UsersAdapter?=null
    lateinit var fragmentUserVM: FragmentUserVM
    private lateinit var binding: FragmentUsersBinding

    /**
     * int the fragment
     */
    override fun initFragment() {
        binding=getFragmentDataBinding()
        initComponent()
        (activity as ActivityMain).prgVisibility.postValue(true)

    }
    override fun getViewModel(): FragmentUserVM {
        fragmentUserVM =
            ViewModelProvider(this@FragmentUsers).get(FragmentUserVM::class.java)
        return fragmentUserVM
    }


    private fun initComponent() {
        setAdapter(arrayListOf())
        callAPi()
        handleClick()
        observeData()

    }

    private fun observeData() {
        fragmentUserVM.userList.observe(this@FragmentUsers,Observer {
            if(Utility.hasInternet(mActivity!!))
                setData(it)
            else
                setFromLocal(it)
        })
        fragmentUserVM.error.observe(this@FragmentUsers,Observer {
            if (Utility.hasInternet(mActivity!!))
                setMsg(it)
            else  Executors.newSingleThreadExecutor().execute {
                fragmentUserVM.loadAllUsers()
            }
        })
    }

    private fun handleClick()
    {
        val layoutManager=LinearLayoutManager(activity!!)
        binding.rvUsers.layoutManager =layoutManager
        binding.rvUsers.addOnScrollListener(object :PaginationListener(layoutManager){
            override fun loadMoreItems() {
                fragmentUserVM.isLoading = true
                fragmentUserVM.currentPage++
                callAPi()
            }
            override val isLastPage: Boolean
                get() =  fragmentUserVM.isLastPage
            override val isLoading: Boolean
                get() = fragmentUserVM.isLoading


        })
        binding.txtRetry.setOnClickListener {
            (activity as ActivityMain).prgVisibility.postValue(true)
            setInitialValue()
            callAPi()
        }

    }
    private fun setInitialValue() {
        fragmentUserVM.currentPage = PAGE_START
        fragmentUserVM.isLastPage = false
        usersAdapter?.clear()
    }

    private fun setAdapter(list: MutableList<UsersResponse>) {
        usersAdapter= UsersAdapter(list)
        binding.rvUsers.adapter=usersAdapter
    }

    /**
     * Call Api to get details
     */
    private fun callAPi() {
        fragmentUserVM.callAPi()
    }
    /**
     * Set the data
     */
    private fun setData(userResponse: MutableList<UsersResponse>) {
        Executors.newSingleThreadExecutor().execute {
            fragmentUserVM.checkLocalStorage()
            mActivity?.runOnUiThread {
                if(!userResponse.isNullOrEmpty()) {
                    if (fragmentUserVM.currentPage != PAGE_START)
                        usersAdapter?.removeLoading()
                    else{
                        binding.rvUsers.visibility = VISIBLE
                        binding.liError.visibility = GONE
                    }
                    usersAdapter?.addItems(userResponse)
                    // check weather is last page or not
                    if (fragmentUserVM.currentPage < fragmentUserVM.totalPage) {
                        usersAdapter?.addLoading()
                    } else {
                        fragmentUserVM.isLastPage = true
                    }
                    fragmentUserVM.isLoading = false
                    mActivity?.prgVisibility?.postValue(false)
                }else
                {
                    usersAdapter?.removeLoading()
                }
            }
        }
    }

    /**
     * Set header title
     */
    override fun onResume() {
        super.onResume()
        mActivity?.screenTitle?.postValue(getString(R.string.title_user))
    }

    /**
     * Set layout
     */
    override fun getLayoutId(): Int = R.layout.fragment_users


    private fun setMsg(msg: String) {
        binding.liError.visibility= VISIBLE
        binding.txtError.text=msg
        binding.rvUsers.visibility = GONE
        mActivity?.prgVisibility?.postValue(false)
    }
    private fun setFromLocal(userResponse: MutableList<UsersResponse>) {
        activity!!.runOnUiThread {
            mActivity?.prgVisibility?.postValue(false)
            if (fragmentUserVM.userList.value.isNullOrEmpty()) {
                setMsg(getString(R.string.net_message))
            } else {
                binding.rvUsers.visibility = VISIBLE
                binding.liError.visibility = GONE
                usersAdapter?.clear()
                usersAdapter?.addItems(userResponse)
                usersAdapter?.removeLoading()
            }
        }
    }
}
