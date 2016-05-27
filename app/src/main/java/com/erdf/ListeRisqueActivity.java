package com.erdf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.erdf.classe.DAO.RisqueDAO;
import com.erdf.classe.adapter.RisqueNcAdapter;
import com.erdf.classe.metier.Risque;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ListeRisqueActivity extends BaseActivity {

    @InjectView(R.id.listViewRisque) ListView listviewRisque ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_risque);

        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        //On synchronise la bdd interne avec la bdd en ligne
        RisqueDAO.syncGetListeRisque(this) ;

        //On récupère la liste des risques
        getListeRisque() ;
    }



    private void getListeRisque() {
        //On récupère la liste des risques
        ArrayList<Risque> listeRisque = RisqueDAO.getListeRisque(this, true) ;

        if (!listeRisque.isEmpty()) {
            RisqueNcAdapter adapter = new RisqueNcAdapter(ListeRisqueActivity.this, listeRisque) ;
            listviewRisque.setAdapter(adapter) ;
        }
    }



}
