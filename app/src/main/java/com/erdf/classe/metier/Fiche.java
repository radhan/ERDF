package com.erdf.classe.metier;

/**
 * Created by Radhan on 24/04/2016.
 */
public class Fiche {
    private String id ;
    private Chantier unChantier ;
    private Utilisateur unUtilisateur ;
    private String date ;
    private boolean supprimer ;

    public Fiche() {
    }

    public Fiche(String id, Chantier unChantier, Utilisateur unUtilisateur, String date, boolean supprimer) {
        this.id = id;
        this.unChantier = unChantier;
        this.unUtilisateur = unUtilisateur;
        this.date = date;
        this.supprimer = supprimer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }
}
