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
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FicheDetailActivity extends AppCompatActivity {


    ArrayList<String> nomparam = new ArrayList<>() ;
    ArrayList<String> value = new ArrayList<>() ;


    @InjectView(R.id.tDate) TextView dateText ;
    @InjectView(R.id.tAdresse) TextView adresseText ;
    @InjectView(R.id.tTechnicien) TextView technicienText ;

    ConnexionBDD oConnexion ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_detail);

        nomparam.add("id");
        value.add("1");



        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        //On récupère la liste des risques
        oConnexion = new ConnexionBDD( nomparam, value, "InfoFiche", FicheDetailActivity.this);
        oConnexion.getResponse = (GetResponse) this;
        oConnexion.execute();

    }



    public Void getData(String resultatJson) {
        if(oConnexion.isJSON()) {
            ParserJSON oParser = new ParserJSON(resultatJson) ;
            for (int i = 1; i < oParser.getOJSON().length() + 1; i++) {
                ParserJSON oFiche = new ParserJSON(oParser.getOJSON(), Integer.toString(i)) ;

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                dateText.setText("Date : " + dateFormat.format(oFiche.getString("fic_date")));
                adresseText.setText("Adresse : " + oFiche.getString("cha_nrue") + " " + oFiche.getString("cha_rue") + " " + oFiche.getString("chacodepo") + " " + oFiche.getString("cha_ville"));
                technicienText.setText("Technicien : " + oFiche.getString("use_nom") + " " + oFiche.getString("use_prenom"));

            }
        }

        return null ;
    }
}
