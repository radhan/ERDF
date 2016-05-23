package com.erdf.classe.DAO;

import android.app.Activity;

import com.erdf.classe.metier.Utilisateur;
import com.erdf.classe.technique.ConnexionBDD;
import com.erdf.classe.technique.GetResponse;

import java.util.ArrayList;

/**
 * Created by Alexandre on 03/05/2016.
 */
public class UtilisateurDAO implements GetResponse {

    ConnexionBDD oConnexion;
    private String statusJson ;

    public UtilisateurDAO() {
    }



    public void setUtilisateur(Activity pActivity, Utilisateur unUser) {

        ArrayList<String> nomParams = new ArrayList<>() ;
        nomParams.add("nom") ;
        nomParams.add("prenom") ;
        nomParams.add("mail") ;
        nomParams.add("fonction");
        nomParams.add("password");

        ArrayList<String> valeurParams = new ArrayList<>() ;
        valeurParams.add(unUser.getNom()) ;
        valeurParams.add(unUser.getPrenom()) ;
        valeurParams.add(unUser.getMail());
        valeurParams.add(unUser.getUneFonction().getId()) ;
        valeurParams.add(unUser.getUnCompte().getPassword()) ;

        oConnexion = new ConnexionBDD(nomParams, valeurParams, "SaisirUtilisateur", pActivity);
        oConnexion.getResponse = this ;
        oConnexion.execute() ;

        this.statusJson = "Envoyer" ;
    }



    @Override
    public Void getData(String resultatJson) {
        return null;
    }
}