package com.revesoft.grs.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewDatabase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.revesoft.grs.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;


public class MainActivity extends AppCompatActivity{

    WebView webview;
    WebSettings ws ;
    EditText editText;
    Button button;
    PreferenceManager pref;
    PermissionListener listener;
    Context context;
    MenuItem item;
    boolean isSaved;
    boolean finalIsSaved;
    ProgressBar progressBar;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    View rootLayout;
    Toolbar toolbar;
    AlertDialog alertDialog;
    CookieManager cookieManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        progressBar = findViewById(R.id.progressbar);
        editor = sharedPref.edit();

        rootLayout = findViewById(R.id.root_layout);


        context = this;
        listener = new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                showError();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        };


        Dexter.withActivity(this)
                .withPermission(Manifest.permission.INTERNET)
                .withListener(listener)
                .check();

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                .withListener(listener)
                .check();


        if(!isNetworkAvailable()){
            AlertDialog.Builder builder = null;
            builder = new AlertDialog.Builder(this);
            builder.setTitle("No internet Connection!")
                    .setMessage("Please connect to Internet !")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
        editText = findViewById(R.id.url);
        button = findViewById(R.id.go_web_view);
        webview = findViewById(R.id.webView);

        editText.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        webview.setVisibility(View.GONE);

        isSaved = true;

        if(sharedPref.getString("url", "").isEmpty()){
            isSaved = false;
        }

        final Activity activity = this;
        ws = webview.getSettings();
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("MyApplication", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }

            public void onProgressChanged(WebView view, int progress) {
                if(progress > 80){
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                }
            }


        });



        if(isSaved){
            load_webview();
        }

         finalIsSaved = isSaved;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlLoader(editText.getText().toString());


            }
        });
        urlLoader(getResources().getString(R.string.project_url));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuinflater = getMenuInflater();
        menuinflater.inflate(R.menu.toolbar_menu, menu);
       // item = menu.findItem(R.id.action_home);
        item = menu.findItem(R.id.action_info);
        item.setVisible(isSaved);
        invalidateOptionsMenu();

        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Snackbar.make(rootLayout, "Refreshing...", 1000).show();
        switch (item.getItemId()) {
            case R.id.action_home:
                urlLoader(getResources().getString(R.string.project_url));
                return true;
            case R.id.action_info:
                reload_webview();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void urlLoader(String url){
        editText.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        webview.setVisibility(View.VISIBLE);
        if(url.compareTo(getResources().getString(R.string.project_url))==0){
              if(sharedPref.getString(getResources().getString(R.string.saveCookies), "yes").compareTo("no")==0){
                  clearCookies();
                  Log.d("Cookies","Clear cookies");
              }
        }

      //  clearCookies();
      //  CookieSyncManager.createInstance(this);

    //    cookieManager = CookieManager.getInstance();
    //    cookieManager.setAcceptCookie(true);

//        ws.setSaveFormData(true);
//        ws.setSavePassword(true);
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);



        // String url = editText.getText().toString();
        //    String url = getResources().getString(R.string.project_url);
        url.trim();
        if(!url.startsWith("http://")){
            url = "http://" + url;
        }

        webview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Handle the error
                showError();
                webview.setVisibility(View.GONE);
            }


            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }



            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                if(url.compareTo(getResources().getString(R.string.project_url))==0){
//                    clearCookies();
//                }
                Log.d("Cookies","onPageStarted :"+url);
                if(url.compareTo(getResources().getString(R.string.project_login_success_url))==0){
                    editor.putString(getResources().getString(R.string.logged_in), getResources().getString(R.string.yes)).apply();
                }


            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("Cookies","onPageFinished :"+url);
                String cookies = CookieManager.getInstance().getCookie(url);
                if(cookies!=null && cookies.contains(getResources().getString(R.string.lang))) {
                    setLocale("en");
                }else {
                    setLocale("bn");
                }

                Log.d("Cookies","sharedPref.getString(getResources().getString(R.string.logged_in), getResources().getString(R.string.yes) :"+sharedPref.getString(getResources().getString(R.string.logged_in), getResources().getString(R.string.yes)));
                Log.d("Cookies","sharedPref.getString(getResources().getString(R.string.saveCookies), \"\"):"+sharedPref.getString(getResources().getString(R.string.saveCookies), ""));
                  if(sharedPref.getString(getResources().getString(R.string.logged_in), getResources().getString(R.string.yes)).compareTo(getResources().getString(R.string.yes))==0
                          && sharedPref.getString(getResources().getString(R.string.saveCookies), "").compareTo("")==0){

                    if(cookies!=null && cookies.contains(getResources().getString(R.string.authorization))) {
                         // Log.d("Cookies","cookies :"+cookies);
                        //Toast.makeText(getApplicationContext(),"All Cookies " + cookies , Toast.LENGTH_LONG).show();
                        open();
                    }
                }else {
                   // Toast.makeText(MainActivity.this,"Not Login Page",Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // open rest of URLS in default browser
                if(!url.contains(getResources().getString(R.string.project_url))) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false;
            }

        });

        if(isNetworkAvailable()) {
            webview.loadUrl(url);
        }
        else{
            showError();
        }


        if(!finalIsSaved){
            editor.putString("url", url).apply();
        }
        isSaved = true;
