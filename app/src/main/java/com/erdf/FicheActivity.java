package com.erdf;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.erdf.adapter.RisqueAdapter;
import com.erdf.classe.DAO.ChantierDAO;
import com.erdf.classe.DAO.FicheDAO;
import com.erdf.classe.DAO.RisqueDAO;
import com.erdf.classe.metier.Chantier;
import com.erdf.classe.metier.Fiche;
import com.erdf.classe.metier.Risque;
import com.erdf.classe.metier.Utilisateur;
import com.erdf.classe.technique.ConnexionBDD;
import com.erdf.classe.technique.InternetDetection;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FicheActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    List<ViewRisque> lesRisques = new ArrayList<>() ;
    ListView listviewRisque ;

    ArrayList<String> idChantiers = new ArrayList<>() ;
    ArrayList<String> lesChantiers = new ArrayList<>();

    @InjectView(R.id.sDate) TextView dateText ;
    @InjectView(R.id.sChantier) TextView chantierText ;
    @InjectView(R.id.spinnerChantier) Spinner spinnerChantier ;
    @InjectView(R.id.sAdresse) TextView adresseText ;
    @InjectView(R.id.bAdresse) Button btnAdresse ;
    @InjectView(R.id.button) Button btnEnvoyer ;

    Fiche uneFiche ;
    ArrayList<Risque> listeRisques = new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche);

        //NE PAS OUBLIER SI ON UTILISE ButterKnife
        ButterKnife.inject(this) ;

        //On déclare l'objet fiche
        uneFiche = new Fiche() ;

        //On définie le selected listener
        spinnerChantier.setOnItemSelectedListener(this) ;

        //Configuration button d'edition
        btnAdresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adresseText.setEnabled(!adresseText.isEnabled());
            }
        });

        //Configuration button d'envoie
        btnEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFiche();
            }
        });

        //On récupère la liste des risques et des chantiers
        getRisques() ;
        getChantiers() ;

        //On récupère la date et l'heure
        getDate() ;

        //On regarde si on dispose d'une connexion internet
        InternetDetection inter = new InternetDetection(getApplicationContext());
        Boolean isInternetPresent = inter.isConnectingToInternet(); // true or false

        //S'il dispose d'une connexion internet
        if(isInternetPresent) {
            //Si le GPS est activé, alors on va chercher la localisation
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if(manager.isProviderEnabled( LocationManager.GPS_PROVIDER)) {
                getLocalisation() ;
                adresseText.setEnabled(false);
            }
            else {
                adresseText.setEnabled(true);
            }
        }
        else {
            adresseText.setEnabled(true);
        }

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        chantierText.setText(idChantiers.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        chantierText.setText(0);
    }

    //Méthode qui récupère la date
    private void getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        dateText.setText(dateFormat.format(date));
    }

    //Méthode qui récupère la localisation
    private void getLocalisation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null) {
            getAdresse(location);
        }

        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                getAdresse(location);
            }

            public void onProviderDisabled(String arg0) {
            }

            public void onProviderEnabled(String arg0) {
            }

            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
    }

    //Méthode permettant d'avoir l'adresse
    private void getAdresse(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Geocoder maLocalisation = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> localisation ;
        try {
            localisation = maLocalisation.getFromLocation(latitude, longitude, 1);
            if(localisation.size() == 1) {
                adresseText.setText(localisation.get(0).getAddressLine(0) + ", " + localisation.get(0).getLocality() + ", " + localisation.get(0).getPostalCode()) ;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    //On récupère la liste des risques
    public void getRisques() {
        final RisqueDAO unRisqueDAO = new RisqueDAO(this) ;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                for (int i = 0; i < unRisqueDAO.getListeRisque().size(); i++) {
                    ViewRisque vRisque = new ViewRisque(unRisqueDAO.getListeRisque().get(i).getTitre(), unRisqueDAO.getListeRisque().get(i).getResume());
                    lesRisques.add(vRisque);
                }
                if (!lesRisques.isEmpty()) {
                    listviewRisque = (ListView) findViewById(R.id.listView);
                    RisqueAdapter adapter = new RisqueAdapter(FicheActivity.this, lesRisques);
                    listviewRisque.setAdapter(adapter);

                    listviewRisque.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView parent, View view, int position, long id) {
                            if (view != null) {
                                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                                checkBox.setChecked(!checkBox.isChecked());

                                Risque unRisque = new Risque(unRisqueDAO.getListeRisque().get(position).getId(), unRisqueDAO.getListeRisque().get(position).getTitre(), unRisqueDAO.getListeRisque().get(position).getResume(), unRisqueDAO.getListeRisque().get(position).isSupprimer()) ;

                                if(checkBox.isChecked()) {
                                    listeRisques.add(unRisque) ;
                                } else {
                                    listeRisques.remove(unRisque) ;
                                }
                            }
                        }
                    });
                }
            }
        }, 2000);
    }

    //Méthode qui récupère la liste des chantiers
    public void getChantiers() {
        final ChantierDAO unChantierDAO = new ChantierDAO(this) ;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                for (int i = 0; i < unChantierDAO.getListeChantier().size(); i++) {
                    idChantiers.add(unChantierDAO.getListeChantier().get(i).getCode());
                    lesChantiers.add(unChantierDAO.getListeChantier().get(i).getLibelle());
                }
                if(!lesChantiers.isEmpty()) {
                    ArrayAdapter<String> adapter_section = new ArrayAdapter<>(FicheActivity.this, android.R.layout.simple_spinner_item, lesChantiers);
                    adapter_section.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerChantier.setAdapter(adapter_section);
                }
            }
        }, 2000);
    }

    public void setFiche() {

        SharedPreferences connexionPref = getSharedPreferences("connexion", 0);
        int idUtilisateur = connexionPref.getInt("idUtilisateur", 1) ;

        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date inputDate = null;
        try {
            inputDate = fmt.parse(dateText.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Create the MySQL datetime string
        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = fmt.format(inputDate);

        Chantier unChantier = new Chantier() ;
        unChantier.setCode(chantierText.getText().toString()) ;

        Utilisateur unUtilisateur = new Utilisateur() ;
        unUtilisateur.setId(Integer.toString(idUtilisateur)) ;

        uneFiche.setUnChantier(unChantier) ;
        uneFiche.setUnUtilisateur(unUtilisateur) ;
        uneFiche.setDate(dateString) ;
        uneFiche.setListeRisque(listeRisques);

        FicheDAO uneFicheDAO =  new FicheDAO() ;
        uneFicheDAO.setFiche(this, uneFiche);
    }
}
