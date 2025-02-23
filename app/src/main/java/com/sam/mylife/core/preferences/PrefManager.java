package com.sam.mylife.core.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.sam.mylife.core.model.AppUser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("unused")
public class PrefManager extends BasePref {

    private final SharedPreferences mPref;
    private static PrefManager sInstance;

    public static synchronized PrefManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PrefManager(context);
        }
        return sInstance;
    }

    public PrefManager(@NonNull Context context) {
        String prefsFile = context.getPackageName();
        mPref = context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE);
    }


    @Nullable
    public HashMap<String, Boolean> getWritingResponseTicks() {
        return getObject("WRITING_RESPONSE_TICK", new TypeToken<HashMap<String, Boolean>>() {
        }.getType());
    }

    public void setWritingResponseTick(String questionID) {
        HashMap<String, Boolean> tickMap = getObject("WRITING_RESPONSE_TICK", new TypeToken<HashMap<String, Boolean>>() {
        }.getType());
        if (tickMap == null) {
            tickMap = new HashMap<>();
            tickMap.put(questionID, true);
        }
        if (!tickMap.containsKey(questionID)) {
            tickMap.put(questionID, true);
        }
        setObject("WRITING_RESPONSE_TICK", tickMap);
    }

    public void clearSharePrefUserData() {
        deleteAllPreference();
    }

    @Override
    public SharedPreferences getPrefs() {
        return mPref;
    }

    private Editor getEditor() {
        return mPref.edit();
    }

    private void savePrefrenceData(String key, String value) {
        savePreference(key, value);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private <T> T getPrefrenceData(String key) {
        return getPreference(key);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private <T> T getPrefrenceData(String key, T type) {
        return getPreference(key, type);
    }

//    public void saveUserData(UserData user) {
//        setObject(Prefkeys.USER_INFO, user);
//    }
//
//    @Nullable
//    public UserData getUserData() {
//        return getObject(Prefkeys.USER_INFO, UserData.class);
//    }


    public Boolean isLoginSkip() {
        return getPreference(Prefkeys.LOGIN_SKIP, false);
    }

    public void setLoginSkip(boolean isLogged) {
        savePreference(Prefkeys.LOGIN_SKIP, isLogged);
    }


    private void addNewObjectStringInList(String sharedPrefKey, String objectString, Type type) {
        ArrayList<String> fileListForQuestion = getObject(sharedPrefKey, new TypeToken<List<String>>() {
        }.getType());
        if (fileListForQuestion != null) {
            fileListForQuestion.add(objectString);
        } else {
            fileListForQuestion = new ArrayList<>();
            fileListForQuestion.add(objectString);
        }
        setObject(sharedPrefKey, fileListForQuestion);
    }

    @Nullable
    private ArrayList<String> getList(String sharePrefKey) {
        return getObject(sharePrefKey, new TypeToken<List<String>>() {
        }.getType());
    }

    public void removeUploadTask(String objectString) {
        removeObjStringFromList("UPLOAD_TASK_LIST", objectString);
    }

    private void removeObjStringFromList(String sharedKey, String objString) {
        ArrayList<String> objStringList = getObject(sharedKey
                , new TypeToken<List<String>>() {
                }.getType());
        if (objStringList != null) {
            int index = objStringList.indexOf(objString);
            if (index != -1) {
                objStringList.remove(index);
            }
        } else {
            return;
        }
        setObject(sharedKey, objStringList);
    }

    public void addLanguages(HashMap<String, String> languageMap) {
        setObject(Prefkeys.LANGUAGE_MAP, languageMap);
    }

    @Nullable
    public Map<String, String> getLanguages() {
        return getObject(Prefkeys.LANGUAGE_MAP, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    //---------------------------------------------------------------------------------------------

    public void saveUserData(AppUser user) {
        setObject(Prefkeys.USER_INFO, user);
    }

    @Nullable
    public AppUser getUserData() {
        return getObject(Prefkeys.USER_INFO, AppUser.class);
    }


}

