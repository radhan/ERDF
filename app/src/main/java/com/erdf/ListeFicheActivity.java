package com.erdf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.erdf.adapter.FicheAdapter;
import com.erdf.adapter.RisqueAdapter;
import com.erdf.classe.technique.ConnexionBDD;
import com.erdf.classe.technique.GetResponse;
import com.erdf.classe.technique.ParserJSON;

import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;



public class ListeFicheActivity extends BaseActivity implements GetResponse {

    List<ViewFiche> lesFiches = new ArrayList<>() ;
    ListView listviewFiches ;
    ConnexionBDD oConnexion ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_fiche);

        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        //On récupère la liste des risques
        oConnexion = new ConnexionBDD("ListeFiche", ListeFicheActivity.this);
        oConnexion.getResponse = this;
        oConnexion.execute();

    }



    @Override
    public Void getData(String resultatJson) {
        if(oConnexion.isJSON()) {
            ParserJSON oParser = new ParserJSON(resultatJson) ;
            for (int i = 1; i < oParser.getOJSON().length() + 1; i++) {
                ParserJSON oFiche = new ParserJSON(oParser.getOJSON(), Integer.toString(i)) ;

                ViewFiche vFiches = new ViewFiche(oFiche.getString("cha_nrue"), oFiche.getString("cha_rue"), oFiche.getString("cha_codepo"), oFiche.getString("cha_ville"),  oFiche.getString("fic_date"));
                lesFiches.add(vFiches);
            }
        }

        if(!lesFiches.isEmpty()) {
            listviewFiches = (ListView) findViewById(R.id.listFiche);
            FicheAdapter adapter = new FicheAdapter(ListeFicheActivity.this, lesFiches);
            listviewFiches.setAdapter(adapter);
        }

        return null ;
    }



}
