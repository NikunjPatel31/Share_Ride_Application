package com.example.shareride;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class CropperActivity extends AppCompatActivity {

    String result; // this is for the final result. We will pass this to final activity
    Uri fileUri; // this is for the image that we pick from the gallery

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);

        readIntent();

        String dest_uri = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();

        UCrop.Options options = new UCrop.Options();

        UCrop.of(fileUri, Uri.fromFile(new File(getCacheDir(), dest_uri)))
                .withOptions(options)
                .withAspectRatio(0,0)
                .withAspectRatio(2000,2000)
                .start(CropperActivity.this);
    }

    private void readIntent() {
        // this method will be used to read the uri which we will get through the parent activity
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            result = intent.getStringExtra("DATA");
            fileUri = Uri.parse(result); // we are converting string to uri
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("RESULT", resultUri+"");
            setResult(-1, returnIntent);
            finish();
        }

    }
}