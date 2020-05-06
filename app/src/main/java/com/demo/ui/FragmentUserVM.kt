package com.demo.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.demo.DemoApplication
import com.demo.api.ApiResponseListener
import com.demo.api.RestClient
import com.demo.base.BaseViewModel
import com.demo.database.AppDao
import com.demo.database.AppDatabase
import com.demo.model.UsersResponse
import com.demo.util.Constant
import retrofit2.Call

open class FragmentUserVM: BaseViewModel() {
    val userList = MutableLiveData<MutableList<UsersResponse>>()
    val error = MutableLiveData<String>()

    private var appDoa: AppDao = AppDatabase.getInstance(DemoApplication.instance)!!.appDoa()!!
    var currentPage: Int = Constant.PAGE_START
    var totalPage:Int= currentPage
    var isLastPage = false
    var isLoading = false

    /**
     * Call Api to get details
     */
    fun callAPi() {
        val callApi: Call<MutableList<UsersResponse>> = RestClient.getApiClient()!!.getResponse(Constant.PAGE_SIZE,currentPage)
        RestClient.makeApiRequest(callApi, Constant.API_REQUEST_CODE, object :ApiResponseListener{
            override fun onApiResponse(response: Any, reqCode: Int) {
                val userResponse = response as MutableList<UsersResponse>
                userList.value=userResponse
                totalPage++
            }

            override fun onApiError(response: Any, reqCode: Int) {
                error.value=response as String
            }

            override fun onApiNetwork(response: Any, reqCode: Int) {
                error.value=response as String
            }

        })
    }

    fun checkLocalStorage() {
        if (currentPage == Constant.PAGE_START)
            appDoa.deleteAllUsers()
        appDoa.insertAllUser(userList.value!!)
    }

    fun loadAllUsers(){
        userList.postValue(appDoa.loadAllUsers())
    }
}