//        item.setVisible(true);
        invalidateOptionsMenu();
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }





    @SuppressWarnings("deprecation")
    public  void clearCookies( )
    {

        editor.putString(getResources().getString(R.string.logged_in), getResources().getString(R.string.no)).apply();
        notDicided();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d("clearCookies", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            editor.putString(getResources().getString(R.string.saveCookies), "").apply();
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else
        {
            Log.d("clearCookies", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    public void open(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.save_signin));
                alertDialogBuilder.setPositiveButton(getResources().getString(R.string.dialog_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                               // Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                                editor.putString(getResources().getString(R.string.saveCookies), getResources().getString(R.string.yes)).apply();

                                alertDialog.dismiss();
                            }
                        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.dialog_no),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

             //   Toast.makeText(MainActivity.this,"You clicked no button",Toast.LENGTH_LONG).show();
                editor.putString(getResources().getString(R.string.saveCookies), getResources().getString(R.string.no)).apply();
                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void notDicided(){
        editor.putString(getResources().getString(R.string.saveCookies), "").apply();
    }
    private void showError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet")
                .setMessage("No Internect access!")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  cookieManager.setAcceptCookie(false);
      //  if(sharedPref.getString(getResources().getString(R.string.saveCookies), "yes").compareTo("no")==0){
            clearCookies();
      //      Log.d("Cookies","Clear cookies");
     //   }

        Log.d("Cookies","Clear cookies :"+sharedPref.getString(getResources().getString(R.string.saveCookies), ""));
    }

    @Override
    public void onBackPressed() {
        if(webview.copyBackForwardList().getCurrentIndex() > 0){
            webview.goBack();
        }
        else {
            super.onBackPressed();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void load_webview(){
        String url = sharedPref.getString("url","");
        editText.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        webview.setVisibility(View.VISIBLE);
//        webview.getSettings().setDomStorageEnabled(true);
//        webview.getSettings().setJavaScriptEnabled(true);
//        CookieManager.getInstance().setAcceptCookie(true);
//        webview.getSettings().setSaveFormData(true);
//        webview.getSettings().setSavePassword(true);


        if(isNetworkAvailable()){
           // webview.loadUrl(url);
        }
        else{
            showError();
        }


    }
    public void reload_webview(){
//        progressBar.setVisibility(View.VISIBLE);

        webview.setWebViewClient(new WebViewClient());
        if(isNetworkAvailable()){
            webview.loadUrl(webview.getUrl());
        }
        else{
            showError();
        }
        webview.getSettings().setJavaScriptEnabled(true);
    }
}
