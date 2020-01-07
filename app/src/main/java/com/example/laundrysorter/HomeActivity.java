package com.example.laundrysorter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;


import android.os.Bundle;

import android.view.View;

import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.AppCompatActivity;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


public class HomeActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    BluetoothSPP bluetooth;
    String body;

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
    private ColorPick basket1;
    private ColorPick basket2;
    private ColorPick basket3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        body = getIntent().getStringExtra("body");
        Toast.makeText(HomeActivity.this, "this is " + body, Toast.LENGTH_SHORT).show();
        bluetooth = new BluetoothSPP(this);

        if (!bluetooth.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            finish();
        }

        bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext(),"Connected to laundry sorter successfully :)",Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext(),"Connection lost :(",Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext(),"unable to connect :(",Toast.LENGTH_SHORT).show();
            }
        });
        bluetooth.setAutoConnectionListener(new BluetoothSPP.AutoConnectionListener() {
            public void onNewConnection(String name, String address) {
            }

            public void onAutoConnectionStarted() {
            }
        });


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
        if (!bluetooth.isBluetoothEnabled()) {
            bluetooth.enable();
        } else {
            if (!bluetooth.isServiceAvailable()) {
                bluetooth.setupService();
                bluetooth.startService(BluetoothState.DEVICE_OTHER);
            }
        }
        if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
            bluetooth.disconnect();
        } else {
            bluetooth.autoConnect("HC-06");
        }
    }
    public void onDestroy() {
        super.onDestroy();
        bluetooth.stopService();
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

    public void onSaveClick(View view){
//        Intent intent = new Intent(this, bluetooth_activity.class);
//        startActivity(intent);

        bluetooth.send("begin",false);
        bluetooth.send(convertColorToString(basket1)+"$",false);
        bluetooth.send(convertColorToString(basket2)+"$",false);
        bluetooth.send(convertColorToString(basket3),false);

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
            String basket_num = requestedView.getTag().toString();
            if (basket_num.equals("1")){
                basket1 = color;
            }
            else if (basket_num.equals("2")) {
                basket2 = color;
            }
            else {
                basket3 = color;
            }
        }
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bluetooth.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetooth.setupService();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
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

    private String convertColorToString (ColorPick color){
        switch (color){
            case BLACK:
                return "BLACK";
            case WHITE:
                return "WHITE";
            case GRAY:
                return "GRAY";
            case BLUE:
                return "BLUE";
            case ORANGE:
                return "ORANGE";
            case GREEN:
                return "GREEN";
            case YELLOW:
                return "YELLOW";
            case LIGHTBLUE:
                return "LIGHTBLUE";
            case BROWN:
                return "BROWN";
            case RED:
                return "RED";
            case PINK:
                return "PINK";
            case PURPLE:
                return "PURPLE";
        }
        return "WHITE";
    }





}
