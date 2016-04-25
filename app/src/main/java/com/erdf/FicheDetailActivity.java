package com.erdf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.erdf.classe.technique.ConnexionBDD;
import com.erdf.classe.technique.GetResponse;
import com.erdf.classe.technique.ParserJSON;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FicheDetailActivity extends BaseActivity {


    ArrayList<String> nomparam = new ArrayList<>() ;
    ArrayList<String> value = new ArrayList<>() ;


    @InjectView(R.id.tDate) TextView dateText ;
    @InjectView(R.id.tAdresse) TextView adresseText ;
    @InjectView(R.id.tTechnicien) TextView technicienText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_detail);

        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        //On récupère l'objet Bundle envoyé par l'autre Activity
        Bundle objetBdl  = this.getIntent().getExtras();

        //On récupère les données du Bundle
        if (objetBdl != null && objetBdl.containsKey("dateFiche") && objetBdl.containsKey("adresseFiche") && objetBdl.containsKey("technicienFiche")) {
            //On récupère la liste des risques
            dateText.setText("Date : " + this.getIntent().getStringExtra("dateFiche"));
            adresseText.setText("Adresse : " + this.getIntent().getStringExtra("adresseFiche"));
            technicienText.setText("Technicien : " + this.getIntent().getStringExtra("technicienFiche"));
        }
    }

}
