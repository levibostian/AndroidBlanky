# HTTP  

This document covers how you can perform HTTP requests in this project. We do so with an open source library called Retrofit. 

Retrofit is a popular open source library for performing network requests in Android. Anytime that you need to perform a HTTP request to a server to get some data, send data to the server, upload or download files, Retrofit makes this experience quick and easy. 

There are some alternatives to using Retrofit but I would say that Retrofit is by far the most popular choice in Android development. Retrofit was chosen for this project because:
* The community is large. If you have a problem, chances are you can find help on StackOverflow or GitHub. 
* The library has been around for many years and is maintained. It's stable and you can depend on it now and well into the future. 
* It works great with modern technologies such as Kotlin coroutines or RxJava. When new tech comes out for Android, Retrofit adopts this new tech. 

# Table of contents

If you are new to Retrofit, I recommend you read this document in full. However if you are only here to remind yourself how to use Retrofit specifically in this project, skip to the section on [How do I use it?](#how-do-i-use-it). 

* [How is it installed and configured?](#how-is-it-installed-and-configured) - Retrofit is installed and configured in this project already. This section of the doc will walk you through how Retrofit is installed and configured in this project. This section is meant for reference only to understand how Retrofit is setup in this project. It is also a good section to teach you the basics of Retrofit if you are not familiar with it. 
* [Use Retrofit with tests](#Use-Retrofit-with-tests) - How Retrofit is configured in this project to work with integration and UI tests. 
* [How do I use it in this project?](#how-do-i-use-it-in-this-project) - Learn how this project expects you use Retrofit. Even if you are familiar with Retrofit, read this section to learn the opinionated way that this project uses Retrofit for maintainability and testability of the code. 

# How is it installed and configured? 

* Install the Retrofit library with Gradle. 

At a minimum, you need to add the core dependency for Retrofit:

```
def retrofitVersion = '2.9.0'
implementation "com.squareup.retrofit2:retrofit:$retrofitVersion"
```

In this project, we also use the libraries [Moshi](MOSHI.md) and [RxJava](RXJAVA.md). Retrofit provides plugins for Moshi and RxJava to add functionality to Retrofit. Because of that, we install these plugins for Retrofit in Gradle, too:

```
implementation "com.squareup.retrofit2:converter-moshi:$retrofitVersion"
implementation "com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion"
```

> Note: To view a list of officially supported plugins, see the [Retrofit official site](https://square.github.io/retrofit/). You do not need to add more plugins into this project to work with Retrofit. 

* Add some entries for Proguard/R8. Retrofit includes an [official doc](https://github.com/square/retrofit/blob/master/retrofit/src/main/resources/META-INF/proguard/retrofit2.pro) with those entries to be added. Those rules have already been added to the file `app/proguard-rules.pro` in this project. 

* That's all for installing Retrofit. Now, let's get into how Retrofit is configured in this project. 

Here is an overview of the configuration of Retrofit in this project. 
1. Create an interface defining all endpoints of the API that you want to call. 
2. Create an instance of the `Retrofit` class. This is also where we configure Moshi and RxJava with Retrofit. 
3. Set hostname depending on build environment. Learn how this project is configured to provide the HTTP hostname to point to the network API you want to use. 

The sections below will go into each of these configurations. 

#### Create an interface defining all endpoints of the API that you want to call. 

To use Retrofit, we need to create an interface that defines all of the HTTP endpoints that we are calling. See the file `GitHubService.kt` in this project for an example of an interface we have made. 

### Create an instance of the `Retrofit` class. 

We need to create an instance of the `Retrofit` class. This is pretty simple. It's as easy as using something similar to this:

```kt
return Retrofit.Builder()
  .baseUrl("https://api.github.com")
  .build()
```

However, our setup is more advanced in this project. This project uses the tool [Hilt](HILT.md) for dependency injection. Because we use Hilt, we put the Retrofit initialization inside of a Hilt module so we can easily inject the Retrofit instance into other classes in our code. Open the file `DependencyModule.kt` to see the Hilt module we use to house Retrofit initialization. In this file, you will find code similar to this:

```kt
    @Provides
    fun provideOkHttp(@ApplicationContext context: Context, connectivityUtil: ConnectivityUtil, userManager: UserManager, logger: Logger): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggerInterceptor(logger))
            .addInterceptor(DefaultErrorHandlerInterceptor(context, connectivityUtil))
            .addNetworkInterceptor(AppendHeadersInterceptor(userManager))
            .build()

    @Provides
    fun provideGitHubService(client: OkHttpClient, hostname: GitHubApiHostname): GitHubService = Retrofit.Builder()
        .client(client)
        .baseUrl(hostname.hostname)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(MoshiConverterFactory.create(JsonAdapter.moshi))
        .build()
        .create(GitHubService::class.java)
```

> Tip: If you are confused about how these 2 functions work with it's parameters and annotations like `@Provides`, check out the [Hilt](HILT.md) document which explains Hilt modules. 

> Note: You might be wondering what `ConnectivityUtil`, `UserManager`, `ApiHostname`, or `Logger` is from the function above. These are all classes that we need in order to create the `Retrofit` class instance. You can open each of these files in the project to learn more about them. Some of the files include documentation at the top that explain what each file does. 

There are 2 functions here. The first function returns a `OkHttpClient` object and the second function returns a `GitHubService` instance. 

What is `OkHttpClient`? To explain that, I need to explain what OkHttp is. Retrofit under the hood uses another open source library called [OkHttp](https://github.com/square/okhttp). OkHttp is actually what performs HTTP requests. Retrofit is a library built *on top of OkHttp* to make HTTP requests even easier for Android. 

You may use Retrofit without ever interacting with OkHttp in your code. But if you ever want to add more advanced functionality to Retrofit, you need to work with OkHttp to do so. That is what we are doing here with the `OkHttpClient` object. We are creating an instance of `OkHttpClient` because we want to add [OkHttp interceptors](https://square.github.io/okhttp/interceptors/) to our network calls. OkHttp interceptors are classes that gets executed *on every network call* performed with a `Retrofit` instance. View the `AppendHeadersInterceptor.kt` file in the project for a simple example of an interceptor. The `AppendHeadersInterceptor` interceptor appends HTTP headers to every HTTP call we make. This is very handy because some network APIs out there require you send some HTTP headers in every HTTP request. Interceptors are much easier to use then having to copy and paste code to add these headers on every HTTP request we do with Retrofit!

The `Retrofit.Builder()` is how we create an instance of the `Retrofit` class. In our code above, we are adding the Moshi and RxJava plugin, our OkHttp client, and our interface that we created that contains all of our endpoints. With all of that done, we have told Retrofit exactly how to communicate with the HTTP network API we are working with. Retrofit does all of the rest of the work for you! 

### Set hostname depending on build environment 

With the code, `Retrofit.Builder().baseUrl(hostname.hostname).build()`, you might be wondering "What is `baseUrl(hostname.hostname)`"? `.baseUrl()` is how you provide the hostname to the API that you want the Retrofit instance to communicate with. An example of a hostname is `https://api.github.com`. It's a URL. 

You can easily pass in a string to this function `.baseUrl("https://api.github.com")` and that will work. However, what do you do when your team has a production API server (used by customers, only) and a staging/testing/QA API server (used for development and testing, only)? What if you are integrating your app with a 3rd party API like Stripe or Airtable and you want to use the sandbox environment (used for development and testing, only) instead of the production environment (used for customers, only)? 

This project is setup for you to dynamically change the hostname quick and easy. Here is how it works. 

First, you need to create a `ApiHostname` subclass. You need to create 1 subclass for every API that you communicate with in your app. For example, if you have an app that communicates to the GitHub API and the Stripe API then you need to create 2 `ApiHostname` subclasses. Open the file `ApiHostname.kt` in this project to see an example of a subclass. It's very simple:

```kt
data class StripeApiHostname(override val hostname: String): ApiHostname
```

> Note: To create a `ApiHostname` subclass, you can copy/paste the code above and simply replace `Stripe` with the name of the API you are communicating with. The use of `ApiHostname` subclasses is a hack for Hilt to differentiate hostnames from each other. 

Let's say that you are communicating with the Stripe API to charge a credit card in your app. Your `NetworkModule` will contain a line of code similar to this:

```kt
    @Provides
    fun provideStripeApiHostname(): StripeApiHostname = StripeApiHostname("https://api.stripe.com")
```

This code right now has a hard-coded string providing the hostname URL of the API we want to use. This will work for compiling our production app that our customers will use. But how do we provide an alternative hostname URL string at compile time to point to a hostname we can use for development? Here is how. 

* Open the `.env` file in the root directory of your project. Add an entry inside of it. 

```
STRIPE_API_HOSTNAME=https://api.stripe.com
```

Then, edit the code in `NetworkModule` to look like this:

```kt
    @Provides
    fun provideStripeApiHostname(): StripeApiHostname = StripeApiHostname(Env.stripeApiHostname)
```

Notice how we get `Env.stripeApiHostname` (camel case) from `STRIPE_API_HOSTNAME` (snake case). We remove all `_` characters, lowercase the first character (here, the `s`) and uppercase the first character after each of the `_`s. Use [this simple tool](https://www.better-converter.com/Case-Converters/Camel-Case-Converter) if you need help converting from snake to camel.

> Tip: Wonder how you're able to get `Env.stripeApiHostname` to work even though you didn't write the `Env` class? This file gets generated for you automatically with the project [dotenv-android](https://github.com/levibostian/dotenv-android). 

Real quick, open up the `TestNetworkModule` file. Inside, you should have an entry for your `StripeApiHostname` class that looks like this:

```kt
    @Provides
    @Singleton
    fun provideStripeApiHostname(mockWebServer: MockWebServer): StripeApiHostname = StripeApiHostname(mockWebServer.url)
```

All you need to edit in the code above is replace `StripeApiHostname` with the name of your `ApiHostname` subclass. Everything else should be the same as above. This file is explained in this doc's section about testing. 

Compile your app. If it compiles, everything should be setup correctly. Now, all you need to do is have a `.env` file you use for production builds of your app and a separate `.env` file that you use for your staging/testing/QA builds. Read the [Env](../ENV.md) document to learn more about how we use and manage `.env` files. 

# How do I use it in this project? 

Retrofit is installed and configured for you. This section will explain to you the opinionated steps to using Retrofit in your app. This will allow you to successfully perform HTTP requests in your app. 

* Create 1 interface file for each API that you use. For example purposes, let's say that we are calling the GitHub API (https://api.github.com). This means create a file `GitHubService.kt` that contains *all of the HTTP endpoints* that you call on this API:

```kt
interface GitHubService {

    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Single<Result<List<RepoVo>>>

    @GET("repos/{user}/{repo}/issues")
    fun getRepoIssues(@Path("user") username: String, @Path("repo") repoName: String): Single<Result<List<IssueVo>>>

    ...put all of the endpoint definitions that you use for the GitHub API in this file...
}
```

> Tip: To make things easy to find, put *all* endpoints for each API is contained in 1 file. 1 interface file for each API that you use in that app. 

> Tip: I use a free desktop software [Advanced Rest Client](https://advancedrestclient.com/) to learn how to interact with a network API. Advanced Rest Client allows you to quickly and easily perform HTTP requests from your computer to help you debug your code and better understand a network API you are working with. 

> Note: The file `GitHubService.kt` is already created for you. See that file for an example of an interface file for creating endpoints. 

In your interface file, you will have code that looks like this:
```kotlin
@GET("users/{user}/repos")
fun listRepos(@Path("user") user: String): Single<Result<List<RepoVo>>>
```
This defines 1 endpoint in the API. It's syntax helps to tell Retrofit how to call your API endpoint. Let's break this code down. 
`@GET` - This is the HTTP method to use. Use `@GET` for a HTTP GET request. `@POST` for a HTTP POST request. `@PUT` for PUT and `@DELETE` for DELETE. And so on...

`"users/{user}/repos"` - This is the URL endpoint path. `users` and `repos` in this example are static. `{user}` is dynamic and will be replaced at runtime. This allows you to perform a HTTP request to a path like `users/levibostian/repos` where `levibostian` replaces `{user}`. To do this, you need to use `@Path(...)` to do the replacement. `@Path()` is explained below. 

`fun listRepos` - You can name the function whatever you want. Try to make it descriptive to what the HTTP endpoint is doing for you.

`(@Path("user") user: String)` - The parameters of the function. The parameters allows you to add data to the HTTP call. Let's get into those. You have some options:
1. `@Path("user") username: String` - This will dynamically replace `{user}` in the URL endpoint path with the value `username: String` that you pass into the function. 
2. `@Query("user") username: String` - This will create a HTTP query parameter with the key `user` and value is the value of the `username` variable. Adding query parameters can be added to any HTTP request method but it's most common with GET requests. 
3. `@Body user: User` - This adds a Body to the HTTP request. Adding a Body can be added to any HTTP request method but it's most common with POST, PUT, and DELETE requests. `User` is the name of a Kotlin data class. See the document [Moshi](MOSHI.md) to learn about how to create data classes and a Moshi Adapter to be able to send a JSON string as the body of a request. 

`Single<Result<List<RepoVo>>>` - `Single<>` is specific to RxJava. See the [RxJava](RXJAVA.md) document to learn more about that. `Result<>` is a Retrofit class that contains a HTTP response. You always want to use this for all responses. `List<RepoVo>` This is the HTTP response data type that you predict to receive when the response gets back. Because we are using Moshi with Retrofit in this project, we expect the HTTP response that comes back to be a JSON string that we parse to be a List of `RepoVo` data classes. See the [Moshi](MOSHI.md) document to learn more about how to create `RepoVo`. 

* Open `ApiHostname.kt` and create a new `ApiHostname` subclass that will contain the URL string that points to the API server. 

```kt
data class GitHubApiHostname(override val hostname: String): ApiHostname
```

* Open `NetworkModule.kt` and add a new entry for your new ApiHostname subclass. We use the NetworkModule so that we can change the HTTP hostname URL for running tests. 

```kt
    @Provides
    fun provideGitHubHostname(): GitHubApiHostname = GitHubApiHostname("https://api.github.com")
```
> Tip: You don't need to directly pass a string value for the hostname. See the section [Call different API servers depending on our environment](#Call-different-API-servers-depending-on-our-environment) to learn some techniques for providing the URL string to your ApiHostname class. 

* Open `TestNetworkModule` and add an entry for your ApiHostname instance:
```kt
    @Provides
    @Singleton
    fun provideGitHubHostname(mockWebServer: MockWebServer): GitHubApiHostname = GitHubApiHostname(mockWebServer.url)
```
The only thing you need to change in the code above is the name of your class. Leave the rest the same. You should be using `mockWebServer.url` so that we can fake our HTTP requests in our tests. 

* Open `DependencyModule`. Add the following code to create an instance of your interface:
```kt
    @Provides
    fun provideGitHubService(client: OkHttpClient, hostname: GitHubApiHostname): GitHubService = Retrofit.Builder()
        .client(client)
        .baseUrl(hostname.hostname)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(MoshiConverterFactory.create(JsonAdapter.moshi))
        .build()
        .create(GitHubService::class.java)
```
> Note: You might be wondering, "Why are we using all of this boilerplate code in this function? Aren't the RxJava and Moshi plugin lines of code going to be the same for all APIs that I interact with?" We are using all of this boilerplate code because every API is different. What if you are sending HTTP requests to a server that uses JSON for requests and responses but another API you work with uses XML instead? Sure, some plugins in your code might be the same for all instances of `Retrofit` that you create but some may also be different. You can modify any of your Hilt modules however you like to prevent boilerplate code. 

* That is all of the Retrofit related setup for getting started with our API. Now is the time where we write more specific code to use Retrofit. 

* To encapsulate the use of Retrofit in our app's source code, we enforce a rule that you must create a new *API class* that contains all of the network code for your endpoint that you want to call. Check out `GitHubApi.kt` in this project for an example of what I mean by *an API class*. Create one function for each endpoint that you need to call in the API. The function that you create in the Api class is responsible for...

1. Using the Retrofit object to perform HTTP requests. This API class is the only class that is allowed to interact with Retrofit. Encapsulate all Retrofit interactions into this 1 file. 
2. Prepare the arguments to send to the API, if any. If you, for example, allow the user of your app to type into an `EditText` an email address, the API class should trim off whitespace from the entered email address before we give the email address to Retrofit. 
3. Handle HTTP response status codes. For example, if you get back a HTTP response with the status code of 404, handle that 404 in the API class function and return back a `NoResultsError` when the 404 occurs. This is just an example. This project does not have a file named `NoResultsError`. But you can view custom errors in `app/src/main/java/com/app/service/error/network/` for examples of how to create custom error classes. The `GitHubApi` file in this project has a good example function explaining handling status codes. 
4. Let's say that your app wants to update the username and profile picture for the user of your app. You could, if you wish, create 2 functions in your Api class. 1 function for calling the HTTP endpoint to update the username and a separate function to call the HTTP endpoint to upload the new profile picture. Or, you could create 1 function `updateProfile(name, profilePicture)` that performs 2 HTTP requests inside of it. This is up t you and how your app works. 

These API classes that we create all extend the base `API` class. This abstract class contains lots of handy logic in it that makes performing network calls even easier. It handles errors that can happen with using Retrofit and handles common HTTP errors like 500 status code responses. See the file `Api.kt` in this project to see this logic for yourself. 

> Note: It's important that we limit the number of files that call code in the Retrofit library. That's the biggest motivation for creating these `API` subclasses. Why do we care to limit the number of files calling Retrofit code? What if you decide in the future you want to stop using Retrofit and use something else? What if Retrofit has a major release in the future that forces you to make changes to your code that interacts with Retrofit? If we only have 1 or 2 files that interact with the Retrofit library, that would be a very quick and easy to change to another library or upgrade the Retrofit code. But if our app interacts with Retrofit in 15 files, that becomes much more difficult! Also, the more that we encapsulate 3rd party code (code we didn't write ourselves), the easier our code is to unit test. 

4. Now that you have created an API class, we can use the API class to perform HTTP requests. We have a rule where Repository classes are the classes that call the functions in the API class. See `ReposRepository.kt` file in this project for an example of a Repository class. Repository classes are where HTTP requests are being made. Remember that we don't want Retrofit classes to interact with Retrofit code. We want to perform all HTTP requests by calling a function in our API class that we made. 

5. After you crete a Repository class, we need to use our Repository class. There are a few places that we may want to use our Repository class. See the file `UpdateFcmTokenPendingTask.kt` for an example of a class that would call functions on our Repository. See the file `ReposViewModel.kt` for another example of when we would use a Repository class. 

ViewModel classes are special. We use ViewModel classes as the link between our UI code and our backend code. If you need to perform a HTTP request from an Activity or Fragment for example, you should not be calling a Repository function in your Activity or Fragment. You should instead call a function on a ViewModel which calls a function on a Repository for you. 

As a recap, here is how you perform HTTP requests from the UI of your app:

```
UI code (Fragment or Activity) ---> ViewModel ----> Repository ----> API class. 
```

As a recap, here is how you perform HTTP requests from non-UI code in our app:

```
Non-UI code ---> Repository ---> API class.
```
> Notice we do not use a ViewModel here. UI code should be the only code that works with ViewModels. 

#### To recap how to use Retrofit in our app...

* Define all endpoints for your API in an interface. 
* Create a subclass of `ApiHostname`. 
* Add the necessary dependency injection code in `DependencyModule`, `NetworkModule` and `TestNetworkModule`. 
* Create a subclass of the `Api` class and have all of your network logic inside of this class. 
* Create a Repository class that calls the functions of your `Api` subclass. 
* Create a ViewModel class that calls the functions of your Repository class. 

# Use Retrofit with tests

When we run automated tests (unit tests, integration tests, UI tests), we don't want to perform *real* HTTP requests when our tests run. Real HTTP requests are slow and unpredictable (bad for testing!). If we instead *fake* our HTTP requests by not calling a real API server, we can make tests that are super fast and predictable (good for testing!).

> Note: This documentation section is not for unit tests. For unit tests, we just mock classes. We don't do anything at all with Retrofit in our unit tests. We only use Retrofit in our integration and UI tests that run on an actual Android device. 

How do we fake HTTP calls? Let me explain. 

When the user of your app uses your app, this is how network requests work:

```
1. User's phone -------(send HTTP request)--------->>> GitHub's API server. 

2. User's phone <<<-------(receive HTTP response)--------- GitHub's API server. 
```

Users send requests, the GitHub API server sends back a response. The GitHub API server is the program that processes the request from you and determines the response back to you. We have no control over this processing. GitHub's server is what does the processing. What if we want to test our app's code works when a 404 HTTP status code gets returned? What if we need to test our code behaves correctly when a 500 error happens? What if we want to make sure out app's code handles the scenario of GitHub's API server being down? Well, if we call the real GitHub API server, we cannot easily test these scenarios. 

To make our test code predictable, we do not need to modify any of our app's code. We instead setup a *fake web server* that does not belong to GitHub! This allows *us to process the HTTP requests and return back a response that we decide*!

```
1. User's phone -------(send HTTP request)--------->>> An API server that we control. 

2. User's phone <<<-------(receive HTTP response)--------- An API server that we control. 
```

Because we operate the API server and not GitHub, we are the ones to process each request and we can send back whatever response we want.

You already learned in a previous section in this document that you can provide to Retrofit with a URL to a web server. We just need to provide Retrofit with a different URL that points to the API server we control instead of GitHub's API server at https://api.github.com. Good news is that you have this setup already. That's why you need to add code to `TestNetworkModule`. Cool. That part is done!

Well, how do we make an API server? Good news. OkHttp provides to us a cool tool that allows us to run a web server on our Android device. You heard that right. We can run a server on our Android device that our tests are going to execute on. No need to create a server or anything. Cool, huh?!

This project has installed and configured this OkHttp web server for you already. Here is a quick overview of how the mock web server got installed and configured. 

1. Install the library with Gradle:
```
androidTestImplementation "com.squareup.okhttp3:mockwebserver:$okhttpVersion"
testImplementation "com.squareup.okhttp3:mockwebserver:$okhttpVersion"
```

2. The file `MockWebServer` in the project is where the web server is managed. There are functions in this file to start and stop the server. Not only can we start and stop the server, but we also use this file to send back responses to HTTP requests made in our code while tests run. 

The mock web server is a **first in first out Queue** data structure. When writing a test, we need to ask ourselves, "What HTTP requests will be executed when this test code runs?", "What order will these HTTP requests run in?", "What do I want to return for each HTTP request?". We add responses to the mock web server queue in our test function. Then, **when the mock web server runs it will return the response in a first in first our order**. 

Here is an example test function from `MainFragmentTest`:
```kt
    @Test
    fun programsList_givenPrograms_expectShowsInList() {
        val cache = listOf(
            RepoModel(1, "repo-1", RepoOwnerModel("levi"))
        )

        mockWebServer.queue(200, cache.toTypedArray())

        launchFragment()

        onView(withId(usernameEditTextId))
            .perform(typeText("levi"))
        onView(withId(goButtonId))
            .perform(click())

        onView(withId(loadingViewId))
            .check(matches(not(isDisplayed())))
        onView(withId(reposRecyclerViewId))
            .check(matches(isDisplayed()))

        assertListItems(reposRecyclerViewId, cache) { index, item ->
            matches(hasDescendant(withText(item.name)))
        }
    }
```

We add a response to the mock web server queue with the code: `mockWebServer.queue(200, cache.toTypedArray())`. We are telling the web server, "Return a HTTP response code of 200 with a response of a list of `RepoModel` instances". Because we are using the Moshi plugin in this project, the mock web server will take the `cache` variable value, convert that to a JSON string, and return the JSON string as the response to the HTTP request. 

> Note: It's very important to use `.toTypedArray()`. If you use something like `.toList()` you will have Retrofit and Moshi errors in your tests. 

The mock web server is not super intelligent. You cannot tell the mock web server, "Send X response when you get the request, Y". Instead, it's just a queue data structure. Your test function adds to the queue. Then, when your app's code performs a HTTP request, the queue will remove the next item from the queue and return that as the response. This might sound like a pain, but really, it's not that hard to work with. If you ever find yourself having a difficult time managing more then 2-3 items in the network queue in your test function, that's usually a sign your test function needs to be broken up into multiple test functions. Make your tests smaller. With the many apps that I have built using this mock web server, I have yet to encounter a time when I needed to add more then 2 items to the mock web server queue. Keep your tests small to have a better testing experience. 

> Tip: See `MainFragmentTest.kt` for some examples of how to use mock web server. 

3. `TestSetupUtil` is a convenient file in our project that executes all of the code to setup our tests and tear them down. It is inside of this class that we start the mock web server. All you need to do in your integration or UI test is to call `setup()` to start the mock web server:

```kt
class MainFragmentTest : FragmentEspressoTest<MainFragment>() {

    @Inject lateinit var mockWebServer: MockWebServer
    @Inject lateinit var testSetup: TestSetupUtil

    @Before
    override fun setup() {
        super.setup()

        diRule.inject()
        testSetup.setup()
    }
    ...
}
```

> Tip: See `MainFragmentTest.kt` to see a full example of an instrumentation test that uses `TestSetupUtil`. 


