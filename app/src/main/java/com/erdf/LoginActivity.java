package com.erdf;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.erdf.classe.DAO.ChantierDAO;
import com.erdf.classe.DAO.CompteDAO;
import com.erdf.classe.DAO.FicheDAO;
import com.erdf.classe.DAO.FicheRisqueDAO;
import com.erdf.classe.DAO.FonctionDAO;
import com.erdf.classe.DAO.RisqueDAO;
import com.erdf.classe.DAO.UtilisateurDAO;
import com.erdf.classe.technique.SessionManager;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends Activity {


    @InjectView(R.id.email) EditText userText;
    @InjectView(R.id.password) EditText passwordText;
    @InjectView(R.id.loginButton) Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Préférences de l'application - Utilisateur déjà connecté ou non
        SessionManager session = new SessionManager(getApplicationContext());
        if(session.isConnecte()) {
            Intent intent = new Intent(this, AccueilActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Log.i("LoginActivity", "Connexion - OK");
            startActivity(intent);
            finish();
        }

        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this);

        //On synchronise la bdd interne avec la bdd en ligne
        FonctionDAO.syncGetListeFonction(this);
        UtilisateurDAO.syncGetListeUtilisateur(this);
        ChantierDAO.syncGetListeChantier(this);
        RisqueDAO.syncGetListeRisque(this);
        FicheDAO.syncGetListeFiche(this);
        FicheRisqueDAO.syncGetListeFicheRisque(this) ;

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }


    public void login() {

        if (!validate()) {
            return;
        }

        String user = userText.getText().toString();
        String password = passwordText.getText().toString();

        CompteDAO.Connexion(getApplicationContext(), user, password);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public boolean validate() {
        boolean valid = true;

        String user = userText.getText().toString();
        String password = passwordText.getText().toString();

        if (user.isEmpty()) {
            userText.setError("Entrer un nom d'utilisateur valide");
            valid = false;
        }
        else {
            userText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            passwordText.setError("Entrer un mot de passe ayant au moins 4 caractères alphanumériques");
            valid = false;
        }
        else {
            passwordText.setError(null);
        }

        return valid;
    }
}