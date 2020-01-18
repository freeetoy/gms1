package com.gms.app.gmsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private Context context;
    private String bottles;
    private String shared = "file";
    private String userId = "";

    private Button btn_hole,btn_vacuum, btn_close;


    public BottomSheetDialog(Context context, String bottles) {
        this.context = context;
        this.bottles = bottles;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        //SharedPreferences 로그인 정보 유무 확인
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared,0);
        userId = sharedPreferences.getString("id", "");

        btn_hole  = v.findViewById(R.id.btn_hole);
        btn_vacuum  = v.findViewById(R.id.btn_vacuum);
        btn_close  = v.findViewById(R.id.btn_close);

        btn_hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Button 1 clicked");
                ChargeDialog customDialog = new ChargeDialog(context, btn_hole.getText().toString());

                //Toast.makeText(context, bottles, Toast.LENGTH_SHORT).show();
                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.

                customDialog.callFunction(bottles, userId);
                dismiss();
            }
        });
        btn_vacuum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Button 1 clicked");
                ChargeDialog customDialog = new ChargeDialog(context, btn_vacuum.getText().toString());
                //Toast.makeText(context, bottles, Toast.LENGTH_SHORT).show();
                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.

                customDialog.callFunction(bottles, userId);

                dismiss();
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
