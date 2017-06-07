package cn.pantiy.myroster.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutRes());
        init();
    }

    protected void init() {
        initData();
        initViews();
        setupAdapter();
        setupListener();
    }

    protected abstract void initData();

    protected abstract void initViews();

    protected abstract void setupAdapter();

    protected abstract void setupListener();

    protected abstract int setLayoutRes();

}
