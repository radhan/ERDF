package com.erdf;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.erdf.classe.technique.ConnexionBDD;
import com.erdf.classe.technique.GetResponse;

import java.util.ArrayList;

public class LoginActivity extends Activity implements GetResponse {


    private static final int REQUEST_SIGNUP = 0;

    //Variable qui sert à voir si la connexion est OK ou non
    String statutConnexion = null ;
    ConnexionBDD oConnexion ;

    TextView login;
    TextView pass;
    Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (TextView)findViewById(R.id.email);
        pass = (TextView)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.loginButton);


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

        ArrayList<String> nomParams = new ArrayList<>() ;
        nomParams.add("login") ;
        nomParams.add("password") ;

        ArrayList<String> valeurParams = new ArrayList<>() ;
        valeurParams.add((String)login.getText()) ;
        valeurParams.add((String)pass.getText()) ;

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

        String slogin = (String) login.getText();
        String spass = (String) pass.getText();

        if (slogin.isEmpty()) {
            login.setError("Entrer un nom d'utilisateur valide");
            valid = false;
        } else {
            login.setError(null);
        }

        if (spass.isEmpty() || spass.length() < 4) {
            pass.setError("Entrer un mot de passe ayant au moins 4 caractères alphanumériques");
            valid = false;
        } else {
            pass.setError(null);
        }

        return valid;
    }

    @Override
    public Void getData(String resultatJson) {
        return null;
    }
}