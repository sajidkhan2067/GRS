package com.revesoft.grs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
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


public class MainActivity extends AppCompatActivity{

    WebView webview;
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

        webview.setWebViewClient(new WebViewClient());

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
//                editText.setVisibility(View.GONE);
//                button.setVisibility(View.GONE);
//                webview.setVisibility(View.VISIBLE);
//
//
//                String url = editText.getText().toString();
//            //    String url = getResources().getString(R.string.project_url);
//                url.trim();
//                if(!url.startsWith("http://")){
//                    url = "http://" + url;
//                }
//                webview.setWebViewClient(new WebViewClient());
////                progressBar.setVisibility(View.VISIBLE);
//                if(isNetworkAvailable()) {
//                    webview.loadUrl(url);
//                }
//                else{
//                    showError();
//                }
//                webview.getSettings().setJavaScriptEnabled(true);
//
//                if(!finalIsSaved){
//                    editor.putString("url", url).apply();
//                }
//                isSaved = true;
//                item.setVisible(true);
//                invalidateOptionsMenu();

            }
        });
        urlLoader(getResources().getString(R.string.project_url));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuinflater = getMenuInflater();
        menuinflater.inflate(R.menu.toolbar_menu, menu);

        item = menu.findItem(R.id.action_info);
        item.setVisible(isSaved);
        invalidateOptionsMenu();

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Snackbar.make(rootLayout, "Refreshing...", 1000).show();
        reload_webview();
        return super.onOptionsItemSelected(item);
    }

    private void urlLoader(String url){
        editText.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        webview.setVisibility(View.VISIBLE);


       // String url = editText.getText().toString();
        //    String url = getResources().getString(R.string.project_url);
        url.trim();
        if(!url.startsWith("http://")){
            url = "http://" + url;
        }
        webview.setWebViewClient(new WebViewClient());
//                progressBar.setVisibility(View.VISIBLE);
        if(isNetworkAvailable()) {
            webview.loadUrl(url);
        }
        else{
            showError();
        }
        webview.getSettings().setJavaScriptEnabled(true);

        if(!finalIsSaved){
            editor.putString("url", url).apply();
        }
        isSaved = true;
//        item.setVisible(true);
        invalidateOptionsMenu();
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
//        progressBar.setVisibility(View.VISIBLE);

        webview.setWebViewClient(new WebViewClient());
        if(isNetworkAvailable()){
            webview.loadUrl(url);
        }
        else{
            showError();
        }
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
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
