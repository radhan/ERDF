package com.erdf.classe.DAO;

import android.app.Activity;

import com.erdf.classe.metier.Chantier;
import com.erdf.classe.technique.ConnexionBDD;
import com.erdf.classe.technique.GetResponse;
import com.erdf.classe.technique.ParserJSON;

import java.util.ArrayList;

/**
 * Created by Radhan on 19/04/2016.
 */
public class ChantierDAO implements GetResponse {
    private ArrayList<Chantier> listeChantier ;
    ConnexionBDD oConnexion ;

    public ChantierDAO(Activity pActivite) {
        oConnexion = new ConnexionBDD("Chantier", pActivite);
        oConnexion.getResponse = this ;
        oConnexion.execute() ;
    }

    public ArrayList<Chantier> getListeChantier() {
        return listeChantier;
    }

    public void addChantier(Chantier unChantier) {
        if(this.listeChantier == null) {
            this.listeChantier = new ArrayList<>() ;
        }
        this.listeChantier.add(unChantier) ;
    }

    @Override
    public Void getData(String resultatJson) {

        if(oConnexion.isJSON()) {
            ParserJSON oParser = new ParserJSON(resultatJson) ;
            for (int i = 1; i < oParser.getOJSON().length() + 1; i++) {
                ParserJSON oChantier = new ParserJSON(oParser.getOJSON(), Integer.toString(i)) ;
                Chantier unChantier = new Chantier(oChantier.getString("cha_code"), oChantier.getString("cha_libelle"), oChantier.getString("cha_nrue"), oChantier.getString("cha_rue"), oChantier.getString("cha_ville"), oChantier.getString("cha_codepo"), oChantier.getBoolean("cha_supprimer")) ;
                addChantier(unChantier) ;
            }
        }

        return null ;
    }
}
