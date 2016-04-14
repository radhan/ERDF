package com.erdf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.erdf.adapter.RisqueAdapter;
import com.erdf.classe.technique.ConnexionBDD;
import com.erdf.classe.technique.GetResponse;
import com.erdf.classe.technique.InternetDetection;
import com.erdf.classe.technique.ParserJSON;

import java.util.ArrayList;
import java.util.List;

public class FicheActivity extends AppCompatActivity implements GetResponse {

    List<ViewRisque> lesRisques = new ArrayList<>() ;
    ListView listviewRisque ;

    ConnexionBDD oConnexion ;
    private EditText AdressInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche);

        //Configuration button de renvoie
        Button btnEnvoyer = (Button) findViewById(R.id.button) ;
        btnEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        oConnexion = new ConnexionBDD("Risque", FicheActivity.this);
        oConnexion.getResponse = FicheActivity.this;
        oConnexion.execute();

        InternetDetection inter = new InternetDetection(getApplicationContext());

        Boolean isInternetPresent = inter.isConnectingToInternet(); // true or false

        AdressInput = (EditText) findViewById(R.id.iAdress);

        if(isInternetPresent)
        {
            AdressInput.setVisibility(View.GONE);

        }
        else {

            AdressInput.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public Void getData(String resultatJson) {
        if(oConnexion.isJSON()) {
            ParserJSON oParser = new ParserJSON(resultatJson) ;
            for (int i = 1; i < oParser.getOJSON().length() + 1; i++) {
                ParserJSON oFiche = new ParserJSON(oParser.getOJSON(), Integer.toString(i)) ;

                ViewRisque vRisque = new ViewRisque(oFiche.getString("ris_titre"), oFiche.getString("ris_resume"));
                lesRisques.add(vRisque);
            }
        }

        if(!lesRisques.isEmpty()) {
            listviewRisque = (ListView) findViewById(R.id.listView);
            RisqueAdapter adapter = new RisqueAdapter(FicheActivity.this, lesRisques);
            listviewRisque.setAdapter(adapter);

            listviewRisque.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    if (view != null) {
                        CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkbox);
                        checkBox.setChecked(!checkBox.isChecked());
                    }
                }
            });

        }

        return null ;
    }
}
