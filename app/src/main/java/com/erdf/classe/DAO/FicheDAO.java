package com.erdf.classe.DAO;

import android.app.Activity;

import com.erdf.classe.metier.Chantier;
import com.erdf.classe.metier.Fiche;
import com.erdf.classe.metier.Fonction;
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
    private Activity activite ;
    ConnexionBDD oConnexion, oConnexion1 ;
    private String statusJson ;

    public FicheDAO(Activity pActivite) {
        this.activite = pActivite ;
            oConnexion = new ConnexionBDD("Fiche", this.activite);
            oConnexion.getResponse = this;
            oConnexion.execute();
            this.statusJson = "Recupere";
    }

    public ArrayList<Fiche> getListeFiche() {
        return listeFiche ;
    }

    public void addFiche(Fiche uneFiche) {
        if(this.listeFiche == null) {
            this.listeFiche = new ArrayList<>() ;
        }
        this.listeFiche.add(uneFiche) ;
    }

    public void setFiche(Fiche uneFiche) {
        ArrayList<String> nomParams = new ArrayList<>() ;
        nomParams.add("chantier") ;
        nomParams.add("utilisateur") ;
        nomParams.add("date") ;

        ArrayList<String> valeurParams = new ArrayList<>() ;
        valeurParams.add(uneFiche.getUnChantier().getCode()) ;
        valeurParams.add(uneFiche.getUnUtilisateur().getId()) ;
        valeurParams.add(uneFiche.getDate());

        oConnexion1 = new ConnexionBDD(nomParams, valeurParams, "SaisirFiche", this.activite);
        oConnexion1.getResponse = this ;
        oConnexion1.execute() ;

        this.statusJson = "Envoyer" ;
    }

    @Override
    public Void getData(String resultatJson) {

        if(this.statusJson.equals("Recupere")) {
            if(oConnexion.isJSON()) {
                ParserJSON oParser = new ParserJSON(resultatJson);
                for (int i = 1; i < oParser.getOJSON().length() + 1; i++) {
                    ParserJSON oFiche = new ParserJSON(oParser.getOJSON(), Integer.toString(i));

                    //On déclare l'objet chantier
                    Chantier unChantier = new Chantier(oFiche.getString("cha_code"), oFiche.getString("cha_libelle"), oFiche.getString("cha_nrue"), oFiche.getString("cha_rue"), oFiche.getString("cha_ville"), oFiche.getString("cha_codepo"), oFiche.getBoolean("cha_supprimer"));

                    //On déclare l'objet fonction
                    Fonction uneFonction = new Fonction(oFiche.getString("fon_id"), oFiche.getString("fon_libelle"));

                    //On déclare l'objet utilisateur
                    Utilisateur unUtilisateur = new Utilisateur(oFiche.getString("use_id"), oFiche.getString("use_nom"), oFiche.getString("use_prenom"), oFiche.getString("use_mail"), uneFonction, oFiche.getBoolean("use_supprimer"));

                    Fiche uneFiche = new Fiche(oFiche.getString("fic_id"), unChantier, unUtilisateur, oFiche.getString("fic_date"), oFiche.getBoolean("fic_supprimer"));
                    addFiche(uneFiche);
                }
            }
        } else if(this.statusJson.equals("Envoyer")) {
            if(oConnexion1.isJSON()) {

            }
        }
        return null ;
    }
}
