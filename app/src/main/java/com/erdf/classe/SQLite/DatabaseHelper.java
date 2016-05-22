package com.erdf.classe.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.erdf.classe.metier.Chantier;
import com.erdf.classe.metier.Fiche;
import com.erdf.classe.metier.FicheRisque;
import com.erdf.classe.metier.Fonction;
import com.erdf.classe.metier.Risque;
import com.erdf.classe.metier.Utilisateur;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Radhan on 01/05/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME           = "ERDF"            ;

    // Table Names
    private static final String TABLE_CHANTIER          = "edf_chantier"    ;
    private static final String TABLE_RISQUE            = "edf_risque"      ;
    private static final String TABLE_FONCTION          = "edf_fonction"    ;
    private static final String TABLE_UTILISATEUR       = "edf_user"        ;
    private static final String TABLE_FICHE             = "edf_fiche"       ;
    private static final String TABLE_COMPTE            = "edf_compte"      ;
    private static final String TABLE_FICHE_RISQUE      = "edf_fiche_risque";

    // Common column names
    private static final String KEY_CREATED_AT          = "created_at"      ;

    // CHANTIER Table - column names
    private static final String KEY_CHANTIER_CODE       = "cha_code"        ;
    private static final String KEY_CHANTIER_LIBELLE    = "cha_libelle"     ;
    private static final String KEY_CHANTIER_NUMRUE     = "cha_nrue"        ;
    private static final String KEY_CHANTIER_RUE        = "cha_rue"         ;
    private static final String KEY_CHANTIER_VILLE      = "cha_ville"       ;
    private static final String KEY_CHANTIER_CODEPOSTAL = "cha_codepo"      ;
    private static final String KEY_CHANTIER_SUPPRIMER  = "cha_supprimer"   ;

    // RISQUE Table - column names
    private static final String KEY_RISQUE_ID           = "ris_id"          ;
    private static final String KEY_RISQUE_TITRE        = "ris_titre"       ;
    private static final String KEY_RISQUE_RESUME       = "ris_resume"      ;
    private static final String KEY_RISQUE_SUPPRIMER    = "ris_supprimer"   ;

    // FONCTION Table - column names
    private static final String KEY_FONCTION_ID         = "fon_id"          ;
    private static final String KEY_FONCTION_LIBELLE    = "fon_libelle"     ;
    private static final String KEY_FONCTION_SUPPRIMER  = "fon_supprimer"   ;

    // UTILISATEUR Table - column names
    private static final String KEY_UTI_ID                  = "use_id"          ;
    private static final String KEY_UTI_NOM                 = "use_nom"         ;
    private static final String KEY_UTI_PRENOM              = "use_prenom"      ;
    private static final String KEY_UTI_EMAIL               = "use_mail"        ;
    private static final String KEY_UTI_FONCTION            = "use_fonction"    ;
    private static final String KEY_UTI_SUPPRIMER           = "use_supprimer"   ;

    // FICHE Table - column names
    private static final String KEY_FICHE_ID                = "fic_id"          ;
    private static final String KEY_FICHE_CHANTIER          = "fic_chantier"    ;
    private static final String KEY_FICHE_UTI               = "fic_user"        ;
    private static final String KEY_FICHE_DATE              = "fic_date"        ;
    private static final String KEY_FICHE_SUPPRIMER         = "fic_supprimer"   ;

    // COMPTE Table - column names
    private static final String KEY_COMPTE_ID               = "com_id"          ;
    private static final String KEY_COMPTE_LOGIN            = "com_login"       ;
    private static final String KEY_COMPTE_PASS             = "com_pass"        ;
    private static final String KEY_COMPTE_SUPPRIMER        = "com_supprimer"   ;

    // FICHE_RISQUE Table - column names
    private static final String KEY_FICHE_RISQUE_FICHE      = "fri_fiche"       ;
    private static final String KEY_FICHE_RISQUE_RISQUE     = "fri_risque"      ;
    private static final String KEY_FICHE_RISQUE_SUPPRIMER  = "fri_supprimer"   ;


    // Chantier table create statement
    private static final String CREATE_TABLE_CHANTIER = "CREATE TABLE " + TABLE_CHANTIER
            + "(" + KEY_CHANTIER_CODE + " VARCHAR(10) PRIMARY KEY, "
            + KEY_CHANTIER_LIBELLE + " VARCHAR(100), "
            + KEY_CHANTIER_NUMRUE + " VARCHAR(15), "
            + KEY_CHANTIER_RUE + " VARCHAR(100), "
            + KEY_CHANTIER_VILLE + " VARCHAR(60), "
            + KEY_CHANTIER_CODEPOSTAL + " VARCHAR(6), "
            + KEY_CHANTIER_SUPPRIMER + " TINYINT(1), "
            + KEY_CREATED_AT + " DATETIME" + ")" ;

    // Risque table create statement
    private static final String CREATE_TABLE_RISQUE = "CREATE TABLE " + TABLE_RISQUE
            + "(" + KEY_RISQUE_ID + " VARCHAR(10) PRIMARY KEY, "
            + KEY_RISQUE_TITRE + " VARCHAR(255), "
            + KEY_RISQUE_RESUME + " TEXT, "
            + KEY_RISQUE_SUPPRIMER + " TINYINT(1), "
            + KEY_CREATED_AT + " DATETIME" + ")" ;

    // Fonction table create statement
    private static final String CREATE_TABLE_FONCTION = "CREATE TABLE " + TABLE_FONCTION
            + "(" + KEY_FONCTION_ID + " VARCHAR(2) PRIMARY KEY, "
            + KEY_FONCTION_LIBELLE + " VARCHAR(20), "
            + KEY_FONCTION_SUPPRIMER + " TINYINT(1), "
            + KEY_CREATED_AT + " DATETIME" + ")" ;

    // Utilisateur table create statement
    private static final String CREATE_TABLE_UTILISATEUR = "CREATE TABLE " + TABLE_UTILISATEUR
            + "(" + KEY_UTI_ID + " VARCHAR(10) PRIMARY KEY, "
            + KEY_UTI_NOM + " VARCHAR(50), "
            + KEY_UTI_PRENOM + " VARCHAR(50), "
            + KEY_UTI_EMAIL + " VARCHAR(50), "
            + KEY_UTI_FONCTION + " VARCHAR(2), "
            + KEY_UTI_SUPPRIMER + " TINYINT(1), "
            + KEY_CREATED_AT + " DATETIME" + ")" ;

    // Fiche table create statement
    private static final String CREATE_TABLE_FICHE = "CREATE TABLE " + TABLE_FICHE
            + "(" + KEY_FICHE_ID + " VARCHAR(10) PRIMARY KEY, "
            + KEY_FICHE_CHANTIER + " VARCHAR(10), "
            + KEY_FICHE_UTI + " VARCHAR(10), "
            + KEY_FICHE_DATE + " DATETIME, "
            + KEY_FICHE_SUPPRIMER + " TINYINT(1), "
            + KEY_CREATED_AT + " DATETIME" + ")" ;

    // Compte table create statement
    private static final String CREATE_TABLE_COMPTE = "CREATE TABLE " + TABLE_COMPTE
            + "(" + KEY_COMPTE_ID + " VARCHAR(10) PRIMARY KEY, "
            + KEY_COMPTE_LOGIN + " VARCHAR(50), "
            + KEY_COMPTE_PASS + " VARCHAR(32), "
            + KEY_COMPTE_SUPPRIMER + " TINYINT(1), "
            + KEY_CREATED_AT + " DATETIME" + ")" ;

    // Fiche Risque table create statement
    private static final String CREATE_TABLE_FICHE_RISQUE = "CREATE TABLE " + TABLE_FICHE_RISQUE
            + "(" + KEY_FICHE_RISQUE_FICHE + " VARCHAR(10), "
            + KEY_FICHE_RISQUE_RISQUE + " VARCHAR(10), "
            + KEY_FICHE_RISQUE_SUPPRIMER + " TINYINT(1), "
            + KEY_CREATED_AT + " DATETIME, "
            + "PRIMARY KEY(" + KEY_FICHE_RISQUE_FICHE + ", " + KEY_FICHE_RISQUE_RISQUE + "))" ;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_CHANTIER)       ;
        db.execSQL(CREATE_TABLE_RISQUE)         ;
        db.execSQL(CREATE_TABLE_FONCTION)       ;
        db.execSQL(CREATE_TABLE_UTILISATEUR)    ;
        db.execSQL(CREATE_TABLE_FICHE)          ;
        db.execSQL(CREATE_TABLE_COMPTE)         ;
        db.execSQL(CREATE_TABLE_FICHE_RISQUE)   ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANTIER)        ;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RISQUE)          ;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FONCTION)        ;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UTILISATEUR)     ;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FICHE)           ;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPTE)          ;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FICHE_RISQUE)    ;

        // create new tables
        onCreate(db);
    }

    //METHODES
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /*
        ------------------------------------------------------------------
                                    CHANTIER
        ------------------------------------------------------------------
    */

    public void setUnChantier(Chantier unChantier) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues() ;
        values.put(KEY_CHANTIER_CODE, unChantier.getCode()) ;
        values.put(KEY_CHANTIER_LIBELLE, unChantier.getLibelle()) ;
        values.put(KEY_CHANTIER_NUMRUE, unChantier.getNumRue()) ;
        values.put(KEY_CHANTIER_RUE, unChantier.getRue()) ;
        values.put(KEY_CHANTIER_VILLE, unChantier.getVille()) ;
        values.put(KEY_CHANTIER_CODEPOSTAL, unChantier.getCodePostal()) ;
        values.put(KEY_CHANTIER_SUPPRIMER, unChantier.isSupprimer()) ;
        values.put(KEY_CREATED_AT, getDateTime()) ;

        // insert row
        db.insertWithOnConflict(TABLE_CHANTIER, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close() ;

        Log.d(TAG, "Un chantier a été ajouté !") ;
    }

    public ArrayList<Chantier> getAllChantiers() {
        ArrayList<Chantier> lesChantiers = new ArrayList<>() ;
        String selectQuery = "SELECT * FROM " + TABLE_CHANTIER + " ;" ;

        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // Ajout de chaque ligne à la liste
        if (c.moveToFirst()) {
            do {
                Chantier unChantier = new Chantier();
                unChantier.setCode(c.getString(c.getColumnIndex(KEY_CHANTIER_CODE))) ;
                unChantier.setLibelle((c.getString(c.getColumnIndex(KEY_CHANTIER_LIBELLE)))) ;
                unChantier.setNumRue((c.getString(c.getColumnIndex(KEY_CHANTIER_NUMRUE))));
                unChantier.setRue((c.getString(c.getColumnIndex(KEY_CHANTIER_RUE))));
                unChantier.setVille((c.getString(c.getColumnIndex(KEY_CHANTIER_VILLE))));
                unChantier.setCodePostal((c.getString(c.getColumnIndex(KEY_CHANTIER_CODEPOSTAL)))) ;
                boolean supprimer = c.getInt(c.getColumnIndex(KEY_CHANTIER_SUPPRIMER)) > 0 ;
                unChantier.setSupprimer(supprimer);
                unChantier.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT))) ;

                // Ajouter à la liste des risques
                lesChantiers.add(unChantier);
            } while (c.moveToNext());
        }

        db.close() ;

        return lesChantiers ;
    }

    public Chantier getUnChantier(String code) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_CHANTIER + " " +
                             "WHERE " + KEY_CHANTIER_CODE + " = " + code + " ;" ;

        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        }

        Chantier unChantier = new Chantier();
        assert c != null;

        if(c.getCount() > 0) {
            unChantier.setCode(c.getString(c.getColumnIndex(KEY_CHANTIER_CODE)));
            unChantier.setLibelle((c.getString(c.getColumnIndex(KEY_CHANTIER_LIBELLE))));
            unChantier.setNumRue((c.getString(c.getColumnIndex(KEY_CHANTIER_NUMRUE))));
            unChantier.setRue((c.getString(c.getColumnIndex(KEY_CHANTIER_RUE))));
            unChantier.setVille((c.getString(c.getColumnIndex(KEY_CHANTIER_VILLE))));
            unChantier.setCodePostal((c.getString(c.getColumnIndex(KEY_CHANTIER_CODEPOSTAL))));
            boolean supprimer = c.getInt(c.getColumnIndex(KEY_CHANTIER_SUPPRIMER)) > 0;
            unChantier.setSupprimer(supprimer);
            unChantier.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        }

        db.close() ;

        return unChantier ;
    }

    /*
        ------------------------------------------------------------------
                                    RISQUE
        ------------------------------------------------------------------
    */

    public void setUnRisque(Risque unRisque) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues() ;
        values.put(KEY_RISQUE_ID, unRisque.getId()) ;
        values.put(KEY_RISQUE_TITRE, unRisque.getTitre()) ;
        values.put(KEY_RISQUE_RESUME, unRisque.getResume()) ;
        values.put(KEY_RISQUE_SUPPRIMER, unRisque.isSupprimer()) ;
        values.put(KEY_CREATED_AT, getDateTime()) ;

        // insert row
        db.insertWithOnConflict(TABLE_RISQUE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close() ;

        Log.d(TAG, "Un risque a été ajouté !") ;
    }

    public ArrayList<Risque> getAllRisques() {
        ArrayList<Risque> lesRisques = new ArrayList<>() ;
        String selectQuery = "SELECT * FROM " + TABLE_RISQUE + " ;" ;

        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // Ajout de chaque ligne à la liste
        if (c.moveToFirst()) {
            do {
                Risque unRisque = new Risque();
                unRisque.setId(c.getString((c.getColumnIndex(KEY_RISQUE_ID)))) ;
                unRisque.setTitre((c.getString(c.getColumnIndex(KEY_RISQUE_TITRE)))) ;
                unRisque.setResume((c.getString(c.getColumnIndex(KEY_RISQUE_RESUME)))) ;
                boolean supprimer = c.getInt(c.getColumnIndex(KEY_RISQUE_SUPPRIMER)) > 0 ;
                unRisque.setSupprimer(supprimer) ;
                unRisque.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT))) ;

                // Ajouter à la liste des risques
                lesRisques.add(unRisque);
            } while (c.moveToNext());
        }

        db.close() ;

        return lesRisques ;
    }

    public Risque getUnRisque(String code) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_RISQUE + " " +
                             "WHERE " + KEY_RISQUE_ID + " = " + code + " ;" ;

        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        }

        Risque unRisque = new Risque() ;

        assert c != null;
        if(c.getCount() > 0) {
            unRisque.setId(c.getString((c.getColumnIndex(KEY_RISQUE_ID))));
            unRisque.setTitre((c.getString(c.getColumnIndex(KEY_RISQUE_TITRE))));
            unRisque.setResume((c.getString(c.getColumnIndex(KEY_RISQUE_RESUME))));
            boolean supprimer = c.getInt(c.getColumnIndex(KEY_RISQUE_SUPPRIMER)) > 0;
            unRisque.setSupprimer(supprimer);
            unRisque.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        }

        db.close() ;

        return unRisque;
    }

    /*
        ------------------------------------------------------------------
                                    FICHE
        ------------------------------------------------------------------
    */

    public void setUneFiche(Fiche uneFiche) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues() ;
        values.put(KEY_FICHE_ID, uneFiche.getId()) ;
        values.put(KEY_FICHE_CHANTIER, uneFiche.getUnChantier().getCode()) ;
        values.put(KEY_FICHE_UTI, uneFiche.getUnUtilisateur().getId()) ;
        values.put(KEY_FICHE_DATE, uneFiche.getDate()) ;
        values.put(KEY_FICHE_SUPPRIMER, uneFiche.isSupprimer()) ;
        values.put(KEY_CREATED_AT, getDateTime()) ;

        // insert row
        db.insertWithOnConflict(TABLE_FICHE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close() ;

        Log.d(TAG, "Une fiche a été ajoutée !") ;
    }

    public Fiche getUneFiche(String code) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_FICHE
                       + " WHERE " + KEY_FICHE_ID + " = " + code + " ;" ;

        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null){
            c.moveToFirst() ;
        }

        Fiche uneFiche = new Fiche();

        assert c != null;
        if(c.getCount() > 0) {
            Chantier unChantier = getUnChantier(c.getString(c.getColumnIndex(KEY_FICHE_CHANTIER))) ;
            Utilisateur unUtilisateur = getUnUtilisateur(c.getString(c.getColumnIndex(KEY_FICHE_UTI))) ;

            ArrayList<FicheRisque> listeFicheRisque = getUneFicheRisqueByFiche(c.getString((c.getColumnIndex(KEY_FICHE_ID)))) ;
            ArrayList<Risque> listeRisque = new ArrayList<>() ;
            for(FicheRisque uneFicheRisque : listeFicheRisque) {
                Risque unRisque = getUnRisque(uneFicheRisque.getUnRisque().getId()) ;
                listeRisque.add(unRisque) ;
            }

            uneFiche.setId(c.getString((c.getColumnIndex(KEY_FICHE_ID))));
            uneFiche.setUnChantier(unChantier);
            uneFiche.setUnUtilisateur(unUtilisateur) ;
            uneFiche.setListeRisque(listeRisque) ;
            uneFiche.setDate(c.getString((c.getColumnIndex(KEY_FICHE_DATE))));
            boolean supprimer = c.getInt(c.getColumnIndex(KEY_FICHE_SUPPRIMER)) > 0;
            uneFiche.setSupprimer(supprimer) ;
            uneFiche.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT))) ;
        }

        db.close() ;

        return uneFiche ;
    }

    public ArrayList<Fiche> getAllFiches() {
        ArrayList<Fiche> lesFiches = new ArrayList<>() ;

        String selectQuery = "SELECT * FROM " + TABLE_FICHE + " ;" ;

        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // Ajout de chaque ligne à la liste
        if (c.moveToFirst()) {
            do {

                Chantier unChantier = getUnChantier(c.getString(c.getColumnIndex(KEY_FICHE_CHANTIER))) ;
                Utilisateur unUtilisateur = getUnUtilisateur(c.getString(c.getColumnIndex(KEY_FICHE_UTI))) ;

                ArrayList<FicheRisque> listeFicheRisque = getUneFicheRisqueByFiche(c.getString((c.getColumnIndex(KEY_FICHE_ID)))) ;
                ArrayList<Risque> listeRisque = new ArrayList<>() ;
                for(FicheRisque uneFicheRisque : listeFicheRisque) {
                    Risque unRisque = getUnRisque(uneFicheRisque.getUnRisque().getId()) ;
                    listeRisque.add(unRisque) ;
                }

                Fiche uneFiche = new Fiche() ;
                uneFiche.setId(c.getString((c.getColumnIndex(KEY_FICHE_ID))));
                uneFiche.setUnChantier(unChantier);
                uneFiche.setUnUtilisateur(unUtilisateur) ;
                uneFiche.setListeRisque(listeRisque) ;
                uneFiche.setDate(c.getString((c.getColumnIndex(KEY_FICHE_DATE))));
                boolean supprimer = c.getInt(c.getColumnIndex(KEY_FICHE_SUPPRIMER)) > 0;
                uneFiche.setSupprimer(supprimer) ;
                uneFiche.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT))) ;

                // Ajouter à la liste des risques
                lesFiches.add(uneFiche);
            } while (c.moveToNext());
        }

        db.close() ;

        return lesFiches ;
    }

    /*
        ------------------------------------------------------------------
                                    FONCTION
        ------------------------------------------------------------------
    */

    public void setUneFonction(Fonction uneFonction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues() ;
        values.put(KEY_FONCTION_ID, uneFonction.getId()) ;
        values.put(KEY_FONCTION_LIBELLE, uneFonction.getLibelle()) ;
        values.put(KEY_FONCTION_SUPPRIMER, uneFonction.isSupprimer()) ;
        values.put(KEY_CREATED_AT, getDateTime()) ;

        // insert row
        db.insertWithOnConflict(TABLE_FONCTION, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close() ;

        Log.d(TAG, "Une fonction a été ajoutée !") ;
    }

    public Fonction getUneFonction(String code) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_FONCTION + " " +
                             "WHERE " + KEY_FONCTION_ID + " = " + code + " ;" ;

        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        }

        Fonction uneFonction = new Fonction() ;

        assert c != null;
        if(c.getCount() > 0) {
            uneFonction.setId(c.getString((c.getColumnIndex(KEY_FONCTION_ID)))) ;
            uneFonction.setLibelle((c.getString(c.getColumnIndex(KEY_FONCTION_LIBELLE)))) ;
            boolean supprimer = c.getInt(c.getColumnIndex(KEY_FONCTION_SUPPRIMER)) > 0 ;
            uneFonction.setSupprimer(supprimer) ;
            uneFonction.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT))) ;
        }

        db.close() ;

        return uneFonction ;
    }

    public ArrayList<Fonction> getAllFonctions() {
        ArrayList<Fonction> lesFonctions = new ArrayList<>() ;
        String selectQuery = "SELECT * FROM " + TABLE_FONCTION + " ;" ;

        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // Ajout de chaque ligne à la liste
        if (c.moveToFirst()) {
            do {
                Fonction uneFonction = new Fonction() ;
                uneFonction.setId(c.getString((c.getColumnIndex(KEY_FONCTION_ID)))) ;
                uneFonction.setLibelle((c.getString(c.getColumnIndex(KEY_FONCTION_LIBELLE)))) ;
                boolean supprimer = c.getInt(c.getColumnIndex(KEY_FONCTION_SUPPRIMER)) > 0 ;
                uneFonction.setSupprimer(supprimer) ;
                uneFonction.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT))) ;

                // Ajouter à la liste des risques
                lesFonctions.add(uneFonction);
            } while (c.moveToNext());
        }

        db.close() ;

        return lesFonctions ;
    }

    /*
        ------------------------------------------------------------------
                                    UTILISATEUR
        ------------------------------------------------------------------
    */

    public void setUnUtilisateur(Utilisateur unUtilisateur) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues() ;
        values.put(KEY_UTI_ID, unUtilisateur.getId()) ;
        values.put(KEY_UTI_NOM, unUtilisateur.getNom()) ;
        values.put(KEY_UTI_PRENOM, unUtilisateur.getPrenom()) ;
        values.put(KEY_UTI_EMAIL, unUtilisateur.getMail()) ;
        values.put(KEY_UTI_FONCTION, unUtilisateur.getUneFonction().getId()) ;
        values.put(KEY_UTI_SUPPRIMER, unUtilisateur.isSupprimer()) ;
        values.put(KEY_CREATED_AT, getDateTime()) ;

        // insert row
        db.insertWithOnConflict(TABLE_UTILISATEUR, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close() ;

        Log.d(TAG, "Un utilisateur a été ajouté !") ;
    }

    public Utilisateur getUnUtilisateur(String code) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_UTILISATEUR + " " +
                             "WHERE " + KEY_UTI_ID + " = " + code + " ;" ;

        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        }

        Utilisateur unUtilisateur = new Utilisateur() ;

        assert c != null;
        if(c.getCount() > 0) {
            Fonction uneFonction = getUneFonction(c.getString(c.getColumnIndex(KEY_UTI_FONCTION))) ;

            unUtilisateur.setId(c.getString((c.getColumnIndex(KEY_UTI_ID)))) ;
            unUtilisateur.setNom((c.getString(c.getColumnIndex(KEY_UTI_NOM))));
            unUtilisateur.setPrenom((c.getString(c.getColumnIndex(KEY_UTI_PRENOM))));
            unUtilisateur.setMail((c.getString(c.getColumnIndex(KEY_UTI_EMAIL)))) ;
            unUtilisateur.setUneFonction(uneFonction) ;
            boolean supprimerUti = c.getInt(c.getColumnIndex(KEY_UTI_SUPPRIMER)) > 0 ;
            unUtilisateur.setSupprimer(supprimerUti) ;
            unUtilisateur.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT))) ;
        }

        db.close() ;

        return unUtilisateur ;
    }

    public ArrayList<Utilisateur> getAllUtilisateurs() {
        ArrayList<Utilisateur> lesUtilisateurs = new ArrayList<>() ;
        String selectQuery = "SELECT * FROM " + TABLE_UTILISATEUR + " ;" ;

        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // Ajout de chaque ligne à la liste
        if (c.moveToFirst()) {
            do {
                Fonction uneFonction = getUneFonction(c.getString(c.getColumnIndex(KEY_UTI_FONCTION))) ;

                Utilisateur unUtilisateur = new Utilisateur() ;
                unUtilisateur.setId(c.getString((c.getColumnIndex(KEY_UTI_ID)))) ;
                unUtilisateur.setNom((c.getString(c.getColumnIndex(KEY_UTI_NOM)))) ;
                unUtilisateur.setPrenom((c.getString(c.getColumnIndex(KEY_UTI_PRENOM)))) ;
                unUtilisateur.setMail((c.getString(c.getColumnIndex(KEY_UTI_EMAIL)))) ;
                unUtilisateur.setUneFonction(uneFonction) ;
                boolean supprimer = c.getInt(c.getColumnIndex(KEY_UTI_SUPPRIMER)) > 0 ;
                unUtilisateur.setSupprimer(supprimer) ;
                unUtilisateur.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT))) ;

                // Ajouter à la liste des risques
                lesUtilisateurs.add(unUtilisateur) ;
            } while (c.moveToNext());
        }

        db.close() ;

        return lesUtilisateurs ;
    }

    /*
        ------------------------------------------------------------------
                                    FICHE RISQUE
        ------------------------------------------------------------------
    */

    public void setUneFicheRisque(FicheRisque uneFicheRisque) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues() ;
        values.put(KEY_FICHE_RISQUE_FICHE, uneFicheRisque.getUneFiche().getId()) ;
        values.put(KEY_FICHE_RISQUE_RISQUE, uneFicheRisque.getUnRisque().getId()) ;
        values.put(KEY_FICHE_RISQUE_SUPPRIMER, uneFicheRisque.isSupprimer()) ;
        values.put(KEY_CREATED_AT, getDateTime()) ;

        // insert row
        db.insertWithOnConflict(TABLE_FICHE_RISQUE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close() ;

        Log.d(TAG, "Une fiche risque a été ajoutée !") ;
    }

    public ArrayList<FicheRisque> getUneFicheRisqueByFiche(String code) {
        ArrayList<FicheRisque> lesFicheRisques = new ArrayList<>() ;

        String selectQuery = "SELECT  * FROM " + TABLE_FICHE_RISQUE + " " +
                             "WHERE " + KEY_FICHE_RISQUE_FICHE + " = " + code + " ;" ;

        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // Ajout de chaque ligne à la liste
        if (c.moveToFirst()) {
            do {

                Fiche uneFiche = getUneFiche(c.getString(c.getColumnIndex(KEY_FICHE_RISQUE_FICHE))) ;
                Risque unRisque = getUnRisque(c.getString(c.getColumnIndex(KEY_FICHE_RISQUE_RISQUE))) ;

                FicheRisque uneFicheRisque = new FicheRisque() ;
                uneFicheRisque.setUneFiche(uneFiche) ;
                uneFicheRisque.setUnRisque(unRisque) ;
                boolean supprimer = c.getInt(c.getColumnIndex(KEY_FICHE_RISQUE_SUPPRIMER)) > 0 ;
                uneFicheRisque.setSupprimer(supprimer);
                uneFicheRisque.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT))) ;

                // Ajouter à la liste des risques
                lesFicheRisques.add(uneFicheRisque);
            } while (c.moveToNext());
        }

        db.close() ;

        return lesFicheRisques ;
    }

    public ArrayList<FicheRisque> getUneFicheRisqueByRisque(String code) {
        ArrayList<FicheRisque> lesFicheRisques = new ArrayList<>() ;

        String selectQuery = "SELECT  * FROM " + TABLE_FICHE_RISQUE + " " +
                             "WHERE " + KEY_FICHE_RISQUE_RISQUE + " = " + code + " ;" ;

        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // Ajout de chaque ligne à la liste
        if (c.moveToFirst()) {
            do {

                Fiche uneFiche = getUneFiche(c.getString(c.getColumnIndex(KEY_FICHE_RISQUE_FICHE))) ;
                Risque unRisque = getUnRisque(c.getString(c.getColumnIndex(KEY_FICHE_RISQUE_RISQUE))) ;

                FicheRisque uneFicheRisque = new FicheRisque() ;
                uneFicheRisque.setUneFiche(uneFiche) ;
                uneFicheRisque.setUnRisque(unRisque) ;
                boolean supprimer = c.getInt(c.getColumnIndex(KEY_FICHE_RISQUE_SUPPRIMER)) > 0 ;
                uneFicheRisque.setSupprimer(supprimer);
                uneFicheRisque.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT))) ;

                // Ajouter à la liste des risques
                lesFicheRisques.add(uneFicheRisque);
            } while (c.moveToNext());
        }

        db.close() ;

        return lesFicheRisques ;
    }

    public ArrayList<FicheRisque> getAllFicheRisques() {
        ArrayList<FicheRisque> lesFicheRisques = new ArrayList<>() ;

        String selectQuery = "SELECT * FROM " + TABLE_FICHE_RISQUE + " ;" ;

        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // Ajout de chaque ligne à la liste
        if (c.moveToFirst()) {
            do {

                Fiche uneFiche = getUneFiche(c.getString(c.getColumnIndex(KEY_FICHE_RISQUE_FICHE))) ;
                Risque unRisque = getUnRisque(c.getString(c.getColumnIndex(KEY_FICHE_RISQUE_RISQUE))) ;

                FicheRisque uneFicheRisque = new FicheRisque() ;
                uneFicheRisque.setUneFiche(uneFiche) ;
                uneFicheRisque.setUnRisque(unRisque) ;
                boolean supprimer = c.getInt(c.getColumnIndex(KEY_FICHE_RISQUE_SUPPRIMER)) > 0 ;
                uneFicheRisque.setSupprimer(supprimer);
                uneFicheRisque.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT))) ;

                // Ajouter à la liste des risques
                lesFicheRisques.add(uneFicheRisque);
            } while (c.moveToNext());
        }

        db.close() ;

        return lesFicheRisques ;
    }
}
