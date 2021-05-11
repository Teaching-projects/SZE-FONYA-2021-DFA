package com.myitsolver.baseandroidapp.logic.login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;


import java.util.Arrays;
import java.util.List;


public class FacebookLoginManager {
    public static final String PERMISSION_FRIENDS = "user_friends";
    public static final String PERMISSION_LIKES = "user_likes";
    public static final String PERMISSION_POSTS = "user_posts";
    public static final String PERMISSION_EMAIL = "email";
    public static final String PERMISSION_POST_TO_WALL = "publish_actions";

    private CallbackManager manager;
    private Activity activity;

    public FacebookLoginManager(final Activity activity, final OnTokenUpdateFinishedListener listener){
        this.activity = activity;
        manager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(manager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        if (listener != null) {
                            listener.updateFinished(loginResult);
                        }

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    public void loginWithPermission(String permission){
        if (activity == null){
            Log.e("FLM", "'activity' is null in loginWithPermission");
            return;
        }
        if (permission.equals(PERMISSION_POST_TO_WALL)){
            LoginManager.getInstance().logInWithPublishPermissions(
                    activity,
                    Arrays.asList(permission));
        }else{
            LoginManager.getInstance().logInWithReadPermissions(
                    activity ,
                    Arrays.asList(permission));
        }

    }
    public void loginWithPermissions(List<String> permissions){

            LoginManager.getInstance().logInWithReadPermissions(
                    activity,
                    permissions);


    }

    public void logout(){
        LoginManager.getInstance().logOut();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        manager.onActivityResult(requestCode, resultCode, data);
    }

    public interface OnTokenUpdateFinishedListener{
        void updateFinished(LoginResult loginResult);
    }
}
