package com.erdf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.erdf.classe.DAO.RisqueDAO;
import com.erdf.classe.metier.Risque;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RisqueActivity extends BaseActivity {


    @InjectView(R.id.iTitrer) TextView titreText        ;
    @InjectView(R.id.iSsTitre)   TextView ssTitreText     ;
    @InjectView(R.id.button) Button btnEnvoyer       ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risque);


        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        //Configuration button d'envoie
        btnEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRisque();
            }
        });
    }


    public void setRisque() {

        // Creation du risque
        Risque risque = new Risque()            ;
        risque.setTitre(titreText.getText().toString());
        risque.setResume(ssTitreText.getText().toString());
        RisqueDAO.addRisque(this, risque, true); ;
    }
}
