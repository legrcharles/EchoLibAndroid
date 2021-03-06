# ddf-echo-android

### Mock easily your Android application

## add echo to your app

[ ![Download](https://api.bintray.com/packages/d2factory/Echo/com.d2factory.echo/images/download.svg?version=1.0.1) ](https://bintray.com/d2factory/Echo/com.d2factory.echo/1.0.1/link)

Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

then in your build.gradle (app) in section *dependencies* add this line 
```
implementation 'com.d2factory.echo:echo:1.0.1'
```

**use Gradle Sync**


## How to Use ?

Config the lib
==============
```
val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
okHttpBuilder.setupMock(applicationContext,
       "bundleName",
       EchoConfigType.RECORDING);
```

- param1 : the application context
- param2 : (default = "") the bundle name => record/read in separate mock folder (for example record json files for user1 in separate bundle named user1 and then user2 in bundle user2, or each Retrofit client can have it's separate bundle)
- param3 : (default = NONE) configType => RECORDING when you want to save json files
                                          MOCK_RESPONSE when you want to use json files

Mocks files nomenclature
========================

Example :

network call GET https://mydomain.com/_ah/api/user/v1.0/getUser

will create file named :

GET-_ah-api-user-v1.0-getUser.json

Record Json file
==================

When setup echo lib with parameter recordEnable to **true** then every network calls in Json file to your internal app files folder :

![internal_app_files_folder](.repo/internal_app_files_folder.png)


Mock WS with json files
=======================

When setup echo lib with parameter useMockEnable to **true** then every network calls try to find the associated file (related by name) in the assets folder (and bundle folder if setup)

![assets_folder](.repo/assets_folder.png)

If not found it try to find the file in Internal app files folder

If not found, it execute the orginal network call.