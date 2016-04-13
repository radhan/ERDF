package com.erdf.classe.technique;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Radhan on 22/03/2016.
 */
public class ParserJSON {
    private static final String TAG = "PARSER_JSON" ;

    private JSONObject oJSON ;
    private String resultatJSON ;

    public ParserJSON() {
    }

    public ParserJSON(String resultatJSON) {
        this.resultatJSON = resultatJSON.substring(resultatJSON.indexOf("{"), resultatJSON.lastIndexOf("}") + 1) ;
        try {
            this.oJSON = new JSONObject(this.resultatJSON);
        }catch (JSONException e) {
            Log.e(TAG, "Exception JSON inattendue", e);
        }
    }

    public ParserJSON(JSONObject pJSON, String champ)
    {
        try {
            this.oJSON = pJSON.getJSONObject(champ) ;
        }catch (JSONException e) {
            Log.e(TAG, "Exception JSON.getJSONObject inattendue", e);
        }
    }

    public JSONObject getOJSON() {
        return oJSON;
    }

    public String getResultatJSON() {
        return resultatJSON;
    }

    public void setResultatJSON(String resultatJSON) {
        this.resultatJSON = resultatJSON.substring(resultatJSON.indexOf("{"), resultatJSON.lastIndexOf("}") + 1) ;
    }

    public String getString(String nom) {
        try {
            return oJSON.getString(nom) ;
        }catch (JSONException e) {
            Log.e(TAG, "Exception JSON.getString inattendue", e);
            return "Erreur : " + e ;
        }
    }

    public int getInt(String nom) {
        try {
            return oJSON.getInt(nom) ;
        }catch (JSONException e) {
            Log.e(TAG, "Exception JSON.getInt inattendue", e);
            return 0 ;
        }
    }

    public boolean getBoolean(String nom) {
        try {
            return oJSON.getBoolean(nom) ;
        }catch (JSONException e) {
            Log.e(TAG, "Exception JSON.getBoolean inattendue", e);
            return false ;
        }
    }
}
