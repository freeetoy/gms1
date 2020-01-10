package com.gms.app.gmsapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomDialog {
    private Context context;
    String[] items;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView ;


    String bottleType = "";
    String buttonType = "";
    String bottles;
    String customerId="";
    String userId = "";
    String host ="";


    public CustomDialog(Context context, String bType) {
        this.context = context;
        this.buttonType = bType;

        host = context.getString(R.string.host_name);

        if(buttonType.equals("판매") || buttonType.equals("대여") || buttonType.equals("회수"))
            new HttpAsyncTask().execute(host+"api/customerAllList.do");
        else {
            if(!buttonType.equals("충전")) {
                new HttpAsyncTask().execute(host + "api/carList.do");
            }
        }
        //new HttpAsyncTask().execute("http://172.30.57.228:8080/api/carList.do");

    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final TextView main_label, String bottle , String id ){

        bottles= bottle;
        userId = id;

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);


        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.custom_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final TextView title = (TextView) dlg.findViewById(R.id.title);
        final EditText message = (EditText) dlg.findViewById(R.id.mesgase);
        final Button okButton = (Button) dlg.findViewById(R.id.okButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton);

        final RadioGroup rg_gender = dlg.findViewById(R.id.rg_gender);
        final RadioButton rb_man = dlg.findViewById(R.id.rb_man);
        final RadioButton rb_woman = dlg.findViewById(R.id.rb_woman);

        title.setText(buttonType);
        // Add Data to listView
        listView = (ListView) dlg.findViewById(R.id.listview);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "click item", Toast.LENGTH_SHORT).show();
                message.setText(items[position]);
            }
        });

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchItem(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_man) {
                    Toast.makeText(context,"실병 라디오버튼"+host, Toast.LENGTH_SHORT).show();
                    bottleType = "F";
                }else if(checkedId == R.id.rb_woman){
                    Toast.makeText(context,"공병 라디오버튼", Toast.LENGTH_SHORT).show();
                    bottleType = "E";
                }
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                //main_label.setText(message.getText().toString()+"--"+str_result);

                Toast.makeText(context, String.format("\"%s=%s=%s\" 을 입력하였습니다.", message.getText().toString(), bottleType, buttonType), Toast.LENGTH_SHORT).show();
                customerId = message.getText().toString();
                Toast.makeText(context, String.format("\"%s\" 을 입력하였습니다.", customerId), Toast.LENGTH_SHORT).show();

                // 서버 전송
                new HttpAsyncTask1().execute(host+"api/controlAction.do?userId="+userId+"&bottles="+bottles+"&customerNm="+customerId+"&bottleType="+bottleType+"&bottleWorkCd="+buttonType);

                //MainActivity List 제거
                MainActivity.clearArrayList();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }

    public void searchItem(String textToSearch){
        if(items!=null) {
            for (String item : items) {

                if (!item.contains(textToSearch)) {
                    listItems.remove(item);
                }
            }

            adapter.notifyDataSetChanged();
        }else {
            Toast.makeText(context, "items is null", Toast.LENGTH_SHORT).show();
        }
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, List<CustomerSimpleVO>> {
        private final String TAG = HttpAsyncTask.class.getSimpleName();
       // int REQUEST_CODE =
        // OkHttp 클라이언트
        OkHttpClient client = new OkHttpClient();

        @Override
        protected List<CustomerSimpleVO> doInBackground(String... params) {
            List<CustomerSimpleVO> customerList = new ArrayList<>();
            String strUrl = params[0];
            try {
                // 요청
                Request request = new Request.Builder()
                        .url(strUrl)
                        .build();
                // 응답
                Response response = client.newCall(request).execute();

                Gson gson = new Gson();

                // import java.lang.reflect.Type
                Type listType = new TypeToken<ArrayList<CustomerSimpleVO>>() {
                }.getType();
                customerList = gson.fromJson(response.body().string(), listType);

                Log.d(TAG, "onCreate: " + customerList.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return customerList;
        }

        @Override
        protected void onPostExecute(List<CustomerSimpleVO> customerList) {
            super.onPostExecute(customerList);

            if (customerList != null) {
                Log.d("HttpAsyncTask", customerList.toString());
                //CustomerSimpleAdapter adapter = new CustomerSimpleAdapter(customerList);

                items = new String[customerList.size()];
                for(int i =0;i<customerList.size();i++){
                    items[i]= customerList.get(i).getCustomerNm().toString();
                }
                //mCustomerListView.setAdapter(adapter);
                //initList(items);
                listItems =new ArrayList<>(Arrays.asList(items));
                //Create Array Adapter
                adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,listItems);

                listView.setAdapter(adapter);
            }
        }
    }

    private class HttpAsyncTask1 extends AsyncTask<String, Void, String> {

        private final String TAG = HttpAsyncTask1.class.getSimpleName();
        // int REQUEST_CODE =
        // OkHttp 클라이언트
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {
            //List<CustomerSimpleVO> customerList = new ArrayList<>();
            String strUrl = params[0];
            String result= "";
            try {
                // 요청
                Request request = new Request.Builder()
                        .url(strUrl)
                        .build();
                // 응답
                Response response = client.newCall(request).execute();
                result = response.body().string();
                Log.d(TAG, "response.body().string(): " + result);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }
}
