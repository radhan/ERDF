package com.erdf.classe.technique;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Radhan on 01/05/2016.
 */
public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NOM        = "ERDFLogin"       ;
    private static final String KEY_CONNECTE    = "connexion"       ;
    private static final String KEY_USER_ID     = "idUtilisateur"   ;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NOM, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean pConnecte) {

        editor.putBoolean(KEY_CONNECTE, pConnecte);

        // commit changes
        editor.commit();

        Log.d(TAG, "Session de connexion modifié !");
    }

    public boolean isConnecte(){
        return pref.getBoolean(KEY_CONNECTE, false);
    }

    public void deconnexion() {
        editor.clear().apply();
    }

    public void setIdUtilisateur(String pIdUtilisateur) {

        editor.putString(KEY_USER_ID, pIdUtilisateur);

        // commit changes
        editor.commit();

        Log.d(TAG, "Id Utilisateur de la session modifié !");
    }

    public String getIdUtilisateur() {
        return pref.getString(KEY_USER_ID, "Inconnu") ;
    }
}
