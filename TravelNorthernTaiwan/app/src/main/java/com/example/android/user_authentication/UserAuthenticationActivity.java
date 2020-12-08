package com.example.android.user_authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.android.travelnortherntaiwan.DrawerActivity;
import com.example.android.travelnortherntaiwan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAuthenticationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auth);

        mAuth = FirebaseAuth.getInstance();
        Log.i("ACTIVITY CREATED", "userAuth");
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            //open the home page directly if a user is already logged in
            UserAuthenticationActivity.this.finish();
            startActivity(new Intent(this, DrawerActivity.class));
        }
    }

    public void openActivity(View button) {
        int id = button.getId();

        if(id == R.id.log_in_btn) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        else if(id == R.id.sign_up_btn) {
            startActivity(new Intent(this, SignupActivity.class));
        }
//        else if(id == R.id.language_btn) {
//            startActivity(new Intent(this, NewTripActivity.class));
//        }
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

}
