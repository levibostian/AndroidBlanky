# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/levibostian/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Keep models and VOs for your package.
-keep class com.levibostian.androidblanky.**.models.** { *; }
-keep class com.levibostian.androidblanky.**.vo.** { *; }
-keep class com.levibostian.androidblanky.service.error.** { *; }

# Crashlytics required
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
# make sure to NOT have the following line in this file at all to make sure Crashlytics can upload mapping file for you.
#-printmapping mapping.txt

# Retrofit
-dontnote retrofit2.Platform # Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontwarn retrofit2.Platform$Java8 # Platform used when running on Java 8 VMs. Will not be used at runtime.
-keepattributes Signature # Retain generic type information for use by reflection by converters and adapters.
-keepattributes Exceptions # Retain declared checked exceptions for use by a Proxy instance.
# Update: 5-22-18 I accountManager commenting these out to see if they are needed anymore. They do not exist on the website docs.
#-dontwarn okio.**
#-dontwarn retrofit2.Platform$Java8

# EventBus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
## Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# Picasso
-dontwarn com.squareup.okhttp.**

# Okhttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# for DexGuard only
# -keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# Random apache code
# -dontnote android.net.http.*
# -dontnote org.apache.commons.codec.**
# -dontnote org.apache.http.**

# Moshi
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *
## Needed for the codegen Kotlin adapter
-keep class **JsonAdapter {
    <init>(...);
    <fields>;
}
-keepnames @com.squareup.moshi.JsonClass class *