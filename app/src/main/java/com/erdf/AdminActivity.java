package com.erdf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AdminActivity extends AppCompatActivity {

    @InjectView(R.id.button4) Button deconnexionBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        deconnexionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences connexionPref = getSharedPreferences("connexion", 0);
                connexionPref.edit().clear().apply();
                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
