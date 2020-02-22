package com.example.laundrysorter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;

import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


public class HomeActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    BluetoothSPP bluetooth;
    String body;
    DatabaseReference mDatabase =  FirebaseDatabase.getInstance().getReference();
    int maxCapacity = 20;


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
    private int basket1;
    private int basket2;
    private int basket3;
    private static final String prefBasket1 = "basket1";
    private static final String prefBasket2 = "basket2";
    private static final String prefBasket3 = "basket3";
    private View basket1view, basket2view,basket3view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        setBasketsCapacity();

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int defaultValue = 1;
        basket1 = sharedPref.getInt(prefBasket1, defaultValue);
        basket2 = sharedPref.getInt(prefBasket2, defaultValue);
        basket3 = sharedPref.getInt(prefBasket3, defaultValue);
        basket1view = findViewById(R.id.tvBasketColor1);
        basket2view = findViewById(R.id.tvBasketColor2);
        basket3view = findViewById(R.id.tvBasketColor3);

        setBasketsColors();

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
          //  bluetooth.autoConnect("HC-06");
        }
    }
    public void onDestroy() {
        super.onDestroy();
        bluetooth.stopService();
    }

    private void setBasketsColors() {
        ColorPick color1 = ColorPick.fromInteger(basket1);
        ColorPick color2 = ColorPick.fromInteger(basket2);
        ColorPick color3 = ColorPick.fromInteger(basket3);

        int color1_res = convertColorToResourceColor(color1);
        int color2_res = convertColorToResourceColor(color2);
        int color3_res = convertColorToResourceColor(color3);
        basket1view.setBackgroundColor(color1_res);
        basket2view.setBackgroundColor(color2_res);
        basket3view.setBackgroundColor(color3_res);
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

//        bluetooth.send("begin",false);
//        bluetooth.send(convertColorToString(basket1)+"$",false);
//        bluetooth.send(convertColorToString(basket2)+"$",false);
//        bluetooth.send(convertColorToString(basket3),false);

         SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPref.edit();
         editor.putInt(prefBasket1, basket1);
         editor.putInt(prefBasket2, basket2);
         editor.putInt(prefBasket3, basket3);
         editor.apply();

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
                basket1 = ColorPick.fromValue(color);
            }
            else if (basket_num.equals("2")) {
                basket2 = ColorPick.fromValue(color);
            }
            else {
                basket3 = ColorPick.fromValue(color);
            }
            /*SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(basketPreference, ColorPick.fromValue(color));
            editor.apply();*/
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


    public void setBasketsCapacity()
    {
        final TextView basket1 = findViewById(R.id.basket1Capacity);
        final TextView basket2 = findViewById(R.id.basket2Capacity);
        final TextView basket3 = findViewById(R.id.basket3Capacity);

        mDatabase.child("basket1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int capacity = (int)(long)dataSnapshot.getValue();
                capacity = 100*(maxCapacity-capacity)/maxCapacity;
                String percents = String.valueOf(capacity)+'%';
                basket1.setText("basket 1 \n" +"is " + percents + "full");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("basket2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int capacity = (int)(long)dataSnapshot.getValue();
                capacity = 100*(maxCapacity-capacity)/maxCapacity;
                String percents = String.valueOf(capacity)+'%';
                basket2.setText("basket 2 \n" +"is " + percents + "full");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child("basket3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int capacity = (int)(long)dataSnapshot.getValue();
                capacity = 100*(maxCapacity-capacity)/maxCapacity;
                String percents = String.valueOf(capacity)+'%';
                basket3.setText("basket 3 \n" +"is " + percents + "full");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
