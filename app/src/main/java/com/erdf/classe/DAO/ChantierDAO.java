package com.erdf.classe.DAO;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.erdf.classe.SQLite.DatabaseHelper;
import com.erdf.classe.metier.Chantier;
import com.erdf.classe.technique.ConnexionControleur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Radhan on 19/04/2016.
 */
public class ChantierDAO {
    private static String TAG = ChantierDAO.class.getSimpleName() ;
    private static DatabaseHelper db ;
    static String urlAllChantiers = "http://comment-telecharger.eu/ERDF/getAllChantiers.php" ;

    private ChantierDAO() {

    }

   public static ArrayList<Chantier> getListeChantier(Context unContext) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        return db.getAllChantiers() ;
    }

    public static Chantier getUnChantier(Context unContext, String code) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        return db.getUnChantier(code) ;
    }

    public static void setUnChantier(Context unContext, Chantier unChantier) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        db.setUnChantier(unChantier) ;
    }

    public static void syncGetListeChantier(final Context unContext) {

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlAllChantiers, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    // On parcours les données reçues
                    for(int i = 1; i < response.length() + 1; i++) {
                        JSONObject oChantier = response.getJSONObject(Integer.toString(i)) ;
                        boolean supprimer = oChantier.getInt("cha_supprimer") > 0 ;
                        Chantier unChantier = new Chantier(oChantier.getString("cha_code"), oChantier.getString("cha_libelle"), oChantier.getString("cha_nrue"), oChantier.getString("cha_rue"), oChantier.getString("cha_ville"), oChantier.getString("cha_codepo"), supprimer) ;
                        setUnChantier(unContext, unChantier) ;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // On ajoute la requête à la file d'attente
        ConnexionControleur.getInstance().addToRequestQueue(jsonObjReq);
    }
}
