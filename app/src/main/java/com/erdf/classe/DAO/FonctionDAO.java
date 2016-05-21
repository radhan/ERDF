package com.erdf.classe.DAO;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.erdf.classe.SQLite.DatabaseHelper;
import com.erdf.classe.metier.Fonction;
import com.erdf.classe.technique.ConnexionControleur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Radhan on 24/04/2016.
 */
public class FonctionDAO {
    private static String TAG = FonctionDAO.class.getSimpleName() ;
    private static DatabaseHelper db ;
    static String urlAllFonctions = "http://comment-telecharger.eu/ERDF/getAllFonctions.php" ;

    private FonctionDAO() {

    }

    public static ArrayList<Fonction> getListeFonction(Context unContext) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        return db.getAllFonctions() ;
    }

    public static Fonction getUneFonction(Context unContext, String code) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        return db.getUneFonction(code) ;
    }

    public static void setUneFonction(Context unContext, Fonction uneFonction) {
        // SQLite database handler
        db = new DatabaseHelper(unContext) ;

        db.setUneFonction(uneFonction) ;
    }

    public static void syncGetListeFonction(final Context unContext) {


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlAllFonctions, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    // On parcours les données reçues
                    for(int i = 1; i < response.length() ; i++) {
                        JSONObject oFonction = response.getJSONObject(Integer.toString(i)) ;
                        boolean supprimer = oFonction.getInt("fon_supprimer") > 0 ;
                        Fonction uneFonction = new Fonction(oFonction.getString("fon_id"), oFonction.getString("fon_libelle"), supprimer);
                        setUneFonction(unContext, uneFonction) ;
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
