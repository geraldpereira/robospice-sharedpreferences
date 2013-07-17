package com.byob.android.robospice.persistance.json.jackson;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;

import com.byob.android.robospice.persistance.SharedPreferencesObjectPersister;
import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.exception.CacheSavingException;

public class JacksonSharedPreferencesObjectPersister<T> extends SharedPreferencesObjectPersister<T> {

	 private final ObjectMapper jsonMapper;
	 
	public JacksonSharedPreferencesObjectPersister(final String sharedPreferencesPrefix, final Context context, final Class<T> clazz) {
		super(sharedPreferencesPrefix, context, clazz);
		 this.jsonMapper = new ObjectMapper();
	}

	@Override
	protected T deserializeData(String data) throws CacheLoadingException {
		try {
			return jsonMapper.readValue(data, getHandledClass());
		} catch (IOException e) {
			throw new CacheLoadingException(e);
		}
	}

	@Override
	protected String serializeData(T data) throws CacheSavingException {
		try {
			return jsonMapper.writeValueAsString(data);
		} catch (IOException e) {
			throw new CacheSavingException(e);
		}
	}

}
