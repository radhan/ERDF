package com.erdf.classe.metier;

import java.util.Date;

/**
 * Created by Radhan on 24/04/2016.
 */
public class Fiche {
    private Chantier unChantier ;
    private Utilisateur unUtilisateur ;
    private String date ;

    public Fiche() {
    }

    public Fiche(Chantier unChantier, Utilisateur unUtilisateur, String date) {
        this.unChantier = unChantier;
        this.unUtilisateur = unUtilisateur;
        this.date = date;
    }

    public Chantier getUnChantier() {
        return unChantier;
    }

    public void setUnChantier(Chantier unChantier) {
        this.unChantier = unChantier;
    }

    public Utilisateur getUnUtilisateur() {
        return unUtilisateur;
    }

    public void setUnUtilisateur(Utilisateur unUtilisateur) {
        this.unUtilisateur = unUtilisateur;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
