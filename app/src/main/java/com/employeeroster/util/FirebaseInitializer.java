package com.employeeroster.util;


import com.google.firebase.FirebaseApp;

public class FirebaseInitializer {

    public static void initializeFirebase() {
        FirebaseApp.initializeApp(FirebaseApp.getInstance().getApplicationContext());
    }
}
