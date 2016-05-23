package com.erdf.classe.technique;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.erdf.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Radhan on 22/03/2016.
 */
public class ConnexionBDD extends AsyncTask<Void, String, String>  {
    private static final String TAG = "CONNEXION_BDD" ;

    ArrayList<String> nomParametres = new ArrayList<>() ;
    ArrayList<String> valeurParametres = new ArrayList<>() ;
    String resultatJson = "" ;
    String besoin = "" ;

    //Interface qui reçoit les données
    public GetResponse getResponse = null;

    //Pour afficher un popup de progression lorsque ça charge
    Activity activity;
    private ProgressDialog Dialog ;
    boolean hasProgress = true ;

    public ConnexionBDD(ArrayList<String> nomParametres, ArrayList<String> valeurParametres, String besoin, Activity activity1) {
        this.nomParametres = nomParametres;
        this.valeurParametres = valeurParametres;
        this.besoin = besoin;
        this.activity = activity1 ;
    }

    public ConnexionBDD(String besoin, Activity activity1) {
        this.nomParametres = null;
        this.valeurParametres = null;
        this.besoin = besoin;
        this.activity = activity1 ;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(hasProgress) {
            if (Dialog == null) {
                Dialog = new ProgressDialog(activity);
                Dialog.setMessage("Chargement..");
                Dialog.setIndeterminate(true);
                Dialog.setCancelable(false);
                Dialog.show();
            }
        }
    }

    @Override
    protected String doInBackground(Void... voids) {

        //On assemble les paramètres pour les passer dans la requête
        String lesParametres = "";
        if (nomParametres != null && valeurParametres != null) {
            lesParametres = getParametres(nomParametres, valeurParametres);
            Log.i(TAG, "Paramètres : " + lesParametres);
        }

        //On défini l'URL à utiliser en fonction de nos besoins
        String URL = getURL(besoin);

        //On initialise les variables
        HttpURLConnection urlConnection = null;

        try {
            //On définie les informations de connexion
            URL url = new URL(URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            //On écrit les paramètres dans la requête
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.writeBytes(lesParametres);
            wr.flush();
            wr.close();

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    resultatJson += line;
                    Log.i("Connexion", "Resultat : " + line);
                }

                in.close();
                return resultatJson ;
            }
        } catch (Exception e) {
            Log.e("Connexion", "Impossible d'établir la connexion", e);
            return "ERREUR_CONNEXION" ;
        } finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
        return null ;
    }

    @Override
    protected void onPostExecute(String resultat) {
        super.onPostExecute(resultat);

        if (Dialog != null) {
            Dialog.dismiss();
        }

        if(resultat != null) {
            if(resultat.equals("ERREUR_CONNEXION")) {

                AlertDialog.Builder adb = new AlertDialog.Builder(this.activity);
                adb.setTitle("Connexion Interrompue !");
                adb.setIcon(ContextCompat.getDrawable(this.activity, R.drawable.ic_warning_white_48dp)) ;
                adb.setMessage("La connexion a été interrompue, veuillez réessayer !");
                adb.setCancelable(true);
                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                adb.show();
            } else {
                getResponse.getData(resultat);
            }
        }
    }

    public boolean isJSON() {
        boolean isJson = false ;
        if(this.resultatJson != null && this.resultatJson.contains("{")) {
            isJson = true ;
        }
        return(isJson) ;
    }

    public void setProgress(boolean statut) {
        this.hasProgress = statut ;
    }

    private String getParametres(ArrayList<String> nomParametre, ArrayList<String> valeurParametre)
    {
        String lesParametres = "" ;
        if(nomParametre.size() == valeurParametre.size())
        {
            int i = 0 ;
            while(i < nomParametre.size())
            {
                if(lesParametres.isEmpty())
                {
                    lesParametres = nomParametre.get(i);
                    lesParametres += "=" ;
                }
                else if(lesParametres.lastIndexOf("=") == (lesParametres.length() - 1))
                {
                    lesParametres += valeurParametre.get(i);
                    i++ ;
                }
                else if(lesParametres.lastIndexOf("=") != (lesParametres.length() - 1))
                {
                    lesParametres += "&" ;
                    lesParametres += nomParametre.get(i);
                    lesParametres += "=" ;
                }
            }
        }
        return lesParametres ;
    }

    private String getURL(String besoin)
    {
        String URL = "" ;
        final String Domaine = "http://comment-telecharger.eu/ERDF/" ;

        final String CONNEXION_URL           = Domaine + "connexion.php" ;
        final String RISQUE_URL              = Domaine + "getAllRisques.php" ;
        final String FICHE_URL               = Domaine + "getAllFiches.php" ;
        final String CHANTIER_URL            = Domaine + "getAllChantiers.php" ;
        final String SAISIR_FICHE_URL        = Domaine + "setUneFiche.php" ;
        final String SAISIR_UTILISATEUR_URL  = Domaine + "setUnUtilisateur.php" ;

        switch(besoin)
        {
            case "Connexion" :
                URL = CONNEXION_URL ;
            break ;

            case "Risque" :
                URL = RISQUE_URL ;
            break ;

            case "Fiche" :
                URL = FICHE_URL ;
            break ;

            case "Chantier" :
                URL = CHANTIER_URL ;
            break ;

            case "SaisirFiche" :
                URL = SAISIR_FICHE_URL ;
            break ;

            case "SaisirUtilisateur" :
                URL = SAISIR_UTILISATEUR_URL ;
            break ;
        }

        return URL ;
    }
}
