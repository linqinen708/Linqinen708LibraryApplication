package com.linqinen708.library.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.linqinen.library.widget.dialog.MyAlertDialog;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        new MyAlertDialog(this)
                .setTitle("说了也不听")
                .setMessage("我啥也没干")
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getBaseContext(),"网络连接异常，请稍后重试",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(null)
                .show();


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dialog:
                new MyAlertDialog(this)
                        .setTitle("说了也不听")
                        .setMessage("我啥也没干")
                        .setPositiveButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getBaseContext(),"网络连接异常，请稍后重试",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(null)
                        .show();
                break;

            default:
                break;
        }
    }
}
