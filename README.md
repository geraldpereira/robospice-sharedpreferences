robospice-sharedpreferences
============================

SharedPreferences based cache for the [RoboSpice API](https://github.com/octo-online/robospice)


[SharedPreferencesObjectPersister](https://github.com/Kojir0/robospice-sharedpereferences/blob/master/src/main/java/com/byob/android/robospice/persistance/SharedPreferencesObjectPersister.java) and [SharedPreferencesObjectPersisterFactory](https://github.com/Kojir0/robospice-sharedpereferences/blob/master/src/main/java/com/byob/android/robospice/persistance/SharedPreferencesObjectPersisterFactory.java) class are abstractions.

[JacksonSharedPreferencesObjectPersister](https://github.com/Kojir0/robospice-sharedpereferences/blob/master/src/main/java/com/byob/android/robospice/persistance/json/jackson/JacksonSharedPreferencesObjectPersister.java) and [JacksonSharedPreferencesObjectPersisterFactory](https://github.com/Kojir0/robospice-sharedpereferences/blob/master/src/main/java/com/byob/android/robospice/persistance/json/jackson/JacksonSharedPreferencesObjectPersisterFactory.java) class are implementations base on jackson to store the data in JSON format.

Usage
-----

In your SpiceService, configure the cacheManager:

```java
@Override
public CacheManager createCacheManager(Application application) throws CacheCreationException {
  	CacheManager cacheManager = new CacheManager();
		JacksonSharedPreferencesObjectPersisterFactory jacksonSharedPreferencesObjectPersisterFactory = new JacksonSharedPreferencesObjectPersisterFactory(APP_PREFIX, application);
		cacheManager.addPersister(jacksonSharedPreferencesObjectPersisterFactory);
		return cacheManager;
}
```

To access the data in the cache, you can instance a JacksonSharedPreferencesObjectPersister :

```java
final JacksonSharedPreferencesObjectPersister<MyClass> persister = new JacksonSharedPreferencesObjectPersister<MyClass>(APP_PREFIX, getApplication(), MyClass.class);
jacksonSharedPreferencesObjectPersister.loadDataFromCache(key, DurationInMillis.ALWAYS_RETURNED);

```
