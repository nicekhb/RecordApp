package com.sds.study.recordapp.record;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sds.study.recordapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by student on 2016-11-17.
 */

public class RecordMainActivity extends AppCompatActivity {
    String TAG;
    static final int REQUEST_RECORD_PERMISSION = 1;
    MediaRecorder recorder;
    ImageView bt_img;
    boolean isRecording = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getName();
        setContentView(R.layout.record_main);
        bt_img = (ImageView)findViewById(R.id.bt_img);
        recorder = new MediaRecorder();
    }

    public String getSaveFile(){
        File dir = new File(Environment.getExternalStorageDirectory(),"iot_record");
        /*현재시간*/
        //Long currentTime = System.currentTimeMillis();
        Date d = new Date();
        String currentTime = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(d);
        //Log.d(TAG,"currentTime = "+currentTime);
        File saveFile = new File(dir, currentTime+".mp4");
        Log.d(TAG,"saveFile = "+saveFile.getAbsolutePath());
        return saveFile.getAbsolutePath();
    }

    public  void startRecord(){
        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            recorder.setOutputFile(getSaveFile());
            recorder.prepare();
            recorder.start();
            Toast.makeText(this, "recording...",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bt_img.setImageResource(R.drawable.stop);
    }

    public void stopRecord(){
        recorder.stop();
        recorder.reset();
        Toast.makeText(this, "stop",Toast.LENGTH_SHORT).show();
        bt_img.setImageResource(R.drawable.record);
        showList();
    }

    public void showList(){
        Intent intent = new Intent(this, FileListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Log.d(TAG,"requestCode="+requestCode+",grantResults="+grantResults[0]);
        switch (requestCode){
            case REQUEST_RECORD_PERMISSION:
                if(permissions.length>0&&grantResults[0]==PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "읽기쓰기 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                }else if(permissions.length>0&&grantResults[1]==PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "마이크 사용권한이 필요합니다", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void btnClick(View view){
        //권한체크
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recordPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if(writePermission==PackageManager.PERMISSION_DENIED||recordPermission==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
            },REQUEST_RECORD_PERMISSION);
        }else {
            if(isRecording){
                stopRecord();
                isRecording = false;
            }else{
                startRecord();
                isRecording = true;
            }
        }
    }
}
