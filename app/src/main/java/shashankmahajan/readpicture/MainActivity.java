package shashankmahajan.readpicture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST =502 ;
    private static final int REQUEST_PERM_STORAGE = 302;
    private static final int REQUEST_PERM_CAMERA = 402;
    private static final String TAG = "MainActivity" ;

    //For runtime permissions(Camera and Storage)
    String reqCameraPerm[] = new String[]{Manifest.permission.CAMERA};
    String reqStoragePerm[] = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE};

    Button button_image ;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_image = (Button) findViewById(R.id.btn_pic);
        imageView = (ImageView) findViewById(R.id.image_view);

        button_image.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (hasCameraPerm()) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    //If you want the user to choose something based on MIME type, use ACTION_GET_CONTENT.
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
                else    {
                    askCameraPerm();
                    askStoragePerm();
                }
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Picasso.with(this).load(uri).fit().centerCrop().into(imageView);

        }
    }

    private boolean hasCameraPerm() {
        return (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }
    private void askCameraPerm() {

        ActivityCompat.requestPermissions(MainActivity.this, reqCameraPerm, REQUEST_PERM_CAMERA);
    }

    private boolean hasStoragePerm() {
        if (Build.VERSION.SDK_INT >= 23) {
            return (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        }
        return false;
    }
    private void askStoragePerm() {
        ActivityCompat.requestPermissions(MainActivity.this, reqStoragePerm, REQUEST_PERM_STORAGE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERM_STORAGE) {

            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: STORAGE PERMISSION GRANTED");
                }
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
