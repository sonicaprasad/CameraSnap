package com.example.camerasnap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.security.Permission;
import java.util.Date;

import static android.icu.text.DateTimePatternGenerator.PatternInfo.OK;

public class MainActivity extends AppCompatActivity {

    Button takePicButton;
    private ImageView imageView;
    Uri file;

    Button recordingButton;
    private ImageView thumbnailView;

    Intent data;
    private static final int VIDEO_CAPTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //capturing camera
        takePicButton = findViewById(R.id.takePicButton);
        imageView = findViewById(R.id.imageView);

        //recording
        recordingButton = findViewById(R.id.recordButton);
        //thumbnailView = findViewById(R.id.thumbnailImageView);


        /*recordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File mediaFile = new File(Environment.getExternalStorageState());
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                Uri fileURI = Uri.fromFile(mediaFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaFile);

                startActivityForResult(intent, 200);
            }
        });*/

       // final Intent[] data = new Intent[1];
        recordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

               startActivityForResult(data, 200);
            }
        });



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePicButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        takePicButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = Uri.fromFile(getOutputMediaFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, file);


                startActivityForResult(intent, 100);
            }
        });
    }



    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
            }
        }else if(requestCode == 200 && resultCode == RESULT_OK){
            Toast.makeText(this, "Video saved to :\n" + data.getExtras(), Toast.LENGTH_LONG ).show();
        }else if(resultCode == RESULT_CANCELED){
            Toast.makeText(this, "Video is canceled", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Failed to record video", Toast.LENGTH_LONG).show();
        }
    }

        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == 0) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePicButton.setEnabled(true);
                }
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private static File getOutputMediaFile () {
            File mediaStoreDir = new File(Environment.getExternalStorageState(new File(Environment.DIRECTORY_PICTURES)), "Camera Pics");

            if (!mediaStoreDir.exists()) {
                if (!mediaStoreDir.mkdirs()) {
                    return null;
                }
            }

            String timeStamp = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                timeStamp = new SimpleDateFormat("yyyyMMdd_HHMMss").format(new Date());
            }
            return new File(mediaStoreDir.getPath() + File.separator + "img" + timeStamp + ".jpg");
        }




}
