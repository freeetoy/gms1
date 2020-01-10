package com.gms.app.gmsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private TextView txt_result;
    private Button btn_send;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scannerView = (ZXingScannerView)findViewById(R.id.zxscan);
        txt_result = (TextView)findViewById(R.id.txt_result);
        btn_send = (Button)findViewById(R.id.btn_send);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if(scannerView!=null){
                            scannerView.stopCamera();
                        }
                        scannerView.setResultHandler(ScanActivity.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(ScanActivity.this , "Yout must accept this permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();

/*
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                Intent intent = new Intent();
                Toast.makeText(ScanActivity.this , "전달할 데이타 값"+str, Toast.LENGTH_SHORT).show();
                intent.putExtra("bottleBarCd", str);

                setResult(Activity.RESULT_OK, intent); //이 액티비티가 종료되기전에 이 메소드를 호출함으로써 메인액티비티에 응답을 보낼 수 있다.

                finish();
                //startActivity(intent);
            }
        });
*/

    }

    @Override
    protected void onStop() {
        super.onStop();
        scannerView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }


    @Override
    public void handleResult(Result rawResult) {
        // Here we can receive rawResult;
        txt_result.setText(rawResult.getText());
        str = txt_result.getText().toString();
        scannerView.startCamera();

    }
}
