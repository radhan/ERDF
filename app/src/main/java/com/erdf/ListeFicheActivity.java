package com.erdf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.erdf.adapter.FicheAdapter;
import com.erdf.classe.DAO.ChantierDAO;
import com.erdf.classe.DAO.FicheDAO;
import com.erdf.classe.DAO.UtilisateurDAO;
import com.erdf.classe.metier.Chantier;
import com.erdf.classe.metier.Fiche;
import com.erdf.classe.metier.Risque;
import com.erdf.classe.metier.Utilisateur;

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
        //On récupère la liste de fiche
        ArrayList<Fiche> listeFiche = FicheDAO.getListeFiche(getApplicationContext()) ;

        for(Fiche uneFiche : listeFiche) {
            //Pour chaque fiches on récupère le chantier
            Chantier unChantier = ChantierDAO.getUnChantier(getApplicationContext(), uneFiche.getUnChantier().getCode()) ;

            Log.i("ListeFicheAct", "Code : " + unChantier.getCode()) ;
            ViewFiche vFiches = new ViewFiche(unChantier.getNumRue(), unChantier.getRue(), unChantier.getCodePostal(), unChantier.getVille(), uneFiche.getDate()) ;
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
        Chantier unChantier = ChantierDAO.getUnChantier(getApplicationContext(), listeFiche.get(position).getId()) ;
        Utilisateur unUtilisateur = UtilisateurDAO.getUnUtilisateur(getApplicationContext(), listeFiche.get(position).getId()) ;

        //On créé un objet Bundle, c'est ce qui va nous permetre d'envoyer des données à l'autre Activity
        Bundle objetBdl = new Bundle();

        String adresse = unChantier.getNumRue() + " " + unChantier.getRue() + ", " + unChantier.getVille() + ", " + unChantier.getCodePostal() ;
        String technicien = unUtilisateur.getNom() + " " + unUtilisateur.getPrenom() ;

        ArrayList<Risque> listeRisque = listeFiche.get(position).getListeRisque();
        ArrayList<String> titre = new ArrayList<>() ;
        ArrayList<String> soustitre =  new ArrayList<>();

        for (Risque unRisque : listeRisque) {
            titre.add(unRisque.getTitre());
            soustitre.add(unRisque.getResume());
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
