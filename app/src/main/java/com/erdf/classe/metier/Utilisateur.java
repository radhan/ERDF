package com.erdf.classe.metier;

/**
 * Created by Radhan on 24/04/2016.
 */
public class Utilisateur {
    private String id ;
    private String nom ;
    private String prenom ;
    private String mail ;
    private Fonction uneFonction ;
    private Compte unCompte ;
    private boolean supprimer ;
    private String createdAt ;

    public Utilisateur() {
    }

    public Utilisateur(String id, String nom, String prenom, String mail, Fonction uneFonction, boolean supprimer) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.uneFonction = uneFonction;
        this.supprimer = supprimer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Compte getUnCompte() {
        return unCompte ;
    }

    public void setUnCompte(Compte compte) {
        this.unCompte = compte;
    }

    public Fonction getUneFonction() {
        return uneFonction;
    }

    public void setUneFonction(Fonction uneFonction) {
        this.uneFonction = uneFonction;
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
