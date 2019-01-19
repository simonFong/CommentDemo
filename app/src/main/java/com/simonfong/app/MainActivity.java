package com.simonfong.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.simonfong.app.ImageAdd.EvaluateActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_evaluate)
    Button mBtnEvaluate;
    @BindView(R.id.btn_shop_car)
    Button mBtnShopCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_evaluate, R.id.btn_shop_car})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_evaluate:
                startActivity(new Intent(this, EvaluateActivity.class));
                break;
            case R.id.btn_shop_car:
                break;
        }
    }
}
