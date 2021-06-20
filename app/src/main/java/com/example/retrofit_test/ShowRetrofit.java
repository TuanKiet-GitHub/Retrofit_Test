package com.example.retrofit_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.retrofit_test.Model.User;
import com.example.retrofit_test.databinding.ActivityMainBinding;
import com.google.gson.Gson;


public class ShowRetrofit extends AppCompatActivity {
   TextView tvUserName , tvPassWord ;
   ImageView imgView ;
   Button btnBack ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_retrofit);
        tvUserName = findViewById(R.id.tvUserName) ;
        tvPassWord = findViewById(R.id.tvPassword);
        imgView = findViewById(R.id.imgShowAvatar);
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("data");
        Log.e("TAG11" , "Show : " + user.getAvatar() + " | "+ user.getPassword() + user.getUsername());
        if(user!=null)
        {
            tvUserName.setText(user.getUsername());
            tvPassWord.setText(user.getPassword());
            Glide.with(getApplicationContext()).load(user.getAvatar()).into(imgView);
        }
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }
}