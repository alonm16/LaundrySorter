package com.example.laundrysorter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;


public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    public void logout(View view)
    {
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
