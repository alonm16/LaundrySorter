package com.example.laundrysorter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;


import android.os.Bundle;

import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;


public class HomeActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    enum ColorPick {
        BLACK,WHITE,GRAY,BLUE,ORANGE,GREEN,YELLOW,LIGHTBLUE,BROWN,RED,PINK,PURPLE;

        public static ColorPick fromInteger (int x){
            switch (x) {
                case 0: return BLACK;
                case 1: return WHITE;
                case 2: return GRAY;
                case 3: return BLUE;
                case 4: return ORANGE;
                case 5: return GREEN;
                case 6: return YELLOW;
                case 7: return LIGHTBLUE;
                case 8: return BROWN;
                case 9: return RED;
                case 10: return PINK;
                case 11: return PURPLE;
            }
            return WHITE;
        }


        public static int fromValue (ColorPick c){
            switch (c) {
                case BLACK: return 0;
                case WHITE: return 1;
                case GRAY: return 2;
                case BLUE: return 3;
                case ORANGE: return 4;
                case GREEN: return 5;
                case YELLOW: return 6;
                case LIGHTBLUE: return 7;
                case BROWN: return 8;
                case RED: return 9;
                case PINK: return 10 ;
                case PURPLE: return 11 ;
            }
            return 1;
        }
    };


    private static final int PICK_COLOR_REQUEST = 1;
    private View requestedView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




    }


    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        super.onStart();
    }

    public void logout(View view)
    {
        mAuth.signOut();
        mGoogleSignInClient.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void onBasketColorClick (View view){
        Intent intent = new Intent(this,ColorPicker.class);
        requestedView = view;
        startActivityForResult(intent,PICK_COLOR_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_CANCELED || data == null)
            return;

        if (requestCode==PICK_COLOR_REQUEST){
            ColorPick color = ColorPick.fromInteger(data.getIntExtra("color",0));
            int color_res = convertColorToResourceColor(color);
            requestedView.setBackgroundColor(color_res);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private int convertColorToResourceColor (ColorPick color){
        switch (color){
            case BLACK:
                return getResources().getColor(R.color.pickerBlack);
            case WHITE:
                return getResources().getColor(R.color.pickerWhite);
            case GRAY:
                return getResources().getColor(R.color.pickerLightGray);
            case BLUE:
                return getResources().getColor(R.color.pickerBlue);
            case ORANGE:
                return getResources().getColor(R.color.pickerOrange);
            case GREEN:
                return getResources().getColor(R.color.pickerGreen);
            case YELLOW:
                return getResources().getColor(R.color.pickerYellow);
            case LIGHTBLUE:
                return getResources().getColor(R.color.pickerLightBlue);
            case BROWN:
                return getResources().getColor(R.color.pickerBrown);
            case RED:
                return getResources().getColor(R.color.pickerRed);
            case PINK:
                return getResources().getColor(R.color.pickerLightPink);
            case PURPLE:
                return getResources().getColor(R.color.pickerPurple);
        }
        return getResources().getColor(R.color.pickerWhite);
    }




}
