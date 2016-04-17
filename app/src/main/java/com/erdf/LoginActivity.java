package com.erdf;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.erdf.classe.technique.ConnexionBDD;
import com.erdf.classe.technique.GetResponse;
import com.erdf.classe.technique.ParserJSON;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends Activity implements GetResponse {


    @InjectView(R.id.email) EditText userText ;
    @InjectView(R.id.password) EditText passwordText ;
    @InjectView(R.id.loginButton) Button loginButton ;

    //Variable qui sert à voir si la connexion est OK ou non
    String statutConnexion = null ;
    ConnexionBDD oConnexion ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Préférences de l'application - Utilisateur déjà connecté ou non
        SharedPreferences connexionPref = getSharedPreferences("connexion", 0);
        SharedPreferences.Editor ed = connexionPref.edit();
        if(!connexionPref.contains("statut")){
            ed.putBoolean("statut", false);
            ed.apply();
        } else if(connexionPref.getBoolean("statut", false)) {

            if(connexionPref.getString("fonctionUtilisateur", "Inconnu").equals("Administrateur")) {
                //Intent intent = new Intent(this, AdminActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.i("LoginActivity", "Connexion Admin - OK") ;
                //startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, FicheActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.i("LoginActivity", "Connexion - OK") ;
                startActivity(intent);
                finish();
            }
        }

        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }


    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        String user = userText.getText().toString();
        String password = passwordText.getText().toString();

        ArrayList<String> nomParams = new ArrayList<>() ;
        nomParams.add("login") ;
        nomParams.add("password") ;

        ArrayList<String> valeurParams = new ArrayList<>() ;
        valeurParams.add(user) ;
        valeurParams.add(password) ;

        oConnexion = new ConnexionBDD(nomParams, valeurParams, "Connexion", LoginActivity.this);
        oConnexion.getResponse = this;
        oConnexion.execute();
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);

        Intent intent = new Intent(this, FicheActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

        Toast.makeText(getBaseContext(), "Vous êtes connecté !", Toast.LENGTH_LONG).show();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Identifiants incorrect !", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String user = userText.getText().toString();
        String password = passwordText.getText().toString();

        if (user.isEmpty()) {
            userText.setError("Entrer un nom d'utilisateur valide");
            valid = false;
        } else {
            userText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            passwordText.setError("Entrer un mot de passe ayant au moins 4 caractères alphanumériques");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public Void getData(String resultatJson) {
        if(oConnexion.isJSON()) {
            statutConnexion = "OK" ;
            ParserJSON oParser = new ParserJSON(resultatJson);

            //Préférences de l'application - Utilisateur déjà connecté ou non
            SharedPreferences connexionPref = getSharedPreferences("connexion", 0);
            SharedPreferences.Editor ed;
            ed = connexionPref.edit();
            ed.putBoolean("statut", true);
            ed.putInt("idUtilisateur", oParser.getInt("com_id"));
            ed.putString("nomUtilisateur", oParser.getString("use_nom"));
            ed.putString("prenomUtilisateur", oParser.getString("use_prenom"));
            ed.putString("emailUtilisateur", oParser.getString("use_email"));
            ed.putString("fonctionUtilisateur", oParser.getString("fon_libelle"));

            ed.apply();
        }
        if (statutConnexion != null && statutConnexion.contains("OK")) {
            onLoginSuccess();
        } else {
            onLoginFailed();
        }

        return null;
    }
}