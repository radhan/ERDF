package com.erdf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    public static Menu _menu ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID)
    {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.activity_container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Récupération du type d'utilisateur
        SharedPreferences connexionPref = getSharedPreferences("connexion", 0) ;

        if (connexionPref.getString("fonctionUtilisateur", "Inconnu").equals("Administrateur")) {
            navigationView.getMenu().findItem(R.id.nav_gestion_utilisateur).setVisible(true) ;
            navigationView.getMenu().findItem(R.id.nav_liste_fiches).setVisible(true) ;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        _menu = menu ;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_modifier) {
            return true;
        }

        if (id == R.id.action_supprimer) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Menu getMenu() {
        return _menu;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_accueil) {
            startActivity(new Intent(BaseActivity.this, AccueilActivity.class));
            finish();

        } else if (id == R.id.nav_saisir_fiche) {
            startActivity(new Intent(BaseActivity.this, FicheActivity.class));
            finish();

        } else if (id == R.id.nav_ajouter_utilisateur) {
            startActivity(new Intent(BaseActivity.this, UserActivity.class));
            finish();

        } else if (id == R.id.nav_liste_utilisateurs) {
            //startActivity(new Intent(BaseActivity.this, AddUserActivity.class));
            finish();
        }else if (id == R.id.nav_liste_fiches) {
            startActivity(new Intent(BaseActivity.this, ListeFicheActivity.class));
            finish();

        } else if (id == R.id.nav_deconnexion) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
            AlertDialog dialog = builder.create();
            dialog.setTitle("Deconnexion");
            dialog.setMessage("Voulez-vous vraiment vous déconnecter ?");
            dialog.setButton("Oui", deconnexionAccept);
            dialog.setCancelable(true);
            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_container);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Deconnexion de l'utilisateur
    DialogInterface.OnClickListener deconnexionAccept = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            SharedPreferences connexionPref = getSharedPreferences("connexion", 0);
            connexionPref.edit().clear().apply();
            startActivity(new Intent(BaseActivity.this, LoginActivity.class));
            finish();
        }
    };
}
