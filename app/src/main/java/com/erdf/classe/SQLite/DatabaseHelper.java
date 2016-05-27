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
        super(context, DATABASE_NAME, null, DATABASE_VERSION) ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //On créer les tables
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

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANTIER)        ;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RISQUE)          ;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FONCTION)        ;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UTILISATEUR)     ;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FICHE)           ;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPTE)          ;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FICHE_RISQUE)    ;

        //On créer les nouvelles tables
        onCreate(db) ;
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

    public void addChantier(Chantier pChantier) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues values = new ContentValues()                      ;
        values.put(KEY_CHANTIER_CODE, pChantier.getCode())              ;
        values.put(KEY_CHANTIER_LIBELLE, pChantier.getLibelle())        ;
        values.put(KEY_CHANTIER_NUMRUE, pChantier.getNumRue())          ;
        values.put(KEY_CHANTIER_RUE, pChantier.getRue())                ;
        values.put(KEY_CHANTIER_VILLE, pChantier.getVille())            ;
        values.put(KEY_CHANTIER_CODEPOSTAL, pChantier.getCodePostal())  ;
        values.put(KEY_CHANTIER_SUPPRIMER, pChantier.isSupprimer())     ;
        values.put(KEY_CREATED_AT, getDateTime())                       ;

        //On insert la ligne
        db.insertWithOnConflict(TABLE_CHANTIER, null, values, SQLiteDatabase.CONFLICT_REPLACE) ;
        db.close() ;

        Log.d(TAG, "Un chantier a été ajouté !") ;
    }

    public void updateChantier(Chantier pChantier) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues values = new ContentValues()                      ;
        values.put(KEY_CHANTIER_CODE, pChantier.getCode())              ;
        values.put(KEY_CHANTIER_LIBELLE, pChantier.getLibelle())        ;
        values.put(KEY_CHANTIER_NUMRUE, pChantier.getNumRue())          ;
        values.put(KEY_CHANTIER_RUE, pChantier.getRue())                ;
        values.put(KEY_CHANTIER_VILLE, pChantier.getVille())            ;
        values.put(KEY_CHANTIER_CODEPOSTAL, pChantier.getCodePostal())  ;
        values.put(KEY_CHANTIER_SUPPRIMER, pChantier.isSupprimer())     ;
        values.put(KEY_CREATED_AT, getDateTime())                       ;

        //On met à jour la ligne
        db.update(TABLE_CHANTIER, values, KEY_CHANTIER_CODE + " = ?",
                new String[]{String.valueOf(pChantier.getCode())}) ;
        db.close() ;

        Log.d(TAG, "Un chantier a été modifié !") ;
    }

    public void deleteChantier(Chantier pChantier) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        db.delete(TABLE_CHANTIER, KEY_CHANTIER_CODE + " = ?",
                new String[]{String.valueOf(pChantier.getCode())}) ;
        db.close() ;

        Log.d(TAG, "Un chantier a été supprimé !") ;
    }

    public Chantier getChantier(String pCode) {
        SQLiteDatabase db = this.getReadableDatabase() ;

        String selectQuery = "SELECT * FROM " + TABLE_CHANTIER + " " +
                             "WHERE " + KEY_CHANTIER_CODE + " = " + pCode + " ;" ;

        Log.d(TAG, selectQuery) ;

        Cursor cursor = db.rawQuery(selectQuery, null) ;
        cursor.moveToFirst() ;

        Chantier unChantier = new Chantier() ;

        try {
            if (cursor.getCount() > 0) {

                unChantier.setCode(cursor.getString(cursor.getColumnIndex(KEY_CHANTIER_CODE)))              ;
                unChantier.setLibelle(cursor.getString(cursor.getColumnIndex(KEY_CHANTIER_LIBELLE)))        ;
                unChantier.setNumRue(cursor.getString(cursor.getColumnIndex(KEY_CHANTIER_NUMRUE)))          ;
                unChantier.setRue(cursor.getString(cursor.getColumnIndex(KEY_CHANTIER_RUE)))                ;
                unChantier.setVille(cursor.getString(cursor.getColumnIndex(KEY_CHANTIER_VILLE)))            ;
                unChantier.setCodePostal(cursor.getString(cursor.getColumnIndex(KEY_CHANTIER_CODEPOSTAL)))  ;
                boolean supprimer = cursor.getInt(cursor.getColumnIndex(KEY_CHANTIER_SUPPRIMER)) > 0        ;
                unChantier.setSupprimer(supprimer)                                                          ;
                unChantier.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)))            ;
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Erreur : " + e.toString()) ;
        }
        finally {
            db.close()      ;
            cursor.close()  ;
        }

        return unChantier ;
    }

    public String getDernierIdChantier() {
        SQLiteDatabase db = this.getReadableDatabase() ;

        String selectQuery = "SELECT MAX(" + KEY_CHANTIER_CODE + ") AS dernierId FROM " + TABLE_CHANTIER + " ;" ;

        Log.d(TAG, selectQuery) ;

        String dernierId = "" ;

        Cursor cursor = db.rawQuery(selectQuery, null) ;
        cursor.moveToFirst() ;

        try {
            if (cursor.getCount() > 0) {
                dernierId = cursor.getString(cursor.getColumnIndex("dernierId")) ;
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Erreur : " + e.toString()) ;
        }
        finally {
            cursor.close() ;
        }

        return dernierId ;
    }

    public ArrayList<Chantier> getAllChantiers() {
        ArrayList<Chantier> lesChantiers = new ArrayList<>() ;

        String selectQuery = "SELECT * FROM " + TABLE_CHANTIER + " ;" ;

        Log.d(TAG, selectQuery) ;

        SQLiteDatabase db = this.getReadableDatabase() ;

        Cursor cursor = db.rawQuery(selectQuery, null) ;
        cursor.moveToFirst() ;

        // Ajout de chaque ligne à la liste
        try {
            while (cursor.moveToNext()) {

                Chantier unChantier = new Chantier() ;
                unChantier.setCode(cursor.getString(cursor.getColumnIndex(KEY_CHANTIER_CODE)))              ;
                unChantier.setLibelle(cursor.getString(cursor.getColumnIndex(KEY_CHANTIER_LIBELLE)))        ;
                unChantier.setNumRue(cursor.getString(cursor.getColumnIndex(KEY_CHANTIER_NUMRUE)))          ;
                unChantier.setRue(cursor.getString(cursor.getColumnIndex(KEY_CHANTIER_RUE)))                ;
                unChantier.setVille(cursor.getString(cursor.getColumnIndex(KEY_CHANTIER_VILLE)))            ;
                unChantier.setCodePostal(cursor.getString(cursor.getColumnIndex(KEY_CHANTIER_CODEPOSTAL)))  ;
                boolean supprimer = cursor.getInt(cursor.getColumnIndex(KEY_CHANTIER_SUPPRIMER)) > 0        ;
                unChantier.setSupprimer(supprimer)                                                          ;
                unChantier.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)))            ;

                // Ajouter à la liste des risques
                lesChantiers.add(unChantier) ;
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Erreur : " + e.toString()) ;
        }
        finally {
            db.close()      ;
            cursor.close()  ;
        }

        return lesChantiers ;
    }

    public int getNbChantier() {
        SQLiteDatabase db = this.getReadableDatabase() ;

        String countQuery = "SELECT * FROM " + TABLE_CHANTIER + " ;" ;

        Cursor cursor = db.rawQuery(countQuery, null) ;
        cursor.close() ;

        //On retourne le nombre de lignes
        return cursor.getCount();
    }

    /*
        ------------------------------------------------------------------
                                    RISQUE
        ------------------------------------------------------------------
    */

    public void addRisque(Risque pRisque) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues values = new ContentValues()                  ;
        values.put(KEY_RISQUE_ID, pRisque.getId())                  ;
        values.put(KEY_RISQUE_TITRE, pRisque.getTitre())            ;
        values.put(KEY_RISQUE_RESUME, pRisque.getResume())          ;
        values.put(KEY_RISQUE_SUPPRIMER, pRisque.isSupprimer())     ;
        values.put(KEY_CREATED_AT, getDateTime())                   ;

        //On insert la ligne
        db.insertWithOnConflict(TABLE_RISQUE, null, values, SQLiteDatabase.CONFLICT_REPLACE) ;
        db.close() ;

        Log.d(TAG, "Un risque a été ajouté !") ;
    }

    public void updateRisque(Risque pRisque) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues values = new ContentValues()                  ;
        values.put(KEY_RISQUE_ID, pRisque.getId())                  ;
        values.put(KEY_RISQUE_TITRE, pRisque.getTitre())            ;
        values.put(KEY_RISQUE_RESUME, pRisque.getResume())          ;
        values.put(KEY_RISQUE_SUPPRIMER, pRisque.isSupprimer())     ;
        values.put(KEY_CREATED_AT, getDateTime())                   ;

        //On met à jour la ligne
        db.update(TABLE_RISQUE, values, KEY_RISQUE_ID + " = ?",
                new String[]{String.valueOf(pRisque.getId())}) ;
        db.close() ;

        Log.d(TAG, "Un risque a été modifié !") ;
    }

    public void deleteRisque(Risque pRisque) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        db.delete(TABLE_RISQUE, KEY_RISQUE_ID + " = ?",
                new String[]{String.valueOf(pRisque.getId())}) ;
        db.close() ;

        Log.d(TAG, "Un risque a été supprimé !") ;
    }

    public Risque getRisque(String pCode, boolean pFiches) {
        SQLiteDatabase db = this.getReadableDatabase() ;

        String selectQuery = "SELECT * FROM " + TABLE_RISQUE + " " +
                             "WHERE " + KEY_RISQUE_ID + " = " + pCode + " ;" ;

        Log.d(TAG, selectQuery) ;

        Cursor cursor = db.rawQuery(selectQuery, null) ;
        cursor.moveToFirst() ;

        Risque unRisque = new Risque() ;

        try {
            if (cursor.getCount() > 0) {

                unRisque.setId(cursor.getString(cursor.getColumnIndex(KEY_RISQUE_ID)))          ;
                unRisque.setTitre(cursor.getString(cursor.getColumnIndex(KEY_RISQUE_TITRE)))    ;
                unRisque.setResume(cursor.getString(cursor.getColumnIndex(KEY_RISQUE_RESUME)))  ;

                //Si on veut récupérer la liste des fiches pour chaque risques
                if(pFiches) {

                    ArrayList<FicheRisque> listeFicheRisque = getFicheRisqueByRisque(cursor.getString(cursor.getColumnIndex(KEY_RISQUE_ID))) ;
                    ArrayList<Fiche> listeFiche = new ArrayList<>() ;

                    for(FicheRisque uneFicheRisque : listeFicheRisque) {
                        Fiche uneFiche = getFiche(uneFicheRisque.getUneFiche().getId(), false) ;
                        listeFiche.add(uneFiche) ;
                    }
                    unRisque.setListeFiche(listeFiche) ;
                }

                boolean supprimer = cursor.getInt(cursor.getColumnIndex(KEY_RISQUE_SUPPRIMER)) > 0  ;
                unRisque.setSupprimer(supprimer)                                                    ;
                unRisque.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)))      ;
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Erreur : " + e.toString()) ;
        }
        finally {
            db.close()      ;
            cursor.close()  ;
        }

        return unRisque ;
    }

    public ArrayList<Risque> getAllRisques(boolean pFiches) {
        ArrayList<Risque> lesRisques = new ArrayList<>() ;
        String selectQuery = "SELECT * FROM " + TABLE_RISQUE + " ;" ;

        Log.d(TAG, selectQuery) ;

        SQLiteDatabase db = this.getReadableDatabase() ;

        Cursor cursor = db.rawQuery(selectQuery, null) ;
        cursor.moveToFirst() ;

        // Ajout de chaque ligne à la liste
        try {
            while (cursor.moveToNext()) {

                Risque unRisque = new Risque();
                unRisque.setId(cursor.getString(cursor.getColumnIndex(KEY_RISQUE_ID)))          ;
                unRisque.setTitre(cursor.getString(cursor.getColumnIndex(KEY_RISQUE_TITRE)))    ;
                unRisque.setResume(cursor.getString(cursor.getColumnIndex(KEY_RISQUE_RESUME)))  ;

                unRisque.setId(cursor.getString(cursor.getColumnIndex(KEY_RISQUE_ID)))          ;
                unRisque.setTitre(cursor.getString(cursor.getColumnIndex(KEY_RISQUE_TITRE)))    ;
                unRisque.setResume(cursor.getString(cursor.getColumnIndex(KEY_RISQUE_RESUME)))  ;

                //Si on veut récupérer la liste des fiches pour chaque risques
                if(pFiches) {

                    ArrayList<FicheRisque> listeFicheRisque = getFicheRisqueByRisque(cursor.getString(cursor.getColumnIndex(KEY_RISQUE_ID))) ;
                    ArrayList<Fiche> listeFiche = new ArrayList<>() ;

                    for(FicheRisque uneFicheRisque : listeFicheRisque) {
                        Fiche uneFiche = getFiche(uneFicheRisque.getUneFiche().getId(), false) ;
                        listeFiche.add(uneFiche) ;
                    }
                    unRisque.setListeFiche(listeFiche) ;
                }

                boolean supprimer = cursor.getInt(cursor.getColumnIndex(KEY_RISQUE_SUPPRIMER)) > 0  ;
                unRisque.setSupprimer(supprimer)                                                    ;
                unRisque.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)))      ;

                // Ajouter à la liste des risques
                lesRisques.add(unRisque) ;
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Erreur : " + e.toString()) ;
        }
        finally {
            db.close()      ;
            cursor.close()  ;
        }

        return lesRisques ;
    }

    public int getNbRisque() {
        SQLiteDatabase db = this.getReadableDatabase() ;

        String countQuery = "SELECT * FROM " + TABLE_RISQUE + " ;" ;

        Cursor cursor = db.rawQuery(countQuery, null) ;
        cursor.close() ;

        //On retourne le nombre de lignes
        return cursor.getCount();
    }

    /*
        ------------------------------------------------------------------
                                    FICHE
        ------------------------------------------------------------------
    */

    public void addFiche(Fiche pFiche) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues values = new ContentValues()                          ;
        values.put(KEY_FICHE_ID, pFiche.getId())                            ;
        values.put(KEY_FICHE_CHANTIER, pFiche.getUnChantier().getCode())    ;
        values.put(KEY_FICHE_UTI, pFiche.getUnUtilisateur().getId())        ;
        values.put(KEY_FICHE_DATE, pFiche.getDate())                        ;
        values.put(KEY_FICHE_SUPPRIMER, pFiche.isSupprimer())               ;
        values.put(KEY_CREATED_AT, getDateTime())                           ;

        //On insert la ligne
        db.insertWithOnConflict(TABLE_FICHE, null, values, SQLiteDatabase.CONFLICT_REPLACE) ;
        db.close() ;

        Log.d(TAG, "Une fiche a été ajoutée !") ;
    }

    public void updateFiche(Fiche pFiche) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues values = new ContentValues()                          ;
        values.put(KEY_FICHE_ID, pFiche.getId())                            ;
        values.put(KEY_FICHE_CHANTIER, pFiche.getUnChantier().getCode())    ;
        values.put(KEY_FICHE_UTI, pFiche.getUnUtilisateur().getId())        ;
        values.put(KEY_FICHE_DATE, pFiche.getDate())                        ;
        values.put(KEY_FICHE_SUPPRIMER, pFiche.isSupprimer())               ;
        values.put(KEY_CREATED_AT, getDateTime())                           ;

        //On met à jour la ligne
        db.update(TABLE_FICHE, values, KEY_FICHE_ID + " = ?",
                new String[]{String.valueOf(pFiche.getId())}) ;
        db.close() ;

        Log.d(TAG, "Une fiche a été modifiée !") ;
    }

    public void deleteFiche(Fiche pFiche) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        db.delete(TABLE_FICHE, KEY_FICHE_ID + " = ?",
                new String[]{String.valueOf(pFiche.getId())}) ;
        db.close() ;

        Log.d(TAG, "Une fiche a été supprimée !") ;
    }

    public Fiche getFiche(String pCode, boolean pRisques) {
        SQLiteDatabase db = this.getReadableDatabase() ;

        String selectQuery = "SELECT * FROM " + TABLE_FICHE + " " +
                             "WHERE " + KEY_FICHE_ID + " = " + pCode + " ;" ;

        Log.d(TAG, selectQuery) ;

        Cursor cursor = db.rawQuery(selectQuery, null) ;
        cursor.moveToFirst() ;

        Fiche uneFiche = new Fiche() ;

        try {
            if (cursor.getCount() > 0) {

                Chantier unChantier = getChantier(cursor.getString(cursor.getColumnIndex(KEY_FICHE_CHANTIER)))          ;
                Utilisateur unUtilisateur = getUtilisateur(cursor.getString(cursor.getColumnIndex(KEY_FICHE_UTI)))      ;

                uneFiche.setId(cursor.getString(cursor.getColumnIndex(KEY_FICHE_ID)))   ;
                uneFiche.setUnChantier(unChantier)                                      ;
                uneFiche.setUnUtilisateur(unUtilisateur)                                ;

                //Si on veut récupérer la liste des risques pour chaque fiches
                if(pRisques) {

                    ArrayList<FicheRisque> listeFicheRisque = getFicheRisqueByFiche(cursor.getString(cursor.getColumnIndex(KEY_FICHE_ID))) ;
                    ArrayList<Risque> listeRisque = new ArrayList<>() ;

                    for (FicheRisque uneFicheRisque : listeFicheRisque) {
                        Risque unRisque = getRisque(uneFicheRisque.getUnRisque().getId(), false) ;
                        listeRisque.add(unRisque) ;
                    }
                    uneFiche.setListeRisque(listeRisque) ;
                }

                uneFiche.setDate(cursor.getString(cursor.getColumnIndex(KEY_FICHE_DATE)))           ;
                boolean supprimer = cursor.getInt(cursor.getColumnIndex(KEY_FICHE_SUPPRIMER)) > 0   ;
                uneFiche.setSupprimer(supprimer)                                                    ;
                uneFiche.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)))      ;
            }
        } catch (Exception e) {
            Log.e(TAG, "Erreur : " + e.toString()) ;
        }
        finally {
            db.close()      ;
            cursor.close()  ;
        }

        return uneFiche ;
    }

    public ArrayList<Fiche> getAllFiches(boolean pRisques) {
        ArrayList<Fiche> lesFiches = new ArrayList<>() ;

        String selectQuery = "SELECT * FROM " + TABLE_FICHE + " ;" ;

        Log.d(TAG, selectQuery) ;

        SQLiteDatabase db = this.getReadableDatabase() ;

        Cursor cursor = db.rawQuery(selectQuery, null) ;
        cursor.moveToFirst() ;

        // Ajout de chaque ligne à la liste
        try {
            while (cursor.moveToNext()) {

                Chantier unChantier = getChantier(cursor.getString(cursor.getColumnIndex(KEY_FICHE_CHANTIER)))          ;
                Utilisateur unUtilisateur = getUtilisateur(cursor.getString(cursor.getColumnIndex(KEY_FICHE_UTI)))      ;

                Fiche uneFiche = new Fiche() ;

                uneFiche.setId(cursor.getString(cursor.getColumnIndex(KEY_FICHE_ID)))   ;
                uneFiche.setUnChantier(unChantier)                                      ;
                uneFiche.setUnUtilisateur(unUtilisateur)                                ;

                //Si on veut récupérer la liste des risques pour chaque fiches
                if(pRisques) {

                    ArrayList<FicheRisque> listeFicheRisque = getFicheRisqueByFiche(cursor.getString(cursor.getColumnIndex(KEY_FICHE_ID))) ;
                    ArrayList<Risque> listeRisque = new ArrayList<>() ;

                    for (FicheRisque uneFicheRisque : listeFicheRisque) {
                        Risque unRisque = getRisque(uneFicheRisque.getUnRisque().getId(), false) ;
                        listeRisque.add(unRisque) ;
                    }
                    uneFiche.setListeRisque(listeRisque) ;
                }

                uneFiche.setDate(cursor.getString(cursor.getColumnIndex(KEY_FICHE_DATE)))           ;
                boolean supprimer = cursor.getInt(cursor.getColumnIndex(KEY_FICHE_SUPPRIMER)) > 0   ;
                uneFiche.setSupprimer(supprimer)                                                    ;
                uneFiche.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)))      ;

                // Ajouter à la liste des risques
                lesFiches.add(uneFiche) ;
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Erreur : " + e.toString()) ;
        }
        finally {
            db.close()      ;
            cursor.close()  ;
        }

        return lesFiches ;
    }

    public int getNbFiche() {
        SQLiteDatabase db = this.getReadableDatabase() ;

        String countQuery = "SELECT * FROM " + TABLE_FICHE + " ;" ;

        Cursor cursor = db.rawQuery(countQuery, null) ;
        cursor.close() ;

        //On retourne le nombre de lignes
        return cursor.getCount();
    }

    /*
        ------------------------------------------------------------------
                                    FONCTION
        ------------------------------------------------------------------
    */

    public void addFonction(Fonction pFonction) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues values = new ContentValues()                      ;
        values.put(KEY_FONCTION_ID, pFonction.getId())                  ;
        values.put(KEY_FONCTION_LIBELLE, pFonction.getLibelle())        ;
        values.put(KEY_FONCTION_SUPPRIMER, pFonction.isSupprimer())     ;
        values.put(KEY_CREATED_AT, getDateTime())                       ;

        //On insert la ligne
        db.insertWithOnConflict(TABLE_FONCTION, null, values, SQLiteDatabase.CONFLICT_REPLACE) ;
        db.close() ;

        Log.d(TAG, "Une fonction a été ajoutée !") ;
    }

    public void updateFonction(Fonction pFonction) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues values = new ContentValues()                      ;
        values.put(KEY_FONCTION_ID, pFonction.getId())                  ;
        values.put(KEY_FONCTION_LIBELLE, pFonction.getLibelle())        ;
        values.put(KEY_FONCTION_SUPPRIMER, pFonction.isSupprimer())     ;
        values.put(KEY_CREATED_AT, getDateTime())                       ;

        //On met à jour la ligne
        db.update(TABLE_FONCTION, values, KEY_FONCTION_ID + " = ?",
                new String[]{String.valueOf(pFonction.getId() )}) ;
        db.close() ;

        Log.d(TAG, "Une fonction a été modifiée !") ;
    }

    public void deleteFonction(Fonction pFonction) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        db.delete(TABLE_FONCTION, KEY_FONCTION_ID + " = ?",
                new String[]{String.valueOf(pFonction.getId())}) ;
        db.close() ;

        Log.d(TAG, "Une fonction a été supprimée !") ;
    }

    public Fonction getFonction(String pCode) {
        SQLiteDatabase db = this.getReadableDatabase() ;

        String selectQuery = "SELECT * FROM " + TABLE_FONCTION + " " +
                             "WHERE " + KEY_FONCTION_ID + " = " + pCode + " ;" ;

        Log.d(TAG, selectQuery) ;

        Cursor cursor = db.rawQuery(selectQuery, null) ;
        cursor.moveToFirst() ;

        Fonction uneFonction = new Fonction() ;

        try {
            if (cursor.getCount() > 0) {

                uneFonction.setId(cursor.getString(cursor.getColumnIndex(KEY_FONCTION_ID)))             ;
                uneFonction.setLibelle(cursor.getString(cursor.getColumnIndex(KEY_FONCTION_LIBELLE)))   ;
                boolean supprimer = cursor.getInt(cursor.getColumnIndex(KEY_FONCTION_SUPPRIMER)) > 0    ;
                uneFonction.setSupprimer(supprimer)                                                     ;
                uneFonction.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)))       ;
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Erreur : " + e.toString()) ;
        }
        finally {
            db.close()      ;
            cursor.close()  ;
        }

        return uneFonction ;
    }

    public ArrayList<Fonction> getAllFonctions() {
        ArrayList<Fonction> lesFonctions = new ArrayList<>() ;
        String selectQuery = "SELECT * FROM " + TABLE_FONCTION + " ;" ;

        Log.d(TAG, selectQuery) ;

        SQLiteDatabase db = this.getReadableDatabase() ;

        Cursor cursor = db.rawQuery(selectQuery, null) ;
        cursor.moveToFirst() ;

        // Ajout de chaque ligne à la liste
        try {
            while (cursor.moveToNext()) {

                Fonction uneFonction = new Fonction() ;
                uneFonction.setId(cursor.getString(cursor.getColumnIndex(KEY_FONCTION_ID)))             ;
                uneFonction.setLibelle(cursor.getString(cursor.getColumnIndex(KEY_FONCTION_LIBELLE)))   ;
                boolean supprimer = cursor.getInt(cursor.getColumnIndex(KEY_FONCTION_SUPPRIMER)) > 0    ;
                uneFonction.setSupprimer(supprimer)                                                     ;
                uneFonction.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)))       ;

                // Ajouter à la liste des risques
                lesFonctions.add(uneFonction) ;
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Erreur : " + e.toString()) ;
        }
        finally {
            db.close()      ;
            cursor.close()  ;
        }

        return lesFonctions ;
    }

    public int getNbFonction() {
        SQLiteDatabase db = this.getReadableDatabase() ;

        String countQuery = "SELECT * FROM " + TABLE_FONCTION + " ;" ;

        Cursor cursor = db.rawQuery(countQuery, null) ;
        cursor.close() ;

        //On retourne le nombre de lignes
        return cursor.getCount();
    }

    /*
        ------------------------------------------------------------------
                                    UTILISATEUR
        ------------------------------------------------------------------
    */

    public void addUtilisateur(Utilisateur pUtilisateur) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues values = new ContentValues()                          ;
        values.put(KEY_UTI_ID, pUtilisateur.getId())                        ;
        values.put(KEY_UTI_NOM, pUtilisateur.getNom())                      ;
        values.put(KEY_UTI_PRENOM, pUtilisateur.getPrenom())                ;
        values.put(KEY_UTI_EMAIL, pUtilisateur.getMail())                   ;
        values.put(KEY_UTI_FONCTION, pUtilisateur.getUneFonction().getId()) ;
        values.put(KEY_UTI_SUPPRIMER, pUtilisateur.isSupprimer())           ;
        values.put(KEY_CREATED_AT, getDateTime())                           ;

        //On insert la ligne
        db.insertWithOnConflict(TABLE_UTILISATEUR, null, values, SQLiteDatabase.CONFLICT_REPLACE) ;
        db.close() ;

        Log.d(TAG, "Un utilisateur a été ajouté !") ;
    }

    public void updateUtilisateur(Utilisateur pUtilisateur) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues values = new ContentValues()                          ;
        values.put(KEY_UTI_ID, pUtilisateur.getId())                        ;
        values.put(KEY_UTI_NOM, pUtilisateur.getNom())                      ;
        values.put(KEY_UTI_PRENOM, pUtilisateur.getPrenom())                ;
        values.put(KEY_UTI_EMAIL, pUtilisateur.getMail())                   ;
        values.put(KEY_UTI_FONCTION, pUtilisateur.getUneFonction().getId()) ;
        values.put(KEY_UTI_SUPPRIMER, pUtilisateur.isSupprimer())           ;
        values.put(KEY_CREATED_AT, getDateTime())                           ;

        //On met à jour la ligne
        db.update(TABLE_UTILISATEUR, values, KEY_UTI_ID + " = ?",
                new String[]{String.valueOf(pUtilisateur.getId())}) ;
        db.close() ;

        Log.d(TAG, "Un utilisateur a été modifié !") ;
    }

    public void deleteUtilisateur(Utilisateur pUtilisateur) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        db.delete(TABLE_UTILISATEUR, KEY_UTI_ID + " = ?",
                new String[]{String.valueOf(pUtilisateur.getId())}) ;
        db.close() ;

        Log.d(TAG, "Un utilisateur a été supprimé !") ;
    }

    public Utilisateur getUtilisateur(String pCode) {
        SQLiteDatabase db = this.getReadableDatabase() ;

        String selectQuery = "SELECT * FROM " + TABLE_UTILISATEUR + " " +
                             "WHERE " + KEY_UTI_ID + " = " + pCode + " ;" ;

        Log.d(TAG, selectQuery) ;

        Cursor cursor = db.rawQuery(selectQuery, null) ;
        cursor.moveToFirst() ;

        Utilisateur unUtilisateur = new Utilisateur() ;

        try {
            if (cursor.getCount() > 0) {

                Fonction uneFonction = getFonction(cursor.getString(cursor.getColumnIndex(KEY_UTI_FONCTION)))   ;

                unUtilisateur.setId(cursor.getString(cursor.getColumnIndex(KEY_UTI_ID)))                        ;
                unUtilisateur.setNom(cursor.getString(cursor.getColumnIndex(KEY_UTI_NOM)))                      ;
                unUtilisateur.setPrenom(cursor.getString(cursor.getColumnIndex(KEY_UTI_PRENOM)))                ;
                unUtilisateur.setMail(cursor.getString(cursor.getColumnIndex(KEY_UTI_EMAIL)))                   ;
                unUtilisateur.setUneFonction(uneFonction)                                                       ;
                boolean supprimerUti = cursor.getInt(cursor.getColumnIndex(KEY_UTI_SUPPRIMER)) > 0              ;
                unUtilisateur.setSupprimer(supprimerUti)                                                        ;
                unUtilisateur.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)))             ;
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Erreur : " + e.toString()) ;
        }
        finally {
            db.close()      ;
            cursor.close()  ;
        }

        return unUtilisateur ;
    }

    public ArrayList<Utilisateur> getAllUtilisateurs() {
        ArrayList<Utilisateur> lesUtilisateurs = new ArrayList<>() ;
        String selectQuery = "SELECT * FROM " + TABLE_UTILISATEUR + " ;" ;

        Log.d(TAG, selectQuery) ;

        SQLiteDatabase db = this.getReadableDatabase() ;

        Cursor cursor = db.rawQuery(selectQuery, null) ;
        cursor.moveToFirst() ;

        // Ajout de chaque ligne à la liste
        try {
            while (cursor.moveToNext()) {
                Fonction uneFonction = getFonction(cursor.getString(cursor.getColumnIndex(KEY_UTI_FONCTION)))   ;

                Utilisateur unUtilisateur = new Utilisateur()                                                   ;
                unUtilisateur.setId(cursor.getString(cursor.getColumnIndex(KEY_UTI_ID)))                        ;
                unUtilisateur.setNom(cursor.getString(cursor.getColumnIndex(KEY_UTI_NOM)))                      ;
                unUtilisateur.setPrenom(cursor.getString(cursor.getColumnIndex(KEY_UTI_PRENOM)))                ;
                unUtilisateur.setMail(cursor.getString(cursor.getColumnIndex(KEY_UTI_EMAIL)))                   ;
                unUtilisateur.setUneFonction(uneFonction)                                                       ;
                boolean supprimer = cursor.getInt(cursor.getColumnIndex(KEY_UTI_SUPPRIMER)) > 0                 ;
                unUtilisateur.setSupprimer(supprimer)                                                           ;
                unUtilisateur.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)))             ;

                // Ajouter à la liste des risques
                lesUtilisateurs.add(unUtilisateur) ;
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Erreur : " + e.toString()) ;
        }
        finally {
            db.close()      ;
            cursor.close()  ;
        }

        return lesUtilisateurs ;
    }

    public int getNbUtilisateur() {
        SQLiteDatabase db = this.getReadableDatabase() ;

        String countQuery = "SELECT * FROM " + TABLE_UTILISATEUR + " ;" ;

        Cursor cursor = db.rawQuery(countQuery, null) ;
        cursor.close() ;

        //On retourne le nombre de lignes
        return cursor.getCount();
    }

    /*
        ------------------------------------------------------------------
                                    FICHE RISQUE
        ------------------------------------------------------------------
    */

    public void addFicheRisque(FicheRisque pFicheRisque) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues values = new ContentValues()                              ;
        values.put(KEY_FICHE_RISQUE_FICHE, pFicheRisque.getUneFiche().getId())  ;
        values.put(KEY_FICHE_RISQUE_RISQUE, pFicheRisque.getUnRisque().getId()) ;
        values.put(KEY_FICHE_RISQUE_SUPPRIMER, pFicheRisque.isSupprimer())      ;
        values.put(KEY_CREATED_AT, getDateTime())                               ;

        //On met à jour la ligne
        db.insertWithOnConflict(TABLE_FICHE_RISQUE, null, values, SQLiteDatabase.CONFLICT_REPLACE) ;
        db.close() ;

        Log.d(TAG, "Une fiche risque a été ajoutée !") ;
    }

    public void updateFicheRisqueByFiche(FicheRisque pFicheRisque) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues values = new ContentValues()                              ;
        values.put(KEY_FICHE_RISQUE_FICHE, pFicheRisque.getUneFiche().getId())  ;
        values.put(KEY_FICHE_RISQUE_RISQUE, pFicheRisque.getUnRisque().getId()) ;
        values.put(KEY_FICHE_RISQUE_SUPPRIMER, pFicheRisque.isSupprimer())      ;
        values.put(KEY_CREATED_AT, getDateTime())                               ;

        //On met à jour la ligne
        db.update(TABLE_FICHE_RISQUE, values, KEY_FICHE_RISQUE_FICHE + " = ?",
                new String[]{String.valueOf(pFicheRisque.getUneFiche().getId())}) ;
        db.close() ;

        Log.d(TAG, "Une fiche risque a été modifiée !") ;
    }

    public void updateFicheRisqueByRisque(FicheRisque pFicheRisque) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues values = new ContentValues()                              ;
        values.put(KEY_FICHE_RISQUE_FICHE, pFicheRisque.getUneFiche().getId())  ;
        values.put(KEY_FICHE_RISQUE_RISQUE, pFicheRisque.getUnRisque().getId()) ;
        values.put(KEY_FICHE_RISQUE_SUPPRIMER, pFicheRisque.isSupprimer())      ;
        values.put(KEY_CREATED_AT, getDateTime())                               ;

        //On met à jour la ligne
        db.update(TABLE_FICHE_RISQUE, values, KEY_FICHE_RISQUE_RISQUE + " = ?",
                new String[]{String.valueOf(pFicheRisque.getUnRisque().getId() )}) ;
        db.close() ;

        Log.d(TAG, "Une fiche risque a été modifiée !") ;
    }

    public void deleteFicheRisqueByFiche(FicheRisque pFicheRisque) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        db.delete(TABLE_FICHE_RISQUE, KEY_FICHE_RISQUE_FICHE + " = ?",
                new String[]{String.valueOf(pFicheRisque.getUneFiche().getId())}) ;
        db.close() ;

        Log.d(TAG, "Une fiche risque a été supprimée !") ;
    }

    public void deleteFicheRisqueByRisque(FicheRisque pFicheRisque) {
        SQLiteDatabase db = this.getWritableDatabase() ;

        db.delete(TABLE_FICHE_RISQUE, KEY_FICHE_RISQUE_RISQUE + " = ?",
                new String[]{String.valueOf(pFicheRisque.getUnRisque().getId() )}) ;
        db.close() ;

        Log.d(TAG, "Une fiche risque a été supprimée !") ;
    }

    public ArrayList<FicheRisque> getFicheRisqueByFiche(String pCode) {
        ArrayList<FicheRisque> lesFicheRisques = new ArrayList<>() ;

        String selectQuery = "SELECT * FROM " + TABLE_FICHE_RISQUE + " " +
                             "WHERE " + KEY_FICHE_RISQUE_FICHE + " = " + pCode + " ;" ;

        Log.d(TAG, selectQuery) ;

        SQLiteDatabase db = this.getReadableDatabase() ;

        Cursor cursor = db.rawQuery(selectQuery, null) ;
        cursor.moveToFirst() ;

        // Ajout de chaque ligne à la liste
        try {
            while (cursor.moveToNext()) {

                Fiche uneFiche = getFiche(cursor.getString(cursor.getColumnIndex(KEY_FICHE_RISQUE_FICHE)), false)       ;
                Risque unRisque = getRisque(cursor.getString(cursor.getColumnIndex(KEY_FICHE_RISQUE_RISQUE)), false)    ;

                FicheRisque uneFicheRisque = new FicheRisque()                                                          ;
                uneFicheRisque.setUneFiche(uneFiche)                                                                    ;
                uneFicheRisque.setUnRisque(unRisque)                                                                    ;
                boolean supprimer = cursor.getInt(cursor.getColumnIndex(KEY_FICHE_RISQUE_SUPPRIMER)) > 0                ;
                uneFicheRisque.setSupprimer(supprimer)                                                                  ;
                uneFicheRisque.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)))                    ;

                // Ajouter à la liste des risques
                lesFicheRisques.add(uneFicheRisque) ;
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Erreur : " + e.toString()) ;
        }
        finally {
            db.close()      ;
            cursor.close()  ;
        }

        return lesFicheRisques ;
    }

    public ArrayList<FicheRisque> getFicheRisqueByRisque(String pCode) {
        ArrayList<FicheRisque> lesFicheRisques = new ArrayList<>() ;

        String selectQuery = "SELECT * FROM " + TABLE_FICHE_RISQUE + " " +
                             "WHERE " + KEY_FICHE_RISQUE_RISQUE + " = " + pCode + " ;" ;

        Log.d(TAG, selectQuery) ;

        SQLiteDatabase db = this.getReadableDatabase() ;

        Cursor cursor = db.rawQuery(selectQuery, null) ;
        cursor.moveToFirst() ;

        // Ajout de chaque ligne à la liste
        try {
            while (cursor.moveToNext()) {

                Fiche uneFiche = getFiche(cursor.getString(cursor.getColumnIndex(KEY_FICHE_RISQUE_FICHE)), false)       ;
                Risque unRisque = getRisque(cursor.getString(cursor.getColumnIndex(KEY_FICHE_RISQUE_RISQUE)), false)    ;

                FicheRisque uneFicheRisque = new FicheRisque()                                                          ;
                uneFicheRisque.setUneFiche(uneFiche)                                                                    ;
                uneFicheRisque.setUnRisque(unRisque)                                                                    ;
                boolean supprimer = cursor.getInt(cursor.getColumnIndex(KEY_FICHE_RISQUE_SUPPRIMER)) > 0                ;
                uneFicheRisque.setSupprimer(supprimer)                                                                  ;
                uneFicheRisque.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)))                    ;

                // Ajouter à la liste des risques
                lesFicheRisques.add(uneFicheRisque) ;
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Erreur : " + e.toString()) ;
        }
        finally {
            db.close() ;
            cursor.close() ;
        }

        return lesFicheRisques ;
    }

    public ArrayList<FicheRisque> getAllFicheRisques() {
        ArrayList<FicheRisque> lesFicheRisques = new ArrayList<>() ;

        String selectQuery = "SELECT * FROM " + TABLE_FICHE_RISQUE + " ;" ;

        Log.d(TAG, selectQuery) ;

        SQLiteDatabase db = this.getReadableDatabase() ;

        Cursor cursor = db.rawQuery(selectQuery, null) ;
        cursor.moveToFirst() ;

        // Ajout de chaque ligne à la liste
        try {
            while (cursor.moveToNext()) {

                Fiche uneFiche = getFiche(cursor.getString(cursor.getColumnIndex(KEY_FICHE_RISQUE_FICHE)), false)       ;
                Risque unRisque = getRisque(cursor.getString(cursor.getColumnIndex(KEY_FICHE_RISQUE_RISQUE)), false)    ;

                FicheRisque uneFicheRisque = new FicheRisque()                                                          ;
                uneFicheRisque.setUneFiche(uneFiche)                                                                    ;
                uneFicheRisque.setUnRisque(unRisque)                                                                    ;
                boolean supprimer = cursor.getInt(cursor.getColumnIndex(KEY_FICHE_RISQUE_SUPPRIMER)) > 0                ;
                uneFicheRisque.setSupprimer(supprimer)                                                                  ;
                uneFicheRisque.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)))                    ;

                // Ajouter à la liste des risques
                lesFicheRisques.add(uneFicheRisque) ;
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Erreur : " + e.toString()) ;
        }
        finally {
            db.close()      ;
            cursor.close()  ;
        }

        return lesFicheRisques ;
    }

    public int getNbFicheRisque() {
        SQLiteDatabase db = this.getReadableDatabase() ;

        String countQuery = "SELECT * FROM " + TABLE_FICHE_RISQUE + " ;" ;

        Cursor cursor = db.rawQuery(countQuery, null) ;
        cursor.close() ;

        //On retourne le nombre de lignes
        return cursor.getCount();
    }
}
