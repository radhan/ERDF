package com.erdf;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AccueilActivity extends BaseActivity {

    @InjectView(R.id.tBienvenue) TextView bienvenueText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        SharedPreferences connexionPref = getSharedPreferences("connexion", 0);
        bienvenueText.setText("Bienvenue " + connexionPref.getString("prenomUtilisateur", "Inconnu") + " " + connexionPref.getString("nomUtilisateur", "Inconnu") + " !") ;
    }
}
