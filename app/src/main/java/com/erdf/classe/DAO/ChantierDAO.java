package com.erdf.classe.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.erdf.classe.SQLite.DatabaseHelper;
import com.erdf.classe.metier.Chantier;
import com.erdf.classe.technique.ConnexionControleur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Radhan on 19/04/2016.
 */
public class ChantierDAO {
    private static String TAG = ChantierDAO.class.getSimpleName() ;
    private static DatabaseHelper db ;
    static String urlAllChantiers = "http://comment-telecharger.eu/ERDF/getAllChantiers.php" ;
    static String urlSetChantier = "http://comment-telecharger.eu/ERDF/setUnChantier.php" ;

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

    public static String getDernierIdChantier(Context unContext) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        return db.getDernierIdChantier() ;
    }

    public static void setUnChantier(final Context pContext, final Chantier unChantier, boolean online) {

        //Si c'est en ligne alors on ajoute à MySql
        if(online) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSetChantier, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    Log.d(TAG, response);
                    try {

                        JSONObject oJson = new JSONObject(response);
                        if (oJson.getString("RESULTAT").equals("OK")) {
                            Toast.makeText(pContext, "Ajout d'un chantier réussi", Toast.LENGTH_SHORT).show();
                            syncGetListeChantier(pContext);
                        } else {
                            Toast.makeText(pContext, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(pContext, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("code", unChantier.getCode());
                    params.put("libelle", unChantier.getLibelle());
                    params.put("numRue", unChantier.getNumRue());
                    params.put("rue", unChantier.getRue());
                    params.put("ville", unChantier.getVille());
                    params.put("codePostal", unChantier.getCodePostal());

                    return params;
                }
            };

            // On ajoute la requête à la file d'attente
            ConnexionControleur.getInstance().addToRequestQueue(stringRequest);
        }
        else {
            // SQLite database handler
            db = new DatabaseHelper(pContext) ;

            db.setUnChantier(unChantier) ;
        }
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
                        setUnChantier(unContext, unChantier, false) ;
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
