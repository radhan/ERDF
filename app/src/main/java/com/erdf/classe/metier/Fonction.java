package com.erdf.classe.metier;

/**
 * Created by Radhan on 24/04/2016.
 */
public class Fonction {
    private String id ;
    private String libelle ;

    public Fonction() {
    }

    public Fonction(String id, String libelle) {
        this.id = id;
        this.libelle = libelle;
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
}
