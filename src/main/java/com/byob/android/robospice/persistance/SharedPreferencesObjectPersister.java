package com.byob.android.robospice.persistance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.ObjectPersister;
import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.exception.CacheSavingException;

public abstract class SharedPreferencesObjectPersister<T> extends ObjectPersister<T> {

	public final static String SHARED_PREFERENCES_KEY_SEPARATOR = "_";
	
	private final static String TIMESTAMP_SUFFIX = "_timestamp";
	private final static String SANITIZED_SUFFIX = "_sanitized"; 
	
	private final SharedPreferences sharedPreferences;
	
	public SharedPreferencesObjectPersister(final String sharedPreferencesPrefix, final Context context, final Class<T> clazz) {
		super(null, clazz);
		final String prefix = sharedPreferencesPrefix + SHARED_PREFERENCES_KEY_SEPARATOR + clazz.getSimpleName(); 
		this.sharedPreferences = context.getSharedPreferences(prefix, Application.MODE_PRIVATE);
	}
	
	private String cacheKeyToString (Object cacheKey){
		// A cacheKey cannot end with TIMESTAMP_SUFFIX
		String key = cacheKey.toString();
		if (key.endsWith(TIMESTAMP_SUFFIX)){
			key += SANITIZED_SUFFIX;
		}
		return key;
	}
	
	private String cacheKeyToTimestamp (Object cacheKey){
		return cacheKey.toString() + TIMESTAMP_SUFFIX;
	}
	
	protected boolean isCachedAndNotExpired(Object cacheKey, long maxTimeInCacheBeforeExpiry) {
		// Is the key in shared preferences ?
		if (!sharedPreferences.contains(cacheKeyToString(cacheKey))){
			return false;
		}

		// Always returned case
		if(maxTimeInCacheBeforeExpiry == DurationInMillis.ALWAYS_RETURNED){
			return true;
		}
		
		// Check the timestamp 
		final long timestamp = sharedPreferences.getLong(cacheKeyToTimestamp(cacheKey), 0);
		final long timeInCache = System.currentTimeMillis() - timestamp;
		if (timeInCache <= maxTimeInCacheBeforeExpiry) {
            return true;
        }
		
		return false;
	}

	@Override
	public T loadDataFromCache(Object cacheKey, long maxTimeInCache) throws CacheLoadingException {
		if (isCachedAndNotExpired(cacheKey, maxTimeInCache)){
			return deserializeData(sharedPreferences.getString(cacheKey.toString(), null));
		}
		return null;
	}

	@Override
	public List<T> loadAllDataFromCache() throws CacheLoadingException {
		final List<T> datas = new ArrayList<T>();
		for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()){
			if (!entry.getKey().endsWith(TIMESTAMP_SUFFIX)){
				// Its a value, not a timestamp
				datas.add(deserializeData((String) entry.getValue()));
			}
		}
		return datas;
	}

	@Override
	public List<Object> getAllCacheKeys() {
		final List<Object> cacheKeys = new ArrayList<Object>();
		cacheKeys.addAll(sharedPreferences.getAll().keySet());
		return cacheKeys;
	}

	@Override
	public T saveDataToCacheAndReturnData(T data, Object cacheKey) throws CacheSavingException {
		final String stringData = serializeData(data);
		final boolean commitSuccess = sharedPreferences.edit().putString(cacheKeyToString(cacheKey), stringData).putLong(cacheKeyToTimestamp(cacheKey), System.currentTimeMillis()).commit();
		if (!commitSuccess){
			throw new CacheSavingException("Data could not be saved in cache for cacheKey=" + cacheKey);
		}
		return data;
	}

	@Override
	public boolean removeDataFromCache(Object cacheKey) {
		final String stringKey = cacheKeyToString(cacheKey);
		final boolean containsKey = sharedPreferences.contains(stringKey);
		sharedPreferences.edit().remove(stringKey).remove(cacheKeyToTimestamp(cacheKey)).commit();
		return containsKey;
	}

	@Override
	public void removeAllDataFromCache() {
		sharedPreferences.edit().clear().commit();
	}

	@Override
	public long getCreationDateInCache(Object cacheKey) throws CacheLoadingException {
		final String timestampKey = cacheKeyToTimestamp(cacheKey);
		if (!sharedPreferences.contains(timestampKey)){
			throw new CacheLoadingException("Data could not be found in cache for cacheKey=" + cacheKey);
		}
		return sharedPreferences.getLong(timestampKey, 0);
	}

	/**
	 * Convert String data to T object
	 * @param json
	 * @return the deserialized data
	 * @throws CacheLoadingException
	 */
    protected abstract T deserializeData(String data) throws CacheLoadingException;

    /**
     * Converts T object to String data
     * @param data
     * @return the string formatted data
     * @throws IOException
     * @throws CacheSavingException
     */
    protected abstract String serializeData(T data) throws CacheSavingException;
}
