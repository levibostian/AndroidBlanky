package com.levibostian.viewmodel

import androidx.lifecycle.ViewModel
import com.levibostian.service.repository.UserRepository
import com.levibostian.service.vo.response.MessageResponseVo
import com.levibostian.testing.OpenForTesting
import com.levibostian.teller.repository.OnlineRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@OpenForTesting
class UserViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    fun loginPasswordlessEmail(email: String): Single<OnlineRepository.FetchResponse<MessageResponseVo>> {
        return userRepository.loginPasswordlessEmail(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}