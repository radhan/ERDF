package com.erdf.classe.DAO;

import android.app.Activity;
import android.util.Log;

import com.erdf.classe.metier.Risque;
import com.erdf.classe.technique.ConnexionBDD;
import com.erdf.classe.technique.GetResponse;
import com.erdf.classe.technique.ParserJSON;

import java.util.ArrayList;

/**
 * Created by Radhan on 24/04/2016.
 */
public class RisqueDAO implements GetResponse {
    private ArrayList<Risque> listeRisque ;
    ConnexionBDD oConnexion ;

    public RisqueDAO(Activity pActivite) {
        oConnexion = new ConnexionBDD("Risque", pActivite);
        oConnexion.getResponse = this ;
        oConnexion.execute() ;
    }

    public ArrayList<Risque> getListeRisque() {
        return listeRisque ;
    }

    public void addRisque(Risque unRisque) {
        if(this.listeRisque == null) {
            this.listeRisque = new ArrayList<>() ;
        }
        this.listeRisque.add(unRisque) ;
        Log.i("addRisque", "Un risque ajouté") ;
    }

    @Override
    public Void getData(String resultatJson) {

        if(oConnexion.isJSON()) {
            ParserJSON oParser = new ParserJSON(resultatJson) ;
            for (int i = 1; i < oParser.getOJSON().length() + 1; i++) {
                ParserJSON oRisque = new ParserJSON(oParser.getOJSON(), Integer.toString(i)) ;
                Risque unRisque = new Risque(oRisque.getString("ris_id"), oRisque.getString("ris_titre"), oRisque.getString("ris_resume"), oRisque.getBoolean("ris_supprimer")) ;
                addRisque(unRisque) ;
            }
        }

        return null ;
    }
}
