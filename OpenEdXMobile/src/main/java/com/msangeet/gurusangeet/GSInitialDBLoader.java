package com.msangeet.gurusangeet;

import android.content.Context;
import android.util.Log;

import com.msangeet.gurusangeet.gsmodels.GSUser;
import com.msangeet.gurusangeet.gsutils.GSDBSyncHandler;
import com.msangeet.gurusangeet.gsutils.data.GSUserDataManager;
import com.msangeet.gurusangeet.gsutils.data.db.SyncListener;

import java.util.Date;
import java.util.HashMap;

public class GSInitialDBLoader {
    private static final String TAG = "GSInitialDBLoader";

    private static GSInitialDBLoader singleInstance;
    private Context context;

    private GSInitialDBLoader() {
    }

    public static GSInitialDBLoader getInstance() {
        if (singleInstance == null) {
            singleInstance = new GSInitialDBLoader();
        }
        return singleInstance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void initialise(String userUUID, SyncListener syncListener) {
        initialise(getDummyUser(), syncListener);
    }

    public void initialiseWithEmail(String userEmail, SyncListener syncListener) {
        initialise(getDummyUser(), syncListener);
    }

    private void initialise(GSUser user, SyncListener syncListener) {
        GSUserDataManager userDataManager = null;
        try {
            userDataManager = GSUserDataManager.getInstance(context, "debug");
            userDataManager.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            syncListener.onError(e);
            return;
        }

        GSDBSyncHandler syncHandler = GSDBSyncHandler.getInstance(context, "debug");

        SyncListener localListener = new SyncListener() {
            @Override
            public void onProgress(long completed, long total) {
                Log.i(TAG, "Progress -> Completed: " + completed + ", Total: " + total);
                syncListener.onProgress(completed, total);
            }

            @Override
            public void onSuccess(long completed, long total) {
                Log.i(TAG, "DB Loading success");
                syncHandler.removeListener(this);
                syncListener.onSuccess(completed, total);

            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "DB Loading exception: " + e.getLocalizedMessage());
                e.printStackTrace();
                syncHandler.removeListener(this);
                syncListener.onError(e);
            }
        };

        syncHandler.setListener(localListener);
        syncHandler.startSync();
    }

    private GSUser getDummyUser() {
        HashMap map = new HashMap();

        map.put("documentType", GSUser.DOCUMENT_TYPE);
        map.put("uuid", "029b1740f2cf11e7a0c90a9c40766c47");
        map.put("teacherUUID", "47116c70ec8111e7a0c90a9c40766c47");
        map.put("firstName", "Vidya");
        map.put("lastName", "Gopalakrishnan-Test");
        map.put("inviteCode", "student");

        map.put("email", "gop34@msangeet.com");
        map.put("phone", null);
        map.put("countryCode", null);
        map.put("genre", "Carnatic Classical");
        map.put("gender", "Female");
        map.put("DOB", "29-05-1993");
        map.put("instrument", "Vocal");
        map.put("basePitch", 48);

        map.put("createdOn", new Date().getTime());
        map.put("modifiedOn", new Date().getTime());
        map.put("isTeacher", false);
        map.put("isStudent", true);
        map.put("isDeleted", false);
        map.put("inSupportMode", false);
        map.put("isPlannedRecordingMode", false);

        return new GSUser(map);
    }

    private void navigateToRecordScreen() {
    }

    private void navigateToPracticeScreen() {
    }
}
