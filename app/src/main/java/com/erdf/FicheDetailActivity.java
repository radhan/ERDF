package com.erdf;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.erdf.classe.adapter.RisqueNcAdapter;
import com.erdf.classe.metier.Risque;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FicheDetailActivity extends BaseActivity {


    @InjectView(R.id.listeRisque) ListView listviewRisque ;
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
            dateText.setText("Date : " + this.getIntent().getStringExtra("dateFiche"));
            adresseText.setText("Adresse : " + this.getIntent().getStringExtra("adresseFiche"));
            technicienText.setText("Technicien : " + this.getIntent().getStringExtra("technicienFiche"));
        }

        //On récupère la liste des risques
        ArrayList<String> titre = this.getIntent().getStringArrayListExtra("titre");
        ArrayList<String> soustitre = this.getIntent().getStringArrayListExtra("soustitre");
        ArrayList<Risque> lesRisques = new ArrayList<>() ;

        for (int i = 0; i < titre.size(); i++) {
            Risque unRisque = new Risque() ;
            unRisque.setTitre(titre.get(i)) ;
            unRisque.setResume(soustitre.get(i)) ;
            lesRisques.add(unRisque);
        }

        if(!lesRisques.isEmpty()) {
            for (Risque unRisque : lesRisques) {
                Log.i("FicheDetail", unRisque.getTitre());
            }
        }
        else {
            Log.i("FicheDetail", "Liste des risques vide");
        }

        if (!lesRisques.isEmpty()) {
            RisqueNcAdapter adapter = new RisqueNcAdapter(FicheDetailActivity.this, lesRisques);
            listviewRisque.setAdapter(adapter);
        }
    }

}
