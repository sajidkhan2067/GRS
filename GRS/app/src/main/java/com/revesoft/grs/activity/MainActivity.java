package com.revesoft.grs.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
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
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.revesoft.grs.R;
import com.revesoft.grs.util.API;
import com.revesoft.grs.util.Constant;
import com.revesoft.grs.util.api.data.item.user.UserStatus;

import org.apache.http.util.EncodingUtils;

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
    ProgressBar progressBar;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    View rootLayout;
    Toolbar toolbar;
    AlertDialog alertDialog;
    CookieManager cookieManager;
    String url,latestUrl;
    UserStatus userStatus;
    boolean isLoginRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);

        userStatus = new UserStatus(MainActivity.this);
        Intent intent = getIntent();
        if (intent!=null) {
             url = intent.getStringExtra(Constant.url);
             if(url.compareTo(API.COMPLAINANT_SIGN_IN_TAG_URL)==0){
                 isLoginRequest=true;
             }

        }
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
            builder.setTitle(getResources().getString(R.string.no_internet_title))
                    .setMessage(getResources().getString(R.string.no_internet_message))
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

        load_webview();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlLoader(editText.getText().toString());
            }
        });
        urlLoader(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuinflater = getMenuInflater();
        menuinflater.inflate(R.menu.toolbar_menu, menu);
        item = menu.findItem(R.id.action_info);
        invalidateOptionsMenu();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Snackbar.make(rootLayout, "Refreshing...", 1000).show();
        switch (item.getItemId()) {
            case R.id.action_home:
                urlLoader(API.APP_URL);
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
//        if(url.compareTo(getResources().getString(R.string.project_url))==0){
//              if(isNeededToClearCookies()){
//                  clearCookies();
//                  Log.d("Cookies","Clear cookies");
//              }
//        }

        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);

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
                latestUrl=url;
                Log.d("Cookies","onPageStarted :"+url);
               if( url.contains(API.COMPLAINANT_SIGN_IN_FAILURE_TAG_URL) || url.contains(API.COMPLAINANT_LOG_OUT_TAG_URL ) || (url.contains(API.COMPLAINANT_SIGN_IN_TAG_URL)&& !isLoginRequest) ){
                   userStatus.setUser_password("");
                   userStatus.setUser_mobile("");
                   Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                   if( url.contains(API.COMPLAINANT_SIGN_IN_FAILURE_TAG_URL)) {
                       intent.putExtra(Constant.message, getResources().getString(R.string.valid_username_pass));
                   }else  if( url.contains(API.COMPLAINANT_LOG_OUT_TAG_URL)) {
                       intent.putExtra(Constant.message, getResources().getString(R.string.logout_success));
                   }
                   startActivity(intent);

                   finish();
               }
//                if(url.compareTo(getResources().getString(R.string.project_login_success_url))==0){
//                    editor.putString(getResources().getString(R.string.logged_in), getResources().getString(R.string.yes)).apply();
//                }else  if(url.contains(getResources().getString(R.string.project_login_url))){
//                    editor.putString(getResources().getString(R.string.saveCookies), "").apply();
//                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("Cookies","onPageFinished :"+url);
                isLoginRequest=false;
//                String cookies = CookieManager.getInstance().getCookie(url);
//                if(cookies!=null && cookies.contains(getResources().getString(R.string.lang))) {
//                    setLocale("en");
//                }else {
//                    setLocale("bn");
//                }
//
//                Log.d("Cookies","sharedPref.getString(getResources().getString(R.string.logged_in), getResources().getString(R.string.yes) :"+sharedPref.getString(getResources().getString(R.string.logged_in), getResources().getString(R.string.yes)));
//                Log.d("Cookies","sharedPref.getString(getResources().getString(R.string.saveCookies), \"\"):"+sharedPref.getString(getResources().getString(R.string.saveCookies), ""));
//                  if(sharedPref.getString(getResources().getString(R.string.logged_in), getResources().getString(R.string.yes)).compareTo(getResources().getString(R.string.yes))==0
//                          && sharedPref.getString(getResources().getString(R.string.saveCookies), "").compareTo("")==0){
//
//                    if(cookies!=null && cookies.contains(getResources().getString(R.string.authorization))) {
//                        openDialog();
//                    }
//                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // open rest of URLS in default browser
                if(!url.contains(API.APP_URL)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false;
            }

        });

        if(isNetworkAvailable()) {

           if(url.contains(API.COMPLAINANT_SIGN_IN_TAG_URL) && isLoginRequest) {
//               String postData = "a=0&password=123qw&username=01847280724";
               String postData = "a=0&"+"password="+userStatus.getUser_password()+"&username="+userStatus.getUser_mobile();
               webview.postUrl(
                       "http://www.grs.gov.bd/login",
                       EncodingUtils.getBytes(postData, "BASE64"));
           }else {
               webview.loadUrl(url);
           }

        }
        else{
            showError();
        }

        editor.putString("url", url).apply();
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
        } else  {
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

    public void openDialog(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.save_signin));
                alertDialogBuilder.setPositiveButton(getResources().getString(R.string.dialog_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                saveCookies();
                                alertDialog.dismiss();
                            }
                        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.dialog_no),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
    private void saveCookies(){
        editor.putString(getResources().getString(R.string.saveCookies), getResources().getString(R.string.yes)).apply();
    }
    private void showError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.no_internet_title))
                .setMessage(getResources().getString(R.string.no_internet_message))
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
        if(isNeededToClearCookies()){
            clearCookies();
            Log.d("Cookies","Clear cookies");
        }
        Log.d("Cookies","Clear cookies :"+sharedPref.getString(getResources().getString(R.string.saveCookies), ""));
    }

    @Override
    public void onBackPressed() {
        if(latestUrl.compareTo(API.DASHBOARD_TAG_URL)==0) {
            showAlertDialog();
        }else {
            if (webview.copyBackForwardList().getCurrentIndex() > 0) {
                webview.goBack();
            } else {
                super.onBackPressed();
            }

        }
    }

    private boolean isNeededToClearCookies(){
        if(sharedPref.getString(getResources().getString(R.string.saveCookies),
                getResources().getString(R.string.yes)).compareTo(getResources().getString(R.string.no))==0){
            return true;
        }else {
            return false;
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

        if(!isNetworkAvailable()){
            showError();
        }
    }
    public void reload_webview(){
        webview.setWebViewClient(new WebViewClient());
        if(isNetworkAvailable()){
            webview.loadUrl(webview.getUrl());
        }
        else{
            showError();
        }
        webview.getSettings().setJavaScriptEnabled(true);
    }



  private void showAlertDialog(){
      AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
              MainActivity.this);

// Setting Dialog Title
      alertDialog2.setTitle(getResources().getString(R.string.choose_selection));

// Setting Dialog Message
    //  alertDialog2.setMessage("Are you sure you want delete this file?");

// Setting Icon to Dialog
   //   alertDialog2.setIcon(R.drawable.delete);

// Setting Positive "Yes" Btn
      alertDialog2.setNeutralButton(getResources().getString(R.string.keep_looged_in),
              new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                      // Write your code here to execute after dialog
//                      Toast.makeText(getApplicationContext(),
//                              "You clicked on Keep me Login", Toast.LENGTH_SHORT)
//                              .show();
                      finish();
                  }
              });
      // Setting Positive "Yes" Btn
      alertDialog2.setNegativeButton(getResources().getString(R.string.log_out),
              new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                      // Write your code here to execute after dialog
//                      Toast.makeText(getApplicationContext(),
//                              "You clicked on Log out", Toast.LENGTH_SHORT)
//                              .show();
                      userStatus.setUser_password("");
                      userStatus.setUser_mobile("");
                      finish();
                  }
              });
// Setting Negative "NO" Btn
      alertDialog2.setPositiveButton(getResources().getString(R.string.cancel),
              new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                      // Write your code here to execute after dialog
//                      Toast.makeText(getApplicationContext(),
//                              "You clicked on Cancel", Toast.LENGTH_SHORT)
//                              .show();
                      dialog.cancel();
                  }
              });

// Showing Alert Dialog
      alertDialog2.show();

  }
}
