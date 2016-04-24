package com.erdf.classe.metier;

/**
 * Created by Radhan on 24/04/2016.
 */
public class Fonction {
    private String libelle ;

    public Fonction() {
    }

    public Fonction(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
