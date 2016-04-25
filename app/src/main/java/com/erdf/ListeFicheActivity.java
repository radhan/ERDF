package com.erdf;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.erdf.adapter.FicheAdapter;
import com.erdf.classe.DAO.FicheDAO;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ListeFicheActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    List<ViewFiche> lesFiches = new ArrayList<>() ;
    FicheDAO uneFicheDAO ;
    @InjectView(R.id.listFiche) ListView listviewFiches ;

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
        uneFicheDAO = new FicheDAO(this) ;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                for (int i = 0; i < uneFicheDAO.getListeFiche().size(); i++) {
                    ViewFiche vFiches = new ViewFiche(uneFicheDAO.getListeFiche().get(i).getUnChantier().getNumRue(), uneFicheDAO.getListeFiche().get(i).getUnChantier().getRue(), uneFicheDAO.getListeFiche().get(i).getUnChantier().getCodePostal(), uneFicheDAO.getListeFiche().get(i).getUnChantier().getVille(), uneFicheDAO.getListeFiche().get(i).getDate());
                    lesFiches.add(vFiches);
                }
                if (!lesFiches.isEmpty()) {
                    FicheAdapter adapter = new FicheAdapter(ListeFicheActivity.this, lesFiches);
                    listviewFiches.setAdapter(adapter);
                    listviewFiches.setOnItemClickListener(ListeFicheActivity.this);
                }
            }
        }, 1000);
    }

    public void onItemClick(AdapterView parentView, View childView, int position, long id) {
        //On créé un objet Bundle, c'est ce qui va nous permetre d'envoyer des données à l'autre Activity
        Bundle objetBdl = new Bundle();

        String adresse = uneFicheDAO.getListeFiche().get(position).getUnChantier().getNumRue() + " " + uneFicheDAO.getListeFiche().get(position).getUnChantier().getRue() + ", " + uneFicheDAO.getListeFiche().get(position).getUnChantier().getVille() + ", " + uneFicheDAO.getListeFiche().get(position).getUnChantier().getCodePostal() ;
        String technicien = uneFicheDAO.getListeFiche().get(position).getUnUtilisateur().getNom() + " " + uneFicheDAO.getListeFiche().get(position).getUnUtilisateur().getPrenom() ;

        //Cela fonctionne plus ou moins comme une HashMap, on entre une clef et sa valeur en face
        objetBdl.putString("dateFiche", uneFicheDAO.getListeFiche().get(position).getDate());
        objetBdl.putString("adresseFiche", adresse);
        objetBdl.putString("technicienFiche", technicien);

        //On créé l'Intent qui va nous permettre d'afficher l'autre Activity
        Intent intent = new Intent(ListeFicheActivity.this, FicheDetailActivity.class);

        //On affecte à l'Intent le Bundle que l'on a créé
        intent.putExtras(objetBdl);

        //On démarre l'autre Activity
        startActivity(intent);
    }
}
