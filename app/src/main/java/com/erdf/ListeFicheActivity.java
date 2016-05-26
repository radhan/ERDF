package com.erdf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.erdf.classe.adapter.FicheAdapter;
import com.erdf.classe.DAO.FicheDAO;
import com.erdf.classe.metier.Chantier;
import com.erdf.classe.metier.Fiche;
import com.erdf.classe.metier.Risque;
import com.erdf.classe.metier.Utilisateur;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ListeFicheActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.listFiche)     ListView listviewFiches ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_liste_fiche) ;

        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        //On synchronise la bdd interne avec la bdd en ligne
        FicheDAO.syncGetListeFiche(this) ;

        //On récupère la liste des risques
        getListeFiches() ;
    }

    private void getListeFiches() {
        //On récupère la liste de fiche
        ArrayList<Fiche> listeFiche = FicheDAO.getListeFiche(this, true) ;

        if (!listeFiche.isEmpty()) {
            FicheAdapter adapter = new FicheAdapter(ListeFicheActivity.this, listeFiche) ;
            listviewFiches.setAdapter(adapter) ;
            listviewFiches.setOnItemClickListener(ListeFicheActivity.this) ;
        }
    }

    public void onItemClick(AdapterView parentView, View childView, int position, long id) {
        ArrayList<Fiche> listeFiche = FicheDAO.getListeFiche(this, true)        ;
        Chantier unChantier = listeFiche.get(position).getUnChantier()          ;
        Utilisateur unUtilisateur = listeFiche.get(position).getUnUtilisateur() ;

        //On créé un objet Bundle, c'est ce qui va nous permetre d'envoyer des données à l'autre Activity
        Bundle objetBdl = new Bundle() ;

        String adresse = unChantier.getNumRue() + " " + unChantier.getRue() + ", " + unChantier.getVille() + ", " + unChantier.getCodePostal() ;
        String technicien = unUtilisateur.getNom() + " " + unUtilisateur.getPrenom() ;

        ArrayList<Risque> listeRisque = listeFiche.get(position).getListeRisque()   ;
        ArrayList<String> titre = new ArrayList<>()                                 ;
        ArrayList<String> soustitre =  new ArrayList<>()                            ;

        for(Risque unRisque : listeRisque) {
            titre.add(unRisque.getTitre())      ;
            soustitre.add(unRisque.getResume()) ;
        }

        Log.i("ListeFiche", "Taille : " + listeRisque.size()) ;

        //Cela fonctionne plus ou moins comme une HashMap, on entre une clef et sa valeur en face
        objetBdl.putString("dateFiche", listeFiche.get(position).getDate()) ;
        objetBdl.putString("adresseFiche", adresse)                         ;
        objetBdl.putString("technicienFiche", technicien)                   ;
        objetBdl.putStringArrayList("titre", titre)                         ;
        objetBdl.putStringArrayList("soustitre", soustitre)                 ;

        //On créé l'Intent qui va nous permettre d'afficher l'autre Activity
        Intent intent = new Intent(ListeFicheActivity.this, FicheDetailActivity.class) ;

        //On affecte à l'Intent le Bundle que l'on a créé
        intent.putExtras(objetBdl) ;

        //On démarre l'autre Activity
        startActivity(intent) ;
    }
}
