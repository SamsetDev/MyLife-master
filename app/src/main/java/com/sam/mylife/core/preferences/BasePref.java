package com.sam.mylife.core.preferences;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Copyright (C) Glasswing Partner - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * <p>
 * Created by Sanjay Singh (samset) on 07,May,2021 at 5:06 PM for Glasswing Partner.
 * <p>
 * New Delhi,India
 */


@SuppressWarnings({"SameParameterValue", "WeakerAccess", "unused"})
public abstract class BasePref {


    private static final String TAG = BasePref.class.getSimpleName();


    private SharedPreferences.Editor getEditor() {
        return getPrefs().edit();
    }

    protected abstract SharedPreferences getPrefs();


    /**
     * Clears all data in SharedPreferences
     */
    public void deleteAllPreference() {
        try {
            getPrefs().edit().clear().apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeKey(String key) {
        getPrefs().edit().remove(key).apply();
    }

    @SuppressWarnings("unchecked")
    public boolean containsKey(String key) {
        return getPrefs().contains(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getPreference(String key) {
        return (T) getPrefs().getAll().get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getPreference(String key, T defValue) {
        T returnValue = (T) getPrefs().getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }

    @SuppressWarnings("unchecked")
    public boolean isPreferenceExists(String key) {
        return getPrefs().contains(key);
    }


    public void savePreference(String key, Object value) {
        deletePreferenceIfPresence(key);
        if (value instanceof Boolean) {
            getPrefs().edit().putBoolean(key, (Boolean) value).apply();
        } else if (value instanceof Integer) {
            getPrefs().edit().putInt(key, (Integer) value).apply();
        } else if (value instanceof Float) {
            getPrefs().edit().putFloat(key, (Float) value).apply();
        } else if (value instanceof Long) {
            getPrefs().edit().putLong(key, (Long) value).apply();
        } else if (value instanceof String) {
            getPrefs().edit().putString(key, (String) value).apply();
        } else if (value instanceof Enum) {
            getPrefs().edit().putString(key, value.toString()).apply();
        } else if (value != null) {
            throw new RuntimeException("Attempting to save non-primitive preference");
        }

    }

    public void deletePreferenceIfPresence(String key) {
        if (getPrefs().contains(key)) {
            getPrefs().edit().remove(key).apply();
        }
    }

    /**
     * Persists an Object in prefs at the specified key, class of given Object must implement Model
     * interface
     *
     * @param key         String
     * @param modelObject Object to persist
     * @param <M>         Generic for Object
     */
    protected <M> void setObject(String key, M modelObject) {
        String value = createJSONStringFromObject(modelObject);
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String createJSONStringFromObject(Object object) {
        return new Gson().toJson(object);
    }

    /**
     * Fetches the previously stored Object of given Class from prefs
     *
     * @param key                String
     * @param classOfModelObject Class of persisted Object
     * @param <M>                Generic for Object
     * @return Object of given class
     */
    @Nullable
    protected <M> M getObject(String key, Type classOfModelObject) {
        String jsonData = getPrefs().getString(key, null);

        if (null != jsonData) {
            try {
                Gson gson = new Gson();
                return gson.fromJson(jsonData, classOfModelObject);
            } catch (ClassCastException cce) {
                Log.e(TAG, "Cannot convert string obtained from prefs into collection of type " +
                        classOfModelObject.toString(), cce);
            }
        }
        return null;
    }

}
