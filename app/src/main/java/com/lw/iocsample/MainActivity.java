package com.lw.iocsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lw.inject.SecondActivity;
import com.lw.iocsample.event.OnClick;

/**
 * Android注解框架原理
 */

@ContentView(value = R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.btn_toast)
    private Button btn;

    @OnClick({R.id.btn_toast})
    public void clickBtnInvoked(View view) {
        switch (view.getId()) {
            case R.id.btn_toast:
                Toast.makeText(this, "我是按钮1", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, SecondActivity.class));
                break;
            case R.id.btn2_toast:
                Toast.makeText(this, "我是按钮2", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjectUtils.inject(this);
    }
}
