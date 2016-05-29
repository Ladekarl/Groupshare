package dk.iha.itsmap.f16.grp03.groupshare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import dk.iha.itsmap.f16.grp03.groupshare.R;
import dk.iha.itsmap.f16.grp03.groupshare.models.Friend;
import dk.iha.itsmap.f16.grp03.groupshare.utils.FacebookUtil;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;

    // Facebook integration implementation from https://developers.facebook.com/docs/facebook-login/android
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookUtil.initialiseFacebookSdk(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        if (loginButton != null) {
            registerLoginCallbacks(loginButton);
        }
    }

    private void registerLoginCallbacks(LoginButton loginButton) {
        loginButton.setReadPermissions("email", "public_profile", "user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, R.string.logged_in, Toast.LENGTH_SHORT).show();
                getUserInfo();
                startGroupOverviewActivity();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, R.string.login_cancelled, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, R.string.fb_login_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserInfo() {
        if (!FacebookUtil.isNetworkAvailable(this)) return;
        Bundle params = new Bundle();
        params.putString("fields", "id,name,picture.type(large)");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject obj = response.getJSONObject();
                        try {
                            Friend mySelf = FacebookUtil.jsonObjectToFriend(obj);
                            if (mySelf.getName() != null) {
                                FacebookUtil.UserInfo.setName(mySelf.getName());
                            }
                            if (mySelf.getPictureUrl() != null) {
                                FacebookUtil.UserInfo.setPictureUrl(mySelf.getPictureUrl());
                            }
                            FacebookUtil.UserInfo.setId(mySelf.getFacebookId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler checkLoginHandler = new Handler();
        checkLoginHandler.postDelayed(new Runnable() {
            public void run() {
                if (isLoggedIn()) {
                    getUserInfo();
                    startGroupOverviewActivity();
                }
            }
        }, 200);
    }

    private void startGroupOverviewActivity() {
        Intent startGroupOverviewIntent = new Intent(LoginActivity.this, GroupOverviewActivity.class);
        startActivity(startGroupOverviewIntent);
        finish();
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = FacebookUtil.getFacebookAccessToken(getApplicationContext());
        return !(accessToken == null || accessToken.isExpired());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
