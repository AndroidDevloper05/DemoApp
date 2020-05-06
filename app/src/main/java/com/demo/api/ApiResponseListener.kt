package com.demo.api


/**
 * Interface for api response
 */
interface ApiResponseListener {

    /**
     * Get Response from Api
     *
     * @param response Response will depend on Request. It will return object and we can type cast this object according to Response
     * @param reqCode  which request is call. Multiple request may be call on screen so it will differentiate by request code
     * @throws Exception If any exception will acquire it will goes to RestClient class will show error Screen
     */
    @Throws(Exception::class)
    fun onApiResponse(response: Any, reqCode: Int)

    /**
     * This method will call when any error will acquire when api will call. mostly it handle in RestClient Class, But in some condition we can handle it
     * According to particular screen. it will come in this method if any error will acquire
     *
     * @param response will mostly return any String message
     * @param reqCode  which request is call. Multiple request may be call on screen so it will differentiate by request code
     * @throws Exception If any exception will acquire it will goes to RestClient class will show error Screen
     */
    @Throws(Exception::class)
    fun onApiError(response: Any, reqCode: Int)

    /**
     * This method will call when Network will not be available, mostly it handle in RestClient Class, But in some condition we can handle it
     * According to particular screen. it will come in this method if  network will not available
     *
     * @param response will mostly return any String message of network
     * @param reqCode  which request is call. Multiple request may be call on screen so it will differentiate by request code
     * @throws Exception If any exception will acquire it will goes to RestClient class will show error Screen
     */
    @Throws(Exception::class)
    fun onApiNetwork(response: Any, reqCode: Int)

}