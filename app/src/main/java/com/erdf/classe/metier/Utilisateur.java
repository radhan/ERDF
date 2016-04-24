package com.erdf.classe.metier;

/**
 * Created by Radhan on 24/04/2016.
 */
public class Utilisateur {
    private String nom ;
    private String prenom ;
    private String mail ;
    private Fonction uneFonction ;

    public Utilisateur() {
    }

    public Utilisateur(String nom, String prenom, String mail, Fonction uneFonction) {
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.uneFonction = uneFonction;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Fonction getUneFonction() {
        return uneFonction;
    }

    public void setUneFonction(Fonction uneFonction) {
        this.uneFonction = uneFonction;
    }
}
