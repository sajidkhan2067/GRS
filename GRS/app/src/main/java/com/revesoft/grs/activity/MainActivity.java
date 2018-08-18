package com.revesoft.grs.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
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
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
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
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
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
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.revesoft.grs.R;
import com.revesoft.grs.util.API;
import com.revesoft.grs.util.Constant;
import com.revesoft.grs.util.api.data.item.user.UserStatus;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int FCR = 1;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);
        requestStoragePermission();
        userStatus = new UserStatus(MainActivity.this);
        Intent intent = getIntent();
        if (intent!=null) {
             url = intent.getStringExtra(Constant.url);
             if(url.compareTo(API.COMPLAINANT_SIGN_IN_TAG_URL)==0 || url.compareTo(API.ADMIN_SIGN_IN_TAG_URL)==0){
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

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(listener)
                .check();

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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
        webview.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                        Toast.LENGTH_LONG).show();

            }
        });
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


            //For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
            }

            // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {

                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                MainActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FCR);
            }

            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), MainActivity.FCR);
            }

            //For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webview, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {

                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }

                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {

                    File photoFile = null;

                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    } catch (IOException ex) {
                        Log.e(TAG, "Image file creation failed", ex);
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");
                Intent[] intentArray;

                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);

                return true;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;

            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {

                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        } else {

            if (requestCode == FCR) {
                if (null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
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

    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            if(!isNetworkAvailable()){
                                AlertDialog.Builder builder = null;
                                builder = new AlertDialog.Builder(MainActivity.this);
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
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
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
               if( url.contains(API.COMPLAINANT_SIGN_IN_FAILURE_TAG_URL) || url.contains(API.ADMIN_SIGN_IN_FAILURE_TAG_URL) || url.contains(API.COMPLAINANT_LOG_OUT_TAG_URL )
                       || url.contains(API.ADMIN_LOG_OUT_TAG_URL)   || (url.contains(API.COMPLAINANT_SIGN_IN_TAG_URL)&& !isLoginRequest) || (url.contains(API.ADMIN_SIGN_IN_TAG_URL)&& !isLoginRequest)){
                   userStatus.setUser_password("");
                   userStatus.setUser_mobile("");
                   userStatus.setUser_id("");
                   Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                   if( url.contains(API.COMPLAINANT_SIGN_IN_FAILURE_TAG_URL) ||  url.contains(API.ADMIN_SIGN_IN_FAILURE_TAG_URL)) {
                       intent.putExtra(Constant.message, getResources().getString(R.string.valid_username_pass));
                   }else  if( url.contains(API.COMPLAINANT_LOG_OUT_TAG_URL) || url.contains(API.ADMIN_LOG_OUT_TAG_URL)) {
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
               String postData = "password="+userStatus.getUser_password()+"&username="+userStatus.getUser_mobile();
               webview.postUrl(
                       API.COMPLAINANT_SIGN_IN_TAG_URL,
                       EncodingUtils.getBytes(postData, "BASE64"));
           }else if(url.contains(API.ADMIN_SIGN_IN_TAG_URL) && isLoginRequest){
               String postData = "password="+userStatus.getUser_password()+"&username="+userStatus.getUser_mobile();
               webview.postUrl(
                       API.ADMIN_SIGN_IN_TAG_URL,
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
        if(latestUrl.compareTo(API.ADMIN_DASHBOARD_TAG_URL)==0 || latestUrl.compareTo(API.COMPLAINANT_DASHBOARD_TAG_URL)==0) {
            showAlertDialog();
        }else{
            if (webview.copyBackForwardList().getCurrentIndex() > 0) {
                webview.goBack();
            } else {
//                super.onBackPressed();
                showAlertDialogShort();
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

    private void showAlertDialogShort(){
        android.app.AlertDialog.Builder alertDialog2 = new android.app.AlertDialog.Builder(
                MainActivity.this);

// Setting Dialog Title
        alertDialog2.setTitle(getResources().getString(R.string.prompt_exit));

// Setting Dialog Message
        //  alertDialog2.setMessage("Are you sure you want delete this file?");

// Setting Icon to Dialog
        //   alertDialog2.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton(getResources().getString(R.string.dialog_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
//                      Toast.makeText(getApplicationContext(),
//                              "You clicked on Log out", Toast.LENGTH_SHORT)
//                              .show();
                        dialog.cancel();
                        finish();
                    }
                });
// Setting Negative "NO" Btn
        alertDialog2.setNegativeButton(getResources().getString(R.string.dialog_no),
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
                      userStatus.setUser_id("");
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

    // Create an image file
    private File createImageFile() throws IOException {

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }



}
