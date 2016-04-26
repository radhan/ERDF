package com.erdf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.erdf.adapter.FicheAdapter;
import com.erdf.adapter.RisqueNcAdapter;
import com.erdf.classe.technique.ConnexionBDD;
import com.erdf.classe.technique.GetResponse;
import com.erdf.classe.technique.ParserJSON;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FicheDetailActivity extends BaseActivity {


    ArrayList<String> nomparam = new ArrayList<>() ;
    ArrayList<String> value = new ArrayList<>() ;
    @InjectView(R.id.listRisque) ListView listviewRisque ;


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

        ArrayList<String> titre = this.getIntent().getStringArrayListExtra("titre");
        ArrayList<String> soustitre = this.getIntent().getStringArrayListExtra("soustitre");
        List<ViewRisqueNc> lesRisques = new ArrayList<>() ;


    /*    for (int i = 0; i < titre.size(); i++) {
            ViewRisqueNc vRisque = new ViewRisqueNc( titre.get(i), soustitre.get(i));
            lesRisques.add(vRisque);
        }*/

        for (int i = 0; i < 7; i++) {
            ViewRisqueNc vRisque = new ViewRisqueNc( "Risque routier", "quand tu conduis !");
            lesRisques.add(vRisque);
        }

        if (!lesRisques.isEmpty()) {
            RisqueNcAdapter adapter = new RisqueNcAdapter( FicheDetailActivity.this, lesRisques);
            listviewRisque.setAdapter(adapter);
        }
    }








}
