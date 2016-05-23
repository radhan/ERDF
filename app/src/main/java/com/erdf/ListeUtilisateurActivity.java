package com.erdf;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.erdf.classe.adapter.UtilisateurAdapter;
import com.erdf.classe.DAO.UtilisateurDAO;
import com.erdf.classe.metier.Utilisateur;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ListeUtilisateurActivity extends BaseActivity implements AdapterView.OnItemClickListener  {

    @InjectView(R.id.listeUti) ListView listviewUtilisateurs ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_utilisateur);

        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        //On récupère la liste des utilisateurs
        getListeUtilisateurs() ;
    }

    public void onItemClick(AdapterView parentView, View childView, int position, long id) {

    }

    private void getListeUtilisateurs() {
        //On récupère la liste des utilisateurs
        ArrayList<Utilisateur> listeUtilisateur = UtilisateurDAO.getListeUtilisateur(getApplicationContext()) ;

        if (!listeUtilisateur.isEmpty()) {
            UtilisateurAdapter adapter = new UtilisateurAdapter(ListeUtilisateurActivity.this, listeUtilisateur);
            listviewUtilisateurs.setAdapter(adapter);
            listviewUtilisateurs.setOnItemClickListener(ListeUtilisateurActivity.this);
        }
    }
}
