package com.erdf.classe.metier;

/**
 * Created by Radhan on 24/04/2016.
 */
public class Fonction {
    private String id ;
    private String libelle ;
    private boolean supprimer ;
    private String createdAt ;

    public Fonction() {
    }

    public Fonction(String id, String libelle, boolean supprimer) {
        this.id = id;
        this.libelle = libelle;
        this.supprimer = supprimer ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
