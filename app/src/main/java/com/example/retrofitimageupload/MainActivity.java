package com.example.retrofitimageupload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText imageTitle;
    private Button btnChoose, btnUpload;
    private Bitmap bitmap;
    ProgressBar progressBar;
    private ImageView imageView;

    private static final int IMG_REQUEST = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageTitle = findViewById(R.id.imageTitle);
        btnChoose = findViewById(R.id.chooseBn);
        btnUpload  = findViewById(R.id.uploadBn);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progress);

//        progressBar.getProgress();

        btnChoose.setOnClickListener(this);
        btnUpload.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.chooseBn:
                selectImage();
                break;
            case R.id.uploadBn:
                    uploadImage();
                break;
        }
    }

    private  void uploadImage(){
        progressBar.setVisibility(View.VISIBLE);
        final String Image = imageToString();
        String Title = imageTitle.getText().toString();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Image> call = apiInterface.sendImage(Title, Image);
        call.enqueue(new Callback<com.example.retrofitimageupload.Image>() {
            @Override
            public void onResponse(Call<com.example.retrofitimageupload.Image> call, Response<com.example.retrofitimageupload.Image> response) {
                progressBar.setVisibility(View.GONE);
                Image serverResponse = response.body();
                Toast.makeText(MainActivity.this, serverResponse.getResponse(), Toast.LENGTH_LONG).show();
                imageView.setVisibility(View.GONE);
                imageTitle.setVisibility(View.GONE);
                btnUpload.setEnabled(false);
                btnChoose.setEnabled(true);
                imageTitle.setText("");
            }

            @Override
            public void onFailure(Call<com.example.retrofitimageupload.Image> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                imageTitle.setVisibility(View.VISIBLE);

                btnUpload.setEnabled(true);
                btnChoose.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }
}
