package com.erdf;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import com.erdf.adapter.FicheAdapter;
import com.erdf.classe.DAO.FicheDAO;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;



public class ListeFicheActivity extends BaseActivity {

    List<ViewFiche> lesFiches = new ArrayList<>() ;
    ListView listviewFiches ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_fiche);

        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        //On récupère la liste des risques
        getListeFiches() ;
    }


    private void getListeFiches() {
        final FicheDAO uneFicheDAO = new FicheDAO(this) ;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                for (int i = 0; i < uneFicheDAO.getListeFiche().size(); i++) {
                    ViewFiche vFiches = new ViewFiche(uneFicheDAO.getListeFiche().get(i).getUnChantier().getNumRue(), uneFicheDAO.getListeFiche().get(i).getUnChantier().getRue(), uneFicheDAO.getListeFiche().get(i).getUnChantier().getCodePostal(), uneFicheDAO.getListeFiche().get(i).getUnChantier().getVille(),  uneFicheDAO.getListeFiche().get(i).getDate());
                    lesFiches.add(vFiches);
                    Log.i("TestDAO", uneFicheDAO.getListeFiche().get(i).getDate());
                }
                if(!lesFiches.isEmpty()) {
                    listviewFiches = (ListView) findViewById(R.id.listFiche);
                    FicheAdapter adapter = new FicheAdapter(ListeFicheActivity.this, lesFiches);
                    listviewFiches.setAdapter(adapter);
                }
            }
        }, 1000);
    }
}
