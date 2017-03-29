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
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUES_CODE = 100;
    Timer timer;
    Handler handler = new Handler();
    ArrayList<Uri> arrayList = new ArrayList<Uri>();
    ImageView imageView;
    ImageView imageView2;
    Button button2;
    boolean start;
    int picnum=0;
    float kaiten=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2=(ImageView)findViewById(R.id.icon);
        Button button1 = (Button) findViewById(R.id.back);
        button2 = (Button) findViewById(R.id.saisei);
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
                if (arrayList.size() != 0) {

                    if (timer == null) {
                        start = true;
                        button2.setText("一時停止");
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        ViewPropertyAnimator animator=imageView2.animate();
                                        animator.setDuration(2000);
                                        animator.rotation(360f*kaiten);
                                       movenum(1);
                                        kaiten++;
                                    }
                                });
                            }
                        }, 100, 2000);
                    } else {
                        button2.setText("再生");
                        timer.cancel();
                        timer = null;
                        start = false;
                    }

                }

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.size() != 0) {
                    if (start == false) {
                        movenum(-1);
                    }
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.size() != 0) {
                    if (start == false) {
                       movenum(1);
                    }

                }

            }
        });

        if(arrayList.size()!=0){
            imageView.setImageURI(arrayList.get(0));
        }


    }

    public  void  movenum(int move){
        picnum=picnum+move;
        if(picnum>=arrayList.size()){
            picnum=0;
        }else if(picnum<0){
            picnum=arrayList.size()-1;
        }
        imageView.setImageURI(arrayList.get(picnum));
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
        }
        cursor.close();
    }
}
