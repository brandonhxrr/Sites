package com.foxware.sites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Finish extends AppCompatActivity{

    TextView finish_msg;
    String site_name;
    Button finisht_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_finish);

        site_name = getIntent().getStringExtra("site_name");
        finish_msg = (TextView) findViewById(R.id.finish2);
        finisht_btn = (Button) findViewById(R.id.btn_end);
        finish_msg.setText(site_name+ " "+ getString(R.string.finish2));

        finisht_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }


}
