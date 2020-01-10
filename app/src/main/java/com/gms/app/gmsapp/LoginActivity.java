package com.gms.app.gmsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class LoginActivity extends AppCompatActivity {

    private EditText et_id, et_pass;
    private Button btn_login;
    private CheckBox chk_login;
    private String shared = "file";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = (EditText)findViewById(R.id.et_id);
        et_pass = (EditText)findViewById(R.id.et_pass);

        btn_login = (Button)findViewById(R.id.btn_login);
        chk_login = (CheckBox)findViewById(R.id.chk_login);

        //SharedPreferences 로그인 정보 유무 확인
        SharedPreferences sharedPreferences = getSharedPreferences(shared,0);
        String value = sharedPreferences.getString("id", "");

        if(value != null && value.length() > 0) {
            Toast.makeText(getApplicationContext(),"로그인이 되어 있습니다,"+value,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
           //intent.putExtra("id",id);
            //intent.putExtra("pw", name);
            startActivity(intent);
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_id.getText().toString();
                String pw = et_pass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            boolean success = false;
                            Log.e("LoginActivity", "jsonObject ="+jsonObject.toString());
                            success = jsonObject.getBoolean("success");
                            Log.e("LoginActivity", "success ="+success);
                            if(success) {
                                String id = jsonObject.getString("userId");
                                String name = URLDecoder.decode(jsonObject.getString("userNm"),"UTF-8");

                                //SharedPreferences 저장
                                if(chk_login.isChecked()) {
                                    SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    //String value = id.getText().toString();
                                    editor.putString("id", id);
                                    editor.putString("name", name);
                                    editor.commit();
                                }
                                Toast.makeText(getApplicationContext(),"로그인이 성공하였습니다,",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.putExtra("id",id);
                                intent.putExtra("pw", name);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"로그인이 실패하였습니다,",Toast.LENGTH_SHORT).show();
                                return;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException ue) {
                            ue.printStackTrace();
                        }

                    }
                };

                LoginRequest loginRequest = new LoginRequest(id,pw,responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }
}
