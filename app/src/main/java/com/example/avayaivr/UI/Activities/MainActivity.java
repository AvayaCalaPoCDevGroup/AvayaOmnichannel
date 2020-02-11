package com.example.avayaivr.UI.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.example.avayaivr.R;
import com.example.avayaivr.UI.Fragments.SettingsFragment;
import com.example.avayaivr.UI.Fragments.HomeFragment;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
import android.widget.Toast;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null); //Para que los iconos aparezcan en su color original
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();*/
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_contact:
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+5215617296477"));
                        startActivity(intent);
                        break;
                    case R.id.nav_whatsapp:
                        PackageManager packageManager = getApplicationContext().getPackageManager();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String phone = "+5491151992014";
                        try {
                            String url = "https://api.whatsapp.com/send?phone="+ phone +"&text=" + URLEncoder.encode("Mensage de prueba", "UTF-8");
                            i.setPackage("com.whatsapp");
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setData(Uri.parse(url));
                            if (i.resolveActivity(packageManager) != null) {
                                getApplicationContext().startActivity(i);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case R.id.nav_messenger:
                        Uri uri = Uri.parse("fb-messenger://user/113255423467121");

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
                        break;
                    case R.id.nav_home:
                        replaceFragment(new HomeFragment());
                        drawer.closeDrawers();
                        break;
                }

                return false;
            }
        });
    }

    public void setAppColor(int color){
        //navigationView.setBackgroundColor(color);
        ActionBar mActionBar;
        mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(color));

        View navh = navigationView.findViewById(R.id.nav_background);
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
