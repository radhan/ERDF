package com.erdf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.erdf.adapter.FicheAdapter;
import com.erdf.classe.DAO.FicheDAO;
import com.erdf.classe.metier.Fiche;
import com.erdf.classe.metier.Risque;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ListeFicheActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    List<ViewFiche> lesFiches = new ArrayList<>() ;
    @InjectView(R.id.listFiche) ListView listviewFiches ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_fiche);

        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        //On synchronise la bdd interne avec la bdd en ligne
        FicheDAO.syncGetListeFiche(getApplicationContext()) ;

        //On récupère la liste des risques
        getListeFiches() ;
    }


    private void getListeFiches() {
        ArrayList<Fiche> listeFiche = FicheDAO.getListeFiche(getApplicationContext()) ;

        for (int i = 0; i < listeFiche.size(); i++) {
            ViewFiche vFiches = new ViewFiche(listeFiche.get(i).getUnChantier().getNumRue(), listeFiche.get(i).getUnChantier().getRue(), listeFiche.get(i).getUnChantier().getCodePostal(), listeFiche.get(i).getUnChantier().getVille(), listeFiche.get(i).getDate());
            lesFiches.add(vFiches);
        }
        if (!lesFiches.isEmpty()) {
            FicheAdapter adapter = new FicheAdapter(ListeFicheActivity.this, lesFiches);
            listviewFiches.setAdapter(adapter);
            listviewFiches.setOnItemClickListener(ListeFicheActivity.this);
        }
    }

    public void onItemClick(AdapterView parentView, View childView, int position, long id) {
        ArrayList<Fiche> listeFiche = FicheDAO.getListeFiche(getApplicationContext()) ;

        //On créé un objet Bundle, c'est ce qui va nous permetre d'envoyer des données à l'autre Activity
        Bundle objetBdl = new Bundle();

        String adresse = listeFiche.get(position).getUnChantier().getNumRue() + " " + listeFiche.get(position).getUnChantier().getRue() + ", " + listeFiche.get(position).getUnChantier().getVille() + ", " + listeFiche.get(position).getUnChantier().getCodePostal() ;
        String technicien = listeFiche.get(position).getUnUtilisateur().getNom() + " " + listeFiche.get(position).getUnUtilisateur().getPrenom() ;
        ArrayList<Risque> risques = listeFiche.get(position).getListeRisque();
        ArrayList<String> titre = new ArrayList<>() ;
        ArrayList<String> soustitre =  new ArrayList<>();

        for (int i = 0; i < risques.size(); i++) {

            Risque risque = risques.get(i);
            titre.add(risque.getTitre());
            soustitre.add(risque.getResume());
        }

        //Cela fonctionne plus ou moins comme une HashMap, on entre une clef et sa valeur en face
        objetBdl.putString("dateFiche", listeFiche.get(position).getDate());
        objetBdl.putString("adresseFiche", adresse);
        objetBdl.putString("technicienFiche", technicien);
        objetBdl.putStringArrayList("titre", titre);
        objetBdl.putStringArrayList("soustitre", soustitre);

        //On créé l'Intent qui va nous permettre d'afficher l'autre Activity
        Intent intent = new Intent(ListeFicheActivity.this, FicheDetailActivity.class);

        //On affecte à l'Intent le Bundle que l'on a créé
        intent.putExtras(objetBdl);

        //On démarre l'autre Activity
        startActivity(intent);
    }
}
