package com.gms.app.gmsapp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    // 서버 URL 설정
   // final static private String URL= "http://172.30.57.228:8080/api/loginAction.do";
    final static private String URL= "http://192.168.0.10:8080/api/loginAction.do";

    private Map<String, String> map;


    public LoginRequest(String id, String pw , Response.Listener<String> listener) {
        super(Method.PATCH,URL,listener,null);

        map = new HashMap<>();
        map.put("id",id);
        map.put("pw",pw);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
