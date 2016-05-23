package com.erdf.classe.DAO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.erdf.FicheActivity;
import com.erdf.classe.SQLite.DatabaseHelper;
import com.erdf.classe.technique.ConnexionControleur;
import com.erdf.classe.technique.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Radhan on 24/04/2016.
 */
public class CompteDAO {
    private static String TAG = CompteDAO.class.getSimpleName() ;
    private static DatabaseHelper db ;
    static String urlConnexion = "http://comment-telecharger.eu/ERDF/connexion.php" ;

    private CompteDAO() {
    }

    public static void Connexion(final Context pContext, final String pLogin, final String pPassword) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlConnexion, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, response) ;
                try {

                    JSONObject oJson = new JSONObject(response) ;
                    if(oJson.getString("RESULTAT").equals("OK")) {
                        onLoginSuccess(pContext, oJson.getString("com_id")) ;
                    }
                    else {
                        onLoginFailed(pContext) ;
                    }

                } catch (JSONException e) {
                    e.printStackTrace() ;
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Erreur Connexion : " + error.toString()) ;
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>() ;
                params.put("login", pLogin) ;
                params.put("password", pPassword) ;

                return params;
            }
        };

        // On ajoute la requête à la file d'attente
        ConnexionControleur.getInstance().addToRequestQueue(stringRequest);
    }

    public static void onLoginSuccess(Context pContext, String pIdUtilisateur) {
        SessionManager session = new SessionManager(pContext) ;
        session.setLogin(true);
        session.setIdUtilisateur(pIdUtilisateur);

        Intent intent = new Intent(pContext, FicheActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        pContext.startActivity(intent);
        //((Activity)pContext).finish();

        Toast.makeText(pContext, "Vous êtes connecté !", Toast.LENGTH_LONG).show();
    }

    public static void onLoginFailed(Context pContext) {
        Toast.makeText(pContext, "Identifiants incorrect !", Toast.LENGTH_LONG).show();
    }
}
