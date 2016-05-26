package com.erdf;

import android.os.Bundle;
import android.widget.TextView;

import com.erdf.classe.DAO.UtilisateurDAO;
import com.erdf.classe.metier.Utilisateur;
import com.erdf.classe.technique.SessionManager;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AccueilActivity extends BaseActivity {

    @InjectView(R.id.tBienvenue)    TextView bienvenueText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_accueil) ;

        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        SessionManager session = new SessionManager(this) ;
        Utilisateur unUtilisateur = UtilisateurDAO.getUtilisateur(this, session.getIdUtilisateur()) ;

        bienvenueText.setText("Bienvenue " + unUtilisateur.getPrenom() + " " + unUtilisateur.getNom() + " !") ;
    }
}
