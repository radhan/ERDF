package com.erdf.classe.DAO;

import android.app.Activity;
import android.util.Log;

import com.erdf.classe.metier.Chantier;
import com.erdf.classe.metier.Fiche;
import com.erdf.classe.metier.Utilisateur;
import com.erdf.classe.technique.ConnexionBDD;
import com.erdf.classe.technique.GetResponse;
import com.erdf.classe.technique.ParserJSON;

import java.util.ArrayList;

/**
 * Created by Radhan on 24/04/2016.
 */
public class FicheDAO implements GetResponse {
    private ArrayList<Fiche> listeFiche ;
    ConnexionBDD oConnexion ;

    public FicheDAO(Activity pActivite) {
        oConnexion = new ConnexionBDD("Fiche", pActivite);
        oConnexion.getResponse = this ;
        oConnexion.execute() ;
    }

    public ArrayList<Fiche> getListeFiche() {
        return listeFiche ;
    }

    public void addFiche(Fiche uneFiche) {
        if(this.listeFiche == null) {
            this.listeFiche = new ArrayList<>() ;
        }
        this.listeFiche.add(uneFiche) ;
        Log.i("addFiche", "Une fiche ajoutée") ;
    }

    @Override
    public Void getData(String resultatJson) {

        if(oConnexion.isJSON()) {
            ParserJSON oParser = new ParserJSON(resultatJson) ;
            for (int i = 1; i < oParser.getOJSON().length() + 1; i++) {
                ParserJSON oFiche = new ParserJSON(oParser.getOJSON(), Integer.toString(i)) ;

                //On déclare l'objet chantier
                Chantier unChantier = new Chantier() ;
                unChantier.setNumRue(oFiche.getString("cha_nrue"));
                unChantier.setRue(oFiche.getString("cha_rue"));
                unChantier.setCodePostal(oFiche.getString("cha_codepo"));
                unChantier.setVille(oFiche.getString("cha_ville"));

                //On déclare l'objet utilisateur
                Utilisateur unUtilisateur = new Utilisateur() ;

                Fiche uneFiche = new Fiche(unChantier, unUtilisateur, oFiche.getString("fic_date")) ;
                addFiche(uneFiche) ;
            }
        }

        return null ;
    }
}
