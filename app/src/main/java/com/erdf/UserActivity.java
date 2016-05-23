package com.erdf;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.erdf.classe.DAO.UtilisateurDAO;
import com.erdf.classe.metier.Compte;
import com.erdf.classe.metier.Fonction;
import com.erdf.classe.metier.Utilisateur;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserActivity extends BaseActivity {


    @InjectView(R.id.iNom) TextView nomText ;
    @InjectView(R.id.iPrenom) TextView prenomText ;
    @InjectView(R.id.iEmail) TextView emailText ;
    @InjectView(R.id.iMdp) TextView mdpText ;
    @InjectView(R.id.chxAdm) CheckBox chxAdmin ;
    @InjectView(R.id.button) Button btnEnvoyer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        //Configuration button d'envoie
        btnEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUser();
            }
        });


        }






    public void setUser() {

        //Creation de la fonction de l'utilisateur
        Fonction uneFonction = new Fonction();

        //On verfie le niveau du compte
        if(chxAdmin.isChecked())
        {
            uneFonction.setId("1");
        }
        else
        {
            uneFonction.setId("2");
        }


        // Creation de l'utilisateur
        Utilisateur user = new Utilisateur();
        user.setNom(nomText.getText().toString());
        user.setPrenom(prenomText.getText().toString());
        user.setMail(emailText.getText().toString());
        user.setUneFonction(uneFonction);

        //Creation du compte de l'utilisateur
        Compte compte = new Compte();
        compte.setPassword(mdpText.getText().toString());

        //Insert de compte dans l'utilisateur
        user.setUnCompte(compte);

        UtilisateurDAO unUserDAO =  new UtilisateurDAO() ;
        unUserDAO.setUnUserTest(this, user);

    }

}
