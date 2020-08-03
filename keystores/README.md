Put keystore files in here.

If you have not created a keystore file yet, perform the following steps ([official docs](https://developer.android.com/studio/publish/app-signing#generate-key))
* In Studio, go to Build > Generate Signed Bundle/APK. APK, next > Create new. 
* In this window, you need to fill in all of the information it asks you to such as keystore location, passwords, and org information. 
  * *Note: You cannot use special characters in the org information section.*
  * Use 1000 for the Validity because why not. 
* Hit ok. Studio then has generated your keystore file for you. You can hit cancel and not actually continue with building the app. 

Then, copy that upload key into `_secrets/_common/keystores/upload.keystore` for cici to encrypt. Make sure to also set the environment variables on your CI machine for this key. 

*Note: We used to use the `keytool` CLI tool to generate the key but had exceptions when trying to build the app with the generated key. Using Studio to generate the key for you is more up-to-date and easy to use.*