package com.gms.app.gmsapp;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class BottleInfoDialog {
    private Context context;
    private String str_info;


    public BottleInfoDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(String str) {
        Log.i("BottleInfoDialog",str);
        str_info = str;

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.bottle_info);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        final Button okButton = (Button) dlg.findViewById(R.id.okButton);
        final TextView et_message = (TextView) dlg.findViewById(R.id.et_message);
        et_message.setText(str);


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
/*
        String bottleBarCd = "";
        try {

            JSONObject jsonObject = new JSONObject(str_info);
            bottleId = jsonObject.getString("bottleId");
            bottleBarCd = jsonObject.getString("bottleBarCd");
            Log.i("BottleInfoDialog bottleId==",bottleId);
            Log.i("BottleInfoDialog bottleBarCd==", bottleBarCd);

            tv_bottleId.setText(bottleId.toString());
            tv_bottleBarCd.setText(bottleBarCd.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
    }
}
