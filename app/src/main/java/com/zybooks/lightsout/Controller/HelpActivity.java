package com.zybooks.lightsout.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.zybooks.lightsout.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    public void onContactClick(View view) {
        Uri phoneNumber = Uri.parse("tel:111-222-3333");
        Intent intent = new Intent(Intent.ACTION_DIAL, phoneNumber);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void onFindClick(View view) {
        Uri location = Uri.parse("geo:0,0?q=1600+Pennsylvania+Ave+NW,+Washington,+DC");
        Intent intent = new Intent(Intent.ACTION_VIEW, location);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}