package ru.bda.icrm.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import ru.bda.icrm.R;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.fragment.PriceFragment;

/**
 * Created by User on 20.12.2016.
 */

public class ChildPriceActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_price);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Прайс");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String code = "";
        if (getIntent()!= null) {
            code = getIntent().getStringExtra(Constants.INTENT_PARENT_CODE);
            Log.d("log-child", code);
        }
        setFragment(code);
    }

    private void setFragment(String code) {
        PriceFragment fragment = new PriceFragment();
        fragment.setParentCode(code);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_content, fragment).commit();
    }
}
