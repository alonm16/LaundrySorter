package com.example.laundrysorter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ColorPicker extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
    }


    public void colorClick (View view){
        String tag = view.getTag().toString();
        Intent intent = new Intent();
        switch (tag){
            case "WHITE": intent.putExtra("color", HomeActivity.ColorPick.fromValue(HomeActivity.ColorPick.WHITE)); break;
            case "BLACK": intent.putExtra("color",HomeActivity.ColorPick.fromValue(HomeActivity.ColorPick.BLACK));break;
            case "GRAY": intent.putExtra("color",HomeActivity.ColorPick.fromValue(HomeActivity.ColorPick.GRAY));break;
            case "BLUE": intent.putExtra("color",HomeActivity.ColorPick.fromValue(HomeActivity.ColorPick.BLUE));break;
            case "ORANGE": intent.putExtra("color",HomeActivity.ColorPick.fromValue(HomeActivity.ColorPick.ORANGE));break;
            case "PINK": intent.putExtra("color",HomeActivity.ColorPick.fromValue(HomeActivity.ColorPick.PINK));break;
            case "PURPLE": intent.putExtra("color",HomeActivity.ColorPick.fromValue(HomeActivity.ColorPick.PURPLE));break;
            case "BROWN": intent.putExtra("color",HomeActivity.ColorPick.fromValue(HomeActivity.ColorPick.BROWN));break;
            case "RED": intent.putExtra("color",HomeActivity.ColorPick.fromValue(HomeActivity.ColorPick.RED));break;
            case "LIGHTBLUE": intent.putExtra("color",HomeActivity.ColorPick.fromValue(HomeActivity.ColorPick.LIGHTBLUE));break;
            case "YELLOW": intent.putExtra("color",HomeActivity.ColorPick.fromValue(HomeActivity.ColorPick.YELLOW));break;
            case "GREEN": intent.putExtra("color",HomeActivity.ColorPick.fromValue(HomeActivity.ColorPick.GREEN));break;
        }
        setResult(RESULT_OK,intent);
        finish();
    }
}
