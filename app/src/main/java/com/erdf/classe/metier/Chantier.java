package com.erdf.classe.metier;

/**
 * Created by Radhan on 19/04/2016.
 */
public class Chantier {
    private String code ;
    private String libelle ;
    private String numRue ;
    private String rue ;
    private String ville ;
    private String codePostal ;

    public Chantier() {
    }

    public Chantier(String code, String libelle, String numRue, String rue, String ville, String codePostal) {
        this.code = code;
        this.libelle = libelle;
        this.numRue = numRue;
        this.rue = rue;
        this.ville = ville;
        this.codePostal = codePostal;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getNumRue() {
        return numRue;
    }

    public void setNumRue(String numRue) {
        this.numRue = numRue;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }
}
