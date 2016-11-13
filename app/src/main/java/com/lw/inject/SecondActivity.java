package com.lw.inject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lw.BindView;
import com.lw.injectview.InjectView;
import com.lw.iocsample.R;

/**
 * @author lw
 *         <p>
 *         编译时注解的核心原理依赖APT(Annotation Processing Tools)实现.
 *         编译时Annotation解析的基本原理是:在某些代码元素上（如类型、函数、字段等）添加注解，
 *         在编译时编译器会检查AbstractProcessor的子类，并且调用该类型的process函数，
 *         然后将添加了注解的所有元素都传递到process函数中，使得开发人员可以在编译器进行相应的处理，
 *         例如，根据注解生成新的Java类，这也就是ButterKnife等开源库的基本原理。
 */
public class SecondActivity extends AppCompatActivity {
    @BindView(R.id.tv)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        InjectView.bind(this);
        textView.setText("我是通过编译时注解设置的TextView");
    }
}
