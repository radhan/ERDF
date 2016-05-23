package com.erdf.classe.metier;

/**
 * Created by Alexandre on 20/05/2016.
 */
public class Compte {
    private String id ;
    private String login ;
    private String password ;
    private boolean supprimer ;

    public Compte() {
    }

    public Compte(String id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }
}
