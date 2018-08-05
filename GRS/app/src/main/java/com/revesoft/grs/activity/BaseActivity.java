package com.revesoft.grs.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.revesoft.grs.R;
import com.revesoft.grs.util.Constant;


/**
 * Created by sajid on 10/28/2015.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        setContentView();
        setupActionBar();
    }

    protected void setContentView() {

    }


    protected void setupActionBar() {
        Log.d(TAG, "setupActionBar()");
        if (getSupportActionBar() != null) {
            if (Constant.KEY_IS_DEVELOPMENT_BUILD){
                getSupportActionBar().setLogo(R.drawable.splashscreen);
            }else {
                getSupportActionBar().setLogo(R.drawable.splashscreen);
            }

            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
          //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);


        }
    }

}
