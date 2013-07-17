package com.byob.android.robospice.persistance;

import android.app.Application;

import com.octo.android.robospice.persistence.ObjectPersisterFactory;

/**
 * ObjectPersisterFactory used to store cache data in shared preferences.
 * 
 * Each supported type has a separate shared preferences
 * 
 * @author Gerald Pereira
 * 
 */
public abstract class SharedPreferencesObjectPersisterFactory extends ObjectPersisterFactory {

	protected final String sharedPreferencesPrefix;

	public SharedPreferencesObjectPersisterFactory(final String sharedPreferencesPrefix, Application application) {
		super(application);
		this.sharedPreferencesPrefix = sharedPreferencesPrefix;
	}

}
