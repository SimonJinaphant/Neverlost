package com.neverlost.ubc.neverlost;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class login extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
    }



        /*
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            callbackManager = CallbackManager.Factory.create();
            LoginButton loginButton = (LoginButton) view.findViewById(R.id.usersettings_fragment_login_button);
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() { ... });

            @Override
            public View onCreateView(
                    LayoutInflater inflater,
                    ViewGroup container,
                    Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.splash, container, false);

                loginButton = (LoginButton) view.findViewById(R.id.login_button);
                loginButton.setReadPermissions("email");
                // If using in a fragment
                loginButton.setFragment(this);
                // Other app specific specialization

                // Callback registration
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
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
        }
        */






}
