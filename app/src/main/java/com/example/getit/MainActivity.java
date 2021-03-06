package com.example.getit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.getit.dummy.DummyContent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        ,MiCuentaFragment.OnFragmentInteractionListener
        ,CrearAnuncioFragment.OnFragmentInteractionListener
        ,CompraFragment.OnFragmentInteractionListener
        ,ComentariosFragment.OnFragmentInteractionListener
        ,PerfilFragment.OnFragmentInteractionListener
        ,AnunciosFragment.OnListFragmentInteractionListener
        ,AnuncioDetalleFragment.OnFragmentInteractionListener
        ,HistorialAnunciosFragment.OnListFragmentInteractionListener
        ,HistorialComprasFragment.OnListFragmentInteractionListener
        {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Anuncios");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set default fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, new AnunciosFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_anuncios) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new AnunciosFragment()).commit();
        } else if (id == R.id.nav_crear_anuncio) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new CrearAnuncioFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_historial_anuncios) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new HistorialAnunciosFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_historial_compras) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new HistorialComprasFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_mi_cuenta) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new MiCuentaFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_cerrar_sesion) {
            SharedPreferences prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("NombresApellidos", "");
            editor.putString("Email", "");
            editor.putInt("UserId", 0);
            editor.putString("Latitud", "");
            editor.putString("Longitud", "");
            editor.commit();

            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            finish();  //Kill the activity from which you will go to next activity
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void GoToAnuncioDetalleFragment(View anuncio_detalle_view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, new AnuncioDetalleFragment()).addToBackStack(null).commit();
    }

    public void GoToCompraFragment(View compra_view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, new CompraFragment()).addToBackStack(null).commit();
    }

    public void GoToComentarioFragment(View compra_view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, new ComentariosFragment()).addToBackStack(null).commit();
    }

    public void GoToPerfilFragment(View compra_view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, new PerfilFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
        }
