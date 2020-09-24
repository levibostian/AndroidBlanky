package com.app.service.api

/**
 * This class is a hack to get Retrofit to work with Hilt dependency injection. When we provide a URL to our Retrofit instance, we usually represent a URL as a string. Strings do not work well for parameters of Hilt functions in modules. Especially because our app might be communicating with 2+ APIs. How we do represent 2+ different strings in Hilt dependency injection?  We create a new data type that stores a string inside. Read the document `docs/libs/HILT.md` in this project to learn more about Hilt to understand more about why this hack is needed.
 *
 * Subclass the [ApiHostname] and use those subclasses in a Hilt module. Create a new subclass for every API that you communicate with in your app.
 */
interface ApiHostname {
    val hostname: String
}

data class GitHubApiHostname(override val hostname: String) : ApiHostname
