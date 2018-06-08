package com.levibostian.androidblanky.service.auth

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.content.Context
import android.os.Bundle
import android.accounts.AccountManager
import android.content.Intent
import android.text.TextUtils
import com.levibostian.androidblanky.BuildConfig
import com.levibostian.androidblanky.view.ui.activity.auth.AuthenticatorActivity

class AccountAuthenticator(private val context: Context): AbstractAccountAuthenticator(context) {

    companion object {
        // The account type designated for your app.
        // If this value below is edited, make sure to also edit the @xml/authenticator.xml `accountType` value to match this.
        // If you want to build and launch a suite of apps, it's recommended to have 1 single application type value that is shared between all your apps so your users can share 1 account between all of your apps, like Google apps.
        //
        // TODO edit the account type below.
        // It is recommended to have it be the domain name of your company instead of having it be your whole package name because then if you were to create another Android app, share the same account type name, then you would be able to share the account between all of the apps.
        const val ACCOUNT_TYPE = "com.levibostian.androidblanky"
    }

    override fun addAccount(response: AccountAuthenticatorResponse?, accountType: String?, authTokenType: String?, requiredFeatures: Array<out String>?, options: Bundle?): Bundle {
        // The Intent inside of the returned Bundle here is what is passed to your AuthenticatorActivity so if there is data you need, send it.
        //
        // Note: This app is built to have 1 user logged in at one time. So, when addAccount is called, it will simply launch the AuthenticatorActivity where the activity will ask the user to logout of the existing account, if they are logged in already, or to simply login. If this function here is ever edited to allowing add multiple accounts, we will need to edit some of the LaunchActivity code too (there is a comment in there where).
        return getAuthenticatorActivityIntentBundle(response)
    }

    override fun getAuthToken(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle {
        val accountManager = AccountManager.get(context)
        val authToken = accountManager.peekAuthToken(account, authTokenType)

        if (authToken?.isNotBlank() == true) {
            val result = Bundle()
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account!!.name)
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken)
            return result
        }

        return getAuthenticatorActivityIntentBundle(response)
    }

    private fun getAuthenticatorActivityIntentBundle(response: AccountAuthenticatorResponse?): Bundle {
        val intent = AuthenticatorActivity.getIntent(context, null, false).apply {
            response?.let { putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, it) }
        }

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)

        return bundle
    }

    override fun getAccountRemovalAllowed(response: AccountAuthenticatorResponse?, account: Account?): Bundle {
        val result = Bundle()
        val allowed = true
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, allowed)
        return result
    }

    override fun confirmCredentials(response: AccountAuthenticatorResponse?, account: Account?, options: Bundle?): Bundle? = null

    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle? = null

    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?): Bundle {
        // This call is used to query whether the Authenticator supports specific features. We don't expect to get called, so we always return false (no) for any queries.
        val result = Bundle()
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false)
        return result
    }

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle? = null

    // null means we don't support multiple authToken types
    override fun getAuthTokenLabel(authTokenType: String?): String? = null

}