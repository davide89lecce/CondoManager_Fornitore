package com.gambino_serra.condomanager_fornitore.View.DrawerMenu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.Home;
import com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.InformazioniPersonali.InformazioniPersonali;
import com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.ListaFornitori.ListaFornitori;
import com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Messaggi.BachecaMessaggi;
import com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.StoricoAvvisi.StoricoAvvisi;
import com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.StoricoInterventi.StoricoInterventi;
import com.gambino_serra.condomanager_fornitore.View.DrawerMenu.other.CircleTransform;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class MainDrawer extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private FirebaseAuth firebaseAuth;



    // urls to load navigation header background image and profile image TODO: CAMBIARE IMMAGINI
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_INFO = "info_personali";
    private static final String TAG_STORICO_INTERVENTI = "storico_interventi";
    private static final String TAG_STORICO_AVVISI = "storico_avvisi";
    private static final String TAG_LISTA_FORNITORI = "lista_fornitori";
    private static final String TAG_MESSAGGI = "bacheca_messaggi";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home Menu when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();



        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);



        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText("NOME DELL'UTENTE"); //TODO: IMPOSTARE NOME DELL'UTENTE LOGGATO
        //txtWebsite.setText("www.androidhive.info");

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

    }

    /***
     * Returns respected Menu that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {

        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
           // toggleFab();
            return;
        }

        // Sometimes, when Menu has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the Menu is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the activity_main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
       // toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
       // invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                Home homeFragment = new Home();
                return homeFragment;
            case 1:
                // bacheca_messaggi
                BachecaMessaggi messaggiFragment = new BachecaMessaggi();
                return messaggiFragment;
            case 2:
                // info personali
                InformazioniPersonali infoFragment = new InformazioniPersonali();
                return infoFragment;
            case 3:
                // storico inteventi
                StoricoInterventi interventiFragment = new StoricoInterventi();
                return interventiFragment;
            case 4:
                // storico avvisi
                StoricoAvvisi avvisiFragment = new StoricoAvvisi();
                return avvisiFragment;
            case 5:
                // lista fornitori
                ListaFornitori listaFornitoriFragment = new ListaFornitori();
                return listaFornitoriFragment;
            default:
                return new Home();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.getMenu().getItem(1).setChecked(false);
        navigationView.getMenu().getItem(2).setChecked(false);
        navigationView.getMenu().getItem(3).setChecked(false);
        navigationView.getMenu().getItem(4).setChecked(false);
        navigationView.getMenu().getItem(5).setChecked(false);

        navigationView.getMenu().getItem(navItemIndex).setChecked(true);

        //Punto accanto voce menù
        //navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the activity_main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        navigationView.getMenu().getItem(0).setChecked(true);
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_messaggi:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_MESSAGGI;
                        break;
                    case R.id.nav_info_personali:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_INFO;
                        break;
                    case R.id.nav_storico_interventi:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_STORICO_INTERVENTI;
                        break;
                    case R.id.nav_storico_avvisi:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_STORICO_AVVISI;
                        break;
                    case R.id.nav_lista_fornitori:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_LISTA_FORNITORI;
                        break;

//                    case R.id.nav_about_us:
//                        // launch new intent instead of loading Menu
//                        startActivity(new Intent(MainDrawer.this, AboutUsActivity.class));
//                        drawer.closeDrawers();
//                        return true;
                    case R.id.nav_logout:
                        // launch new intent instead of loading Menu
                       // startActivity(new Intent(MainDrawer.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainDrawer.this, com.gambino_serra.condomanager_fornitore.View.Login.LoginActivity.class));
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home Menu when back key is pressed
        // when user is in other Menu than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }


    // show or hide the fab
    //private void toggleFab() {
    //    if (navItemIndex == 0)
    //        fab.show();
    //    else
    //        fab.hide();
    //}
}