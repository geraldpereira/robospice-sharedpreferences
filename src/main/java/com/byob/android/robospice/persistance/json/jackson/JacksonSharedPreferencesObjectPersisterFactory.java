package com.byob.android.robospice.persistance.json.jackson;

import android.app.Application;

import com.byob.android.robospice.persistance.SharedPreferencesObjectPersisterFactory;
import com.octo.android.robospice.persistence.ObjectPersister;
import com.octo.android.robospice.persistence.exception.CacheCreationException;

public class JacksonSharedPreferencesObjectPersisterFactory extends SharedPreferencesObjectPersisterFactory{

	public JacksonSharedPreferencesObjectPersisterFactory(String sharedPreferencesPrefix, Application application) {
		super(sharedPreferencesPrefix, application);
	}

	@Override
	public <DATA> ObjectPersister<DATA> createObjectPersister(Class<DATA> clazz) throws CacheCreationException {
		return new JacksonSharedPreferencesObjectPersister<DATA>(sharedPreferencesPrefix, getApplication().getApplicationContext(), clazz);
	}

}
