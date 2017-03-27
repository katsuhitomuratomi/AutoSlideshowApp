package jp.techacademy.katsuhito.muratomi.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private static final int PERMISSIONS_REQUES_CODE = 100;
    Timer timer;
    Handler handler = new Handler();
    int i = 0;
    ArrayList<Uri> arrayList = new ArrayList<Uri>();
    ImageView imageView = (ImageView) findViewById(R.id.imageView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button) findViewById(R.id.back);
        Button button2 = (Button) findViewById(R.id.saisei);
        Button button3 = (Button) findViewById(R.id.step);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getContentsInfo();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUES_CODE);
            }
        }


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUES_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                }
                break;
            default:
                break;
        }
    }

    private void getContentsInfo() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null

        );

        if (cursor.moveToFirst()) {


            Uri imageUri;
            do {
                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = cursor.getLong(fieldIndex);
                imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                arrayList.add(imageUri);
                Log.d("test", "URI" + imageUri.toString());


            } while (cursor.moveToNext());
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {


                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            int j = i % 3;

                            imageView.setImageURI(arrayList.get(j));
                            i++;

                        }
                    });
                }
            }, 100, 2000);


        }
        cursor.close();
    }


}
