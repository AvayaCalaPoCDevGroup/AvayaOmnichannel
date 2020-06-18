package com.example.avayaivr.UI.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.example.avayaivr.BuildConfig;
import com.example.avayaivr.R;
import com.example.avayaivr.UI.Clases.Constants;
import com.example.avayaivr.UI.Clases.Utils;
import com.example.avayaivr.UI.Fragments.ChatFragment;
import com.example.avayaivr.UI.Fragments.SettingsFragment;
import com.example.avayaivr.UI.Fragments.HomeFragment;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    DrawerLayout drawer;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSharedPreferences = getSharedPreferences(Constants.AVAYAIVR_PREFERENCES, 0);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null); //Para que los iconos aparezcan en su color original
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_contact:
                        String telContact = mSharedPreferences.getString(Constants.PREF_CONTACT,"");
                        if(telContact.equals("")){
                            Toast.makeText(getApplicationContext(), "Configura el telefono de contacto en los settings", Toast.LENGTH_SHORT).show();
                        } else {
                            //Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+5215617296477"));
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telContact));
                            startActivity(intent);
                        }
                        break;
                    case R.id.nav_whatsapp:
                        String telWhatsapp = mSharedPreferences.getString(Constants.PREF_WHATSAPP,"");
                        if(telWhatsapp.equals("")){
                            Toast.makeText(getApplicationContext(), "Configura el numero de Whatsapp en los settings", Toast.LENGTH_SHORT).show();
                        } else {
                            PackageManager packageManager = getApplicationContext().getPackageManager();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            String phone = telWhatsapp;
                            try {
                                String url = "https://api.whatsapp.com/send?phone="+ phone +"&text=" + URLEncoder.encode("", "UTF-8");
                                i.setPackage("com.whatsapp");
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.setData(Uri.parse(url));
                                if (i.resolveActivity(packageManager) != null) {
                                    getApplicationContext().startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please Install Whatsapp",    Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Please Install Whatsapp",    Toast.LENGTH_LONG).show();
                            }
                        }

                        break;
                    case R.id.nav_messenger:
                        /*String idMessenger = mSharedPreferences.getString(Constants.PREF_MESSENGERID,"");
                        if(idMessenger.equals("")){
                            Toast.makeText(getApplicationContext(), "Configura URL de Messenger en los Settings", Toast.LENGTH_SHORT).show();
                        } else {
                            //Uri uri = Uri.parse("fb-messenger://user/"+idMessenger);
                            Uri uri = Uri.parse("fb-messenger://user/J0Z.666");
                            Intent toMessenger= new Intent(Intent.ACTION_VIEW, uri);
                            toMessenger.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //toMessenger.setPackage("com.facebook.orca");
                            try {
                                getApplicationContext().startActivity(toMessenger);
                                //startActivity(toMessenger);
                            }
                            catch (android.content.ActivityNotFoundException ex)
                            {
                                Toast.makeText(getApplicationContext(), "Please Install Facebook Messenger",    Toast.LENGTH_LONG).show();
                            }
                        }*/
                        String url =  mSharedPreferences.getString(Constants.PREF_MESSENGERID,"");
                        if(url.equals("")){
                            Toast.makeText(getApplicationContext(), "Configura el URL en los Settings", Toast.LENGTH_SHORT).show();
                        } else if(!url.contains("//m.me/")) {
                            Toast.makeText(getApplicationContext(), "URL incorrecto, el formato debe ser:\nhttp://m.me/{pagina}", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            try{
                                startActivity(i);
                            } catch (Exception ex){
                                Toast.makeText(getApplicationContext(), "Url incorrecto, verifica la configuracion de Spaces", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case R.id.nav_facebook:
                        FacebookAction();
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_twitter:
                        TwitterAction();
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_instagram:
                        InstagramAction();
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_home:
                        replaceFragment(new HomeFragment());
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_chat:
                        replaceFragment(new ChatFragment());
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_email:
                        String mailto, subject, body;
                        mailto = mSharedPreferences.getString(Constants.PREF_EMAIL_TO, "");
                        subject = mSharedPreferences.getString(Constants.PREF_EMAIL_SUBJECT, "");
                        body = mSharedPreferences.getString(Constants.PREF_EMAIL_BODY, "");
                        if(mailto.equals("")){
                            Toast.makeText(getApplicationContext(), "Configura el Email To en los Settings", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                            emailIntent.setData(Uri.parse("mailto:" + mailto + "?subject=" + subject + "&body=" + body));
                            startActivity(emailIntent);
                        }
                        break;
                    case R.id.nav_google:
                        googleAction();
                        break;
                    case R.id.nav_spaces:
                        spacesAction();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"En Construccion...", Toast.LENGTH_SHORT).show();
                        break;
                }

                return false;
            }
        });

        ((TextView)(navigationView.getHeaderView(0).findViewById(R.id.tv_navheader_version))).setText("Ver: " + BuildConfig.VERSION_NAME);
    }

    private void spacesAction() {
        String url = mSharedPreferences.getString(Constants.PREF_SPACES, "");
        if(url.equals("")){
            Toast.makeText(getApplicationContext(), "Configura el URL en los Settings", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(Intent.ACTION_VIEW);
            if (Utils.isPackageInstalled("com.avaya.spaces", getPackageManager()))
                i.setPackage("com.avaya.spaces");
            i.setData(Uri.parse(url));
            try{
                startActivity(i);
            } catch (Exception ex){
                Toast.makeText(getApplicationContext(), "Url incorrecto, verifica la configuracion de Spaces", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void InstagramAction() {
        String url = mSharedPreferences.getString(Constants.PREF_INSTAGRAM, "");
        if(url.equals("")){
            Toast.makeText(getApplicationContext(), "Configura el URL en los Settings", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            try{
                startActivity(i);
            } catch (Exception ex){
                Toast.makeText(getApplicationContext(), "Url incorrecto, verifica la configuracion de Instagram", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void TwitterAction() {
        String url = mSharedPreferences.getString(Constants.PREF_TWITTER, "");
        if(url.equals("")){
            Toast.makeText(getApplicationContext(), "Configura el URL en los Settings", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            try{
                startActivity(i);
            } catch (Exception ex){
                Toast.makeText(getApplicationContext(), "Url incorrecto, verifica la configuracion de Twitter", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void FacebookAction() {
        String url = mSharedPreferences.getString(Constants.PREF_FACEBOOK, "");
        if(url.equals("")){
            Toast.makeText(getApplicationContext(), "Configura el URL en los Settings", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            try{
                startActivity(i);
            } catch (Exception ex){
                Toast.makeText(getApplicationContext(), "Url incorrecto, verifica la configuracion de Facebook", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void googleAction() {
        //Esta primera parte se comento ya que la intencion era abrir una accion de google assistant desde una url
        /*//String url = "https://assistant.google.com/services/invoke/uid/000000d139bbc4d4?utm_source=Google&utm_medium=email&utm_campaign=holiday+sale";
        String url = mSharedPreferences.getString(Constants.PREF_GOOGLE, "");
        if(url.equals("")){
            Toast.makeText(getApplicationContext(), "Configura el URL en los Settings", Toast.LENGTH_SHORT).show();
        } else if(!url.contains("assistant")){
            Toast.makeText(getApplicationContext(), "El url no tiene el formato adecuado.\nEjemplo: https://assistant.google.com/...", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }*/

        String url = mSharedPreferences.getString(Constants.PREF_GOOGLE, "");
        if(url.equals("")){
            Toast.makeText(getApplicationContext(), "Configura el URL en los Settings", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.setClassName("com.google.android.googlequicksearchbox",
                    "com.google.android.googlequicksearchbox.SearchActivity");
            intent.putExtra("query", url);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        //Este codigo solo lanza google assistant pero no es posible ponerle un query
        /*startActivity(new Intent(Intent.ACTION_VOICE_COMMAND)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        int color = mSharedPreferences.getInt(Constants.PREF_COLOR, -64765);
        setAppColor(color);
    }

    public void setAppColor(int color){
        //navigationView.setBackgroundColor(color);
        ActionBar mActionBar;
        mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(color));

        View navh = navigationView.getHeaderView(0).findViewById(R.id.nav_background);
        navh.setBackgroundColor(color);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                replaceFragment(new SettingsFragment());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment, "TAG1")
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /***
     * Metodo para verificar que la aplicacion tiene permisos para acceder al almacenamiento [READ_EXTERNAL_STORAGE]
     */
    public boolean ChekFilePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        45330);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return false;
        } else {
            return true;
        }
    }
}
