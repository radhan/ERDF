package com.erdf.classe.DAO;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.erdf.classe.metier.Chantier;
import com.erdf.classe.metier.Fiche;
import com.erdf.classe.metier.Fonction;
import com.erdf.classe.metier.Risque;
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
    private ArrayList<Risque> listeRisque ;
    ConnexionBDD oConnexion, oConnexion1 ;
    private String statusJson ;
    private Context context ;

    public FicheDAO() {
    }

    public FicheDAO(Activity pActivite) {
        if(this.listeRisque == null) {
            this.listeRisque = new ArrayList<>() ;
        }
        oConnexion = new ConnexionBDD("Fiche", pActivite);
        oConnexion.getResponse = this;
        oConnexion.execute();
        this.statusJson = "Recupere";
        this.context = pActivite.getApplicationContext() ;
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

    public void setFiche(Activity pActivity, Fiche uneFiche) {

        String risques = "" ;
        for(int i = 0; i < uneFiche.getListeRisque().size() ; i++) {
            if(!risques.isEmpty()) {
                risques = risques + "|" ;
            }
            risques = risques + uneFiche.getListeRisque().get(i).getId() ;
        }

        ArrayList<String> nomParams = new ArrayList<>() ;
        nomParams.add("chantier") ;
        nomParams.add("utilisateur") ;
        nomParams.add("date") ;
        nomParams.add("risque");

        ArrayList<String> valeurParams = new ArrayList<>() ;
        valeurParams.add(uneFiche.getUnChantier().getCode()) ;
        valeurParams.add(uneFiche.getUnUtilisateur().getId()) ;
        valeurParams.add(uneFiche.getDate());
        valeurParams.add(risques) ;

        oConnexion1 = new ConnexionBDD(nomParams, valeurParams, "SaisirFiche", pActivity);
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

                    listeRisque = new ArrayList<>();

                    //On déclare l'objet chantier
                    Chantier unChantier = new Chantier(oFiche.getString("cha_code"), oFiche.getString("cha_libelle"), oFiche.getString("cha_nrue"), oFiche.getString("cha_rue"), oFiche.getString("cha_ville"), oFiche.getString("cha_codepo"), oFiche.getBoolean("cha_supprimer"));

                    //On déclare l'objet fonction
                    Fonction uneFonction = new Fonction(oFiche.getString("fon_id"), oFiche.getString("fon_libelle"));

                    //On déclare l'objet utilisateur
                    Utilisateur unUtilisateur = new Utilisateur(oFiche.getString("use_id"), oFiche.getString("use_nom"), oFiche.getString("use_prenom"), oFiche.getString("use_mail"), uneFonction, oFiche.getBoolean("use_supprimer"));

                    if(oFiche.getInt("nbRisque") > 0) {
                        for (int j = 0; j < oFiche.getInt("nbRisque"); j++) {
                            ParserJSON oRisque = new ParserJSON(oFiche.getOJSON(), Integer.toString(j));
                            //On déclare l'objet risque
                            Risque unRisque = new Risque(oRisque.getString("ris_id"), oRisque.getString("ris_titre"), oRisque.getString("ris_resume"), oRisque.getBoolean("ris_supprimer"));
                            listeRisque.add(unRisque) ;
                        }
                    }

                    Fiche uneFiche = new Fiche(oFiche.getString("fic_id"), unChantier, unUtilisateur, oFiche.getString("fic_date"), oFiche.getBoolean("fic_supprimer"));
                    uneFiche.setListeRisque(listeRisque);
                    addFiche(uneFiche);
                }
            }
        } else if(this.statusJson.equals("Envoyer")) {
            if(oConnexion1.isJSON()) {
                ParserJSON oParser = new ParserJSON(resultatJson) ;
                if (oParser.getString("RESULTAT").contains("OK")) {
                   // Toast.makeText(AddEntrepriseActivity.this, "Ajout d'une fiche réussi", Toast.LENGTH_SHORT).show() ;
                } else {
                   // Toast.makeText(AddEntrepriseActivity.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show() ;
                }
            }
        }
        return null ;
    }
}
