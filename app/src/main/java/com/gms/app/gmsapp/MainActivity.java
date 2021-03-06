package com.gms.app.gmsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btn_come, btn_out, btn_incar, btn_charge, btn_sales, btn_rental, btn_back, btn_etc, btn_scan, btn_deleteAll;

    private int REQUEST_TEST = 1;
    private static ArrayList<MainData> arrayList;
    private static MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private static final String TAG = "MainActivity";
    private String shared = "file";
    private String userId = "";
    private String host="";

    //qr code scanner object
    private IntentIntegrator qrScan;
    int tempInd = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        host = getString(R.string.host_name);

        btn_come = (Button)findViewById(R.id.btn_come);     //입고
        btn_out = (Button)findViewById(R.id.btn_out);       // 출고
        btn_incar = (Button)findViewById(R.id.btn_incar);   // 상차
        btn_charge = (Button)findViewById(R.id.btn_charge); // 충전
        btn_sales = (Button)findViewById(R.id.btn_sales);       // 판매
        btn_rental = (Button)findViewById(R.id.btn_rental);     //대여
        btn_back = (Button)findViewById(R.id.btn_back);         //회수
        btn_etc = (Button)findViewById(R.id.btn_etc);           //기타
        btn_scan = (Button)findViewById(R.id.btn_scan);         // 스캔하기
        btn_deleteAll = (Button)findViewById(R.id.btn_deleteAll);       // 리스트 삭제

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();
        mainAdapter = new MainAdapter(arrayList);
        recyclerView.setAdapter(mainAdapter);

        //intializing scan object
        qrScan = new IntentIntegrator(this);
        //IntentIntegrator integrator = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false);
        //qrScan.setCaptureActivity(MainActivity.class);
        //qrScan.initiateScan();

        //SharedPreferences 로그인 정보 유무 확인
        SharedPreferences sharedPreferences = getSharedPreferences(shared,0);
        userId = sharedPreferences.getString("id", "");

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                //startActivityForResult(intent, REQUEST_TEST);
                /* TODO 임시 주석
                qrScan.setPrompt("Scanning...");
                qrScan.setBeepEnabled(false);//바코드 인식시 소리
                //qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
/*/
                //1/9 임시 테스트
                if(tempInd==3) tempInd = 0;
                // = new String[]{"AA315923""AA315784","AA316260"};
                String[] barcodes = new String[]{"AA315744", "AA316255", "AA316192"};
                String testBarCd = barcodes[tempInd++];
                String url = host+"api/bottleDetail.do?bottleBarCd="+testBarCd;//AA315923";

                // AsyncTask를 통해 HttpURLConnection 수행.
                NetworkTask networkTask = new NetworkTask(url, null);
                networkTask.execute();


            }
        });

        final TextView main_label = (TextView) findViewById(R.id.main_label);

        btn_come.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(arrayList.size() <= 0){
                    Toast.makeText(MainActivity.this, "용기를 선택하세요", Toast.LENGTH_LONG).show();
                }else {
                    CustomDialog customDialog = new CustomDialog(MainActivity.this, btn_come.getText().toString());

                    String tempStr = "";
                    for (int i = 0; i < arrayList.size(); i++) {
                        tempStr += arrayList.get(i).getTv_bottleId() + ",";
                    }
                    Toast.makeText(MainActivity.this, tempStr, Toast.LENGTH_LONG).show();

                    // 커스텀 다이얼로그를 호출한다.
                    // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                    customDialog.callFunction(main_label, tempStr, userId);
                }
            }
        });

        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog customDialog = new CustomDialog(MainActivity.this, btn_out.getText().toString());

                String tempStr = "";
                for(int i = 0 ; i < arrayList.size() ; i++) {
                    tempStr += arrayList.get(i).getTv_bottleId()+",";
                }
                Toast.makeText(MainActivity.this, tempStr, Toast.LENGTH_LONG).show();

                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                customDialog.callFunction(main_label, tempStr, userId);

            }
        });

        btn_incar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog customDialog = new CustomDialog(MainActivity.this, btn_incar.getText().toString());

                String tempStr = "";
                for(int i = 0 ; i < arrayList.size() ; i++) {
                    tempStr += arrayList.get(i).getTv_bottleId()+",";
                }
                Toast.makeText(MainActivity.this, tempStr, Toast.LENGTH_LONG).show();

                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                customDialog.callFunction(main_label, tempStr, userId);
            }
        });

        btn_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayList.size() <= 0){
                    Toast.makeText(MainActivity.this, "용기를 선택하세요", Toast.LENGTH_LONG).show();
                }else {
                    CustomDialog customDialog = new CustomDialog(MainActivity.this, btn_charge.getText().toString());

                    String tempStr = "";
                    for (int i = 0; i < arrayList.size(); i++) {
                        tempStr += arrayList.get(i).getTv_bottleId() + ",";
                    }
                    Toast.makeText(MainActivity.this, tempStr, Toast.LENGTH_LONG).show();

                    // 커스텀 다이얼로그를 호출한다.
                    // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                    customDialog.callFunction(main_label, tempStr, userId);
                }
            }
        });

        btn_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog customDialog = new CustomDialog(MainActivity.this, btn_sales.getText().toString());

                String tempStr = "";
                for(int i = 0 ; i < arrayList.size() ; i++) {
                    tempStr += arrayList.get(i).getTv_bottleId()+",";
                }
                Toast.makeText(MainActivity.this, tempStr, Toast.LENGTH_LONG).show();

                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                customDialog.callFunction(main_label, tempStr, userId);
            }
        });


        btn_rental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog customDialog = new CustomDialog(MainActivity.this, btn_sales.getText().toString());

                String tempStr = "";
                for(int i = 0 ; i < arrayList.size() ; i++) {
                    tempStr += arrayList.get(i).getTv_bottleId()+",";
                }
                Toast.makeText(MainActivity.this, tempStr, Toast.LENGTH_LONG).show();

                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                customDialog.callFunction(main_label, tempStr, userId);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog customDialog = new CustomDialog(MainActivity.this, btn_back.getText().toString());

                String tempStr = "";
                for(int i = 0 ; i < arrayList.size() ; i++) {
                    tempStr += arrayList.get(i).getTv_bottleId()+",";
                }
                Toast.makeText(MainActivity.this, tempStr, Toast.LENGTH_LONG).show();

                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                customDialog.callFunction(main_label, tempStr, userId);
            }
        });

        btn_deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "리스트를 삭제", Toast.LENGTH_SHORT).show();
                arrayList.clear();
                mainAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "취소!", Toast.LENGTH_SHORT).show();
            } else {
                //qrcode 결과가 있으면
                Toast.makeText(MainActivity.this, "스캔완료!"+result.getContents(), Toast.LENGTH_SHORT).show();
                try {
                    //data를 json으로 변환
                    JSONObject obj = new JSONObject(result.getContents());
                    //textViewName.setText(obj.getString("name"));
                    //textViewAddress.setText(obj.getString("address"));
                    Toast.makeText(MainActivity.this, obj.getString("name"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                    //textViewResult.setText(result.getContents());
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    static  void  clearArrayList(){
        arrayList.clear();
        mainAdapter.notifyDataSetChanged();
    }
    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            com.gms.app.gmsapp.RequestHttpURLConnection requestHttpURLConnection = new com.gms.app.gmsapp.RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            Log.i("MainActivity doInBackground","rseult="+result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Main2ctivity onPostExecute","s="+s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            //tv_result.setText(s);
            String bottleBarCd="";
            String bottleId="";
            String productNm="";
            Button btn_info = findViewById(R.id.btn_info);
            try {
                JSONObject jsonObject = new JSONObject(s);
                bottleId = jsonObject.getString("bottleId");
                bottleBarCd = jsonObject.getString("bottleBarCd");
                productNm = jsonObject.getString("productNm");
                Log.i("Main2ctivity onPostExecute","tv_bottleBarCd="+bottleBarCd+ "productNm ="+productNm);

                SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(bottleBarCd,s);
                editor.commit();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //tv_result.setText(bottleBarCd+" "+s);
            MainData mainData = new MainData(bottleId,bottleBarCd,productNm,btn_info);

            arrayList.add(mainData);
            mainAdapter.notifyDataSetChanged();
        }
    }



}
