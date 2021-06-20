package com.example.retrofit_test;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.retrofit_test.AIP.ApiService;
import com.example.retrofit_test.AIP.Const;
import com.example.retrofit_test.Model.User;
import com.example.retrofit_test.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1;
    private   ActivityMainBinding binding ;
    public static final String TAG = "LOG";
    private Uri url ;
    private ProgressDialog mProgressDialog ;
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG, "ONACTIVITY" );
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if(data == null)
                        {
                            return;
                        }
                        Uri uri = data.getData();
                        url = uri ;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            binding.imgAvatar.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

    );



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
       mProgressDialog = new ProgressDialog(this);
       mProgressDialog.setMessage("Please wait ..... ");
        binding.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 onClickSelectImage();
            }
        });
        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onClickUpLoadData();
            }
        });
    }

    private void onClickUpLoadData() {
        if(url!=null)
        {
            callUpLoadData();
        }


    }

    private void callUpLoadData() {
        mProgressDialog.show();
        String username = binding.edUserName.getText().toString().trim();
        RequestBody userNameBody = RequestBody.create(MediaType.parse("multipart/form-data"),username );
        String password = binding.edPassword.getText().toString().trim();
        RequestBody passWordBody = RequestBody.create(MediaType.parse("multipart/form-data"), password );
        // Get Uri

        String strRealPath = RealPathUtil.getRealPath(this , url);
        Log.e(TAG , "" + strRealPath);
        File file = new File(strRealPath);
        RequestBody avatarBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multapartBodyAvt = MultipartBody.Part.createFormData(Const.KEY_AVT,file.getName(),avatarBody);



        ApiService.apiService.registerAccount(userNameBody,passWordBody , multapartBodyAvt).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                mProgressDialog.dismiss();
                User user =response.body();
                Log.e(TAG , "Main : " + user.getUsername() + " | " + user.getPassword() + " | " + user.getAvatar());
                Intent intent = new Intent(MainActivity.this , ShowRetrofit.class);
                intent.putExtra("data", user);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(MainActivity.this , "Error" , Toast.LENGTH_LONG).show();
                Log.e(TAG , "Error : " );
            }
        });

    }

    private void onClickSelectImage() {
        // if version android is less than android 6, I would not do request Permission and Open Gallery
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            openGallery();
            return;
        }
        // if user has requested permission , I would open rallery
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            openGallery(); // Chọn ảnh
        } else {
            // neu chua thi xin cap phep
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                openGallery();
            }
        }

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent,"Select Picture"));

    }
}