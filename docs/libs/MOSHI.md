# Moshi

Moshi is an Android open source library that converts JSON into Java/Kotlin objects and vice-versa. Why? JSON strings by themselves are not very useful. They are just a string. But if you can convert a big JSON string into a series of Objects in code, you have a format that is easy for your code to use. 

Today, it's very common for web and mobile apps to communicate over the Internet with servers to display data in the app. Today, it's also very common for client apps and servers to communicate together with the JSON data format. 

# Table of contents

* [What is JSON and why is it used?](#What-is-JSON-and-why-is-it-used) - If you don't know what JSON is, read this. 
* [How is it installed and configured?](#how-is-it-installed-and-configured) - Moshi is installed and configured in this project already. This section of the doc will walk you through how Moshi is installed and configured in this project. This section is meant for reference only to understand how Moshi is setup in this project. It is also a good section to teach you the basics of Moshi if you are not familiar with it.
* [How do I use it?](#how-do-i-use-it) - Learn how this project expects you use Moshi. Even if you are familiar with Moshi, read this section to learn the opinionated way that this project uses Moshi for maintainability and testability of the code. 

### What is JSON and why is it used?

Humans can communicate in many ways. You can write a letter or speak. You can use perfect grammar or have typos in your sntence and th persoon readng can still undrstand whta is being said. Computers are different. In order for them to communicate together, there needs to be a common format the data is arranged in so the computer can understand it the communication. 

For example. If I was to ask you to introduce yourself to me, you may respond to me with something like this:

"Hello, my name is Dana (she/her/hers). I work as a mail person to deliver packages. I enjoy scary movies and cheese pizza."

This is a great response for a human to receive but a computer is going to have a harder time understand this response as it's a sentence in the English language. Let's take the sentence above and convert this into a format that a computer has an easier time understanding:

```
{
    introduction: {
        name: "Dana",
        pronouns: "she/her/hers",
        job: {
            name: "mail person",
            description: "deliver packages"
        },
        enjoy: ["scary movies", "cheese pizza"]
    }
}
```

This is a JSON string that is saying the same thing as the sentence above does. It's just a regular string except it's written in a very specific format. Why is this format important and significant? It's because we can convert this JSON string into something that code can understand. Code can easily understand Objects!

```kotlin
data class Introduction(
    val name: String,
    val pronouns: String,
    val job: Job,
    val enjoy: List<String>
)

data class Job(
    val name: String,
    val description: String
)

// Let's say that we have an Object that represents Dana's introduction. 
val danaIntroduction: Introduction = ...
// If you want her job name, that's easy now!
danaIntroduction.job.name
// This is way easier to work with then a String
```

JSON strings are in a format that is designed to be converted to and from Objects really easily. What is neat is that converting JSON strings to code Objects is done in pretty much every programming language out there. Kotlin, Swift, Javascript/Typescript, Java, C, Python, Rust, Go, etc. In fact, JSON functionality is built into some programming languages without the need for a library or plugin. It's a common task to do. 

This is the reason JSON is a common format for clients and servers to communicate. Computers understand it and it's not specific to 1 specific programming language. 

# How is it installed and configured?

* Install the Moshi library with Gradle. 

```kotlin
def moshiVersion = '1.9.3'
implementation "com.squareup.moshi:moshi:$moshiVersion"
implementation "com.squareup.moshi:moshi-adapters:$moshiVersion"
```

When you use Moshi with Kotlin (like we are doing in this project), you have a choice on how you want to use Moshi. You can use [*reflection*](https://github.com/square/moshi#reflection) or you can use [*code generation*](https://github.com/square/moshi#codegen). Reflection requires less work on your part as a developer but it makes your Android APK bigger because the reflection library is large (2.5mb). Code generation requires more work on your part but it makes your APK smaller. In this project, we use code generation.

That means that we need to install the code generation plugins for Moshi:

```kotlin
kapt "com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion"
```

* In this project, we use Retrofit to preform HTTP requests. It's common for HTTP responses to be a JSON string. If you install the Moshi library and then install the Retrofit Moshi plugin into your project, then Retrofit will use Moshi to automatically parse the HTTP response JSON string into a Kotlin object. No work to do on your part! See the [Retrofit doc](RETROFIT.md) in this project to see how we use Retrofit to create Kotlin Objects from HTTP responses. 

* That's all you need to do for installing Moshi. Now it's up to you to use it. See the section [How do I use it?](#how-do-i-use-it) to learn how we use Moshi in this project. 

# How do I use it?

To successfully use Moshi, there are some requirements that you must follow. 
1. Create classes for your JSON string to map to. 
2. Create an Adapter for each data type to tell Moshi how to parse the JSON string. 
3. Use Moshi to convert a JSON string to Kotlin Objects and vice-versa. 

### Create classes for your JSON string to map to.

Let's say that you have a JSON string:

```json
{
    introduction: {
        name: "Dana",
        pronouns: "she/her/hers",
        job: {
            name: "mail person",
            description: "deliver packages"
        },
        enjoy: ["scary movies", "cheese pizza"]
    }
}
```

If you want to parse this JSON string into Kotlin objects, you need to create the Kotlin class that you want the JSON to be converted to. Here are classes that represent the JSON string above:

```kotlin
data class GetIntroductionResponse(
    val introduction: Introduction
)

data class Introduction(
    val name: String,
    val pronouns: String,
    val job: Job,
    val enjoy: List<String>
)

data class Job(
    val name: String,
    val description: String
)
```

With the code above, here are some notes about it:
* We use `data class` instead of `class`. Kotlin [data classes](https://kotlinlang.org/docs/reference/data-classes.html) are handy for JSON parsing because Kotlin data classes automatically add `equals()` and `toString()` functions that you would need to manually create in Java. Data classes are great when you need to have a class that stores data like this. 
* Compare the JSON string and the data classes that we created. Do you understand how we came up with these data classes? Always start at the root of the JSON:
```json
{
    ...
}
```

This means that our JSON string is 1 Kotlin object. If the root of our JSON string was:
```json
[
    ...
]
```

That is a JSON array. That would mean that we would have a `List<>` of Kotlin objects to parse into. 

Ok. We have 1 Kotlin object. Let's move into the next step of our JSON string:
```json
{
    introduction: {
        ...
    }
}
```

This is a JSON object (`{}` means object). This means we need to create a Kotlin `data class` to represent this code. That's why we created `GetIntroductionResponse`. `GetIntroductionResponse` is the object with 1 property inside of it: `introduction`.

Let's move another step inside of the JSON. The contents of the `introduction` object:

```
name: "Dana",
pronouns: "she/her/hers",
job: {
    name: "mail person",
    description: "deliver packages"
},
enjoy: ["scary movies", "cheese pizza"]
```

We can use built-in data types like String, Int, Double, List<>, Bool. Moshi knows how to parse these automatically. No extra work on our part. 

That's the basics of it. Let's go over a couple of more advanced scenarios:

##### Optional values in JSON string

What if our introduction object didn't need to have a `job` for someone who does not have a job:
```
{
    name: "Dana",
    pronouns: "she/her/hers",
    enjoy: ["scary movies", "cheese pizza"] 
}
```

What if someone could have multiple jobs:
```
name: "Dana",
pronouns: "she/her/hers",
jobs: [
    {
        name: "mail person",
        description: "deliver packages"
    }, {
        name: "freelance artist",
        description: "Write blog posts for solar companies"
    }
],
enjoy: ["scary movies", "cheese pizza"]
```

How do we modify our `Introduction` class to handle these scenarios? 

Our modified `Introduction` class would be:

```kotlin
data class Introduction(
    val name: String,
    val pronouns: String,
    val job: Job? = null,
    val jobs: List<Job>? = null,
    val enjoy: List<String>
)
```

Yes. Moshi understands Kotlin optionals. Note that I included `= null` as default values for `job` and `jobs`. Moshi will throw an exception if it attempts to parse a JSON string into a data class and Moshi does not see a property in the JSON string. That means if you tried to parse this JSON string:

```
{
    name: "Dana",
    pronouns: "she/her/hers",
    job: {
        name: "mail person",
        description: "deliver packages"
    },
    enjoy: ["scary movies", "cheese pizza"]
}
``` 

into the Kotlin data class:

```kotlin
data class Introduction(
    val name: String,
    val pronouns: String,
    val job: Job?,
    val jobs: List<Job>?,
    val enjoy: List<String>
)
```

Moshi would throw an exception because the JSON string does not have a property `jobs`. But if we give a default value of `= null` for the `jobs` property, then Moshi will not throw an exception and will populate `jobs` with the `null` value. 

### Create an Adapter for each data type to tell Moshi how to parse the JSON string. 

We have created some Kotlin data classes to parse our JSON strings into. But we have a problem. Moshi does not know how to parse a JSON string into this custom data type. Moshi by default know how to parse Strings, Bool, Int, List<> and other data types built into the language but your `Introduction` data class for example is a custom data type. Moshi doesn't know how to work with that. To help Moshi parse custom data types, we use Moshi Adapters. 

##### Moshi Adapters 

To use Moshi Adapters, we need to create one. Adapters are what tells Moshi how to parse 1 data type to and from a JSON string. You need to create 1 Adapter for every data type you are working with. Yes, that means 1 Adapter for every data class that you create. That also means that if you want to use a data class like Java's `Date` or `Uri` class built into the programming language, you need to have 1 Adapter for each of these data types, too. 

For data classes that you create, creating an Adapter is super easy. Moshi helps us with that. All you need to do is add this to the top of all of our data classes that we make:

```kotlin
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Introduction(
    ...
)
```

When you add `@JsonClass(generateAdapter = true)`, that will automatically create the Adapter for you to compile time. No more work to do on your part! 

> Note: You need to add `@JsonClass(generateAdapter = true)` to all of your data classes. Even nested data classes like `Job` which is nested inside of `Introduction`. 

But let's say that we want to use a data type like Java's `Uri` class to represent a HTTP URL? We need to create these adapters manually. See the file `UriJsonAdapter` for an example of a custom adapter. These are probably rare when you need to create these. If you use an `enum` as a property data type, that would be an example of when you would need to create a custom Adapter. 

Now that we have either (1) used `@JsonClass(...)` or (2) created an Adapter manually, we need to provide it to Moshi so it knows about it. We provide this in the Moshi Builder that creates a Moshi instance. You find the Moshi Builder in the `JsonAdapter` class in this project. 

```kotlin
Moshi.Builder()
    .add(Date::class.java, Rfc3339DateJsonAdapter()) // This is an adapter that deals with `Date`. Date is a common data type so Moshi gave this to us. 
    .add(UriJsonAdapter())
    .build()
```

### Use Moshi to convert a JSON string to Kotlin Objects and vice-versa. 

We have created Adapters. Lastly, we need to make Moshi do the parsing for us. 

If you want to use Moshi with Retrofit so that Moshi can parse a HTTP JSON string response for you, your work here is done. Read the [Retrofit](RETROFIT.md) document to learn about how to do that. If you are *not* making a HTTP request with Retrofit and you need to manually parse JSON strings, this project has made a handy class `JsonAdapter` for you to use. 

All you need to do in your code is call the static function that you need. 

To convert from a JSON string to Kotlin object:
```kotlin
val jsonString: String = ...
val introduction: Introduction = JsonAdapter.fromJson(jsonString)
// If you a JSON list, use this function instead:
val introductions: List<Introduction> = JsonAdapter.fromJsonList(jsonString)
```

To convert from Kotlin object to a JSON string:
```kotlin
val introduction: Introduction = ...
val jsonString: String = JsonAdapter.toJson(introduction)
// the syntax is the same to make a JSON array string
val introductions: List<Introduction> = ...
val jsonString: String = JsonAdapter.toJson(introductions)
```

Moshi will throw an exception if you forgot to create an Adapter or if your Adapter is not made correctly. That's why I highly recommend you take advantage of unit and integration tests in this project so we can assure that Moshi can parse our JSON strings for us. 

