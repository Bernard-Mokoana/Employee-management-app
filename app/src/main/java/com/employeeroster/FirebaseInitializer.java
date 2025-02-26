package com.employeeroster;


import com.google.firebase.FirebaseApp;

public class FirebaseInitializer {

    public static void initializeFirebase() {
        FirebaseApp.initializeApp(FirebaseApp.getInstance().getApplicationContext());
    }
}
