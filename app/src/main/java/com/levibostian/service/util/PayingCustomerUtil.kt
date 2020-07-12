package com.levibostian.service.util

import com.levibostian.service.manager.UserManager
import javax.inject.Inject

/**
Single source of truth to determine if the user is a paying customer or not. Because we may change how the app determines this in the future, we want to only change this in 1 place.
 */
class PayingCustomerUtil @Inject constructor(private val userManager: UserManager) {

    val isUserPaying: Boolean
        get() = userManager.isUserLoggedIn

}