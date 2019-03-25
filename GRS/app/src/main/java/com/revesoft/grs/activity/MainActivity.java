package com.revesoft.grs.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessaging;
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
import com.revesoft.grs.firebasenotifications.util.NotificationUtils;
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
    ImageView buttonHome,buttonRefresh,buttonProfile,buttonLogOut;
    PermissionListener listener;
    Context context;
    MenuItem item;
    ProgressBar progressBar;
  //  SharedPreferences sharedPref;
  //  SharedPreferences.Editor editor;
 //   View rootLayout;
    AlertDialog alertDialog;
    String url,latestUrl;
    UserStatus userStatus;
    boolean isLoginRequest;
    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int FCR = 1;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    boolean isShowing=false;

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
      //  sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        progressBar = findViewById(R.id.progressbar);
      //  editor = sharedPref.edit();
     //   rootLayout = findViewById(R.id.root_layout);
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
        buttonHome = findViewById(R.id.button_home);
        buttonRefresh = findViewById(R.id.button_refresh);
        buttonProfile = findViewById(R.id.button_profile);
        buttonLogOut = findViewById(R.id.button_logOut);
        webview = findViewById(R.id.webView);
        editText.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        webview.setVisibility(View.GONE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            buttonProfile.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlue));
        }else {
            buttonProfile.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        }
        if(url.compareTo(API.COMPLAINANT_SIGN_IN_TAG_URL)==0){

            buttonProfile.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_list_black_24dp));
        }else {
            buttonProfile.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.dashboard));
        }

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
                    FileChooserParams fileChooserParams) {

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
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlLoader(API.APP_URL);
            }
        });
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               reload_webview();
            }
        });
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlLoader(API.ADMIN_SIGN_IN_SUCCESS_TAG_URL);
            }
        });
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
        urlLoader(url);
        registerBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(info.androidhive.firebasenotifications.app.Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(info.androidhive.firebasenotifications.app.Config.TOPIC_GLOBAL);

                  //  displayFirebaseRegId();

                } else if (intent.getAction().equals(info.androidhive.firebasenotifications.app.Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                  //  txtMessage.setText(message);
                }
            }
        };
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Snackbar.make(progressBar, "Refreshing...", 1000).show();
//        switch (item.getItemId()) {
//            case R.id.action_home:
//                urlLoader(API.APP_URL);
//                return true;
//            case R.id.action_info:
//                reload_webview();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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

        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);

        url.trim();
//        if(!url.startsWith("http://")){
//            url = "http://" + url;
//        }

        webview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
         //   boolean timeout=true;
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Handle the error
              //  showError();
               // webview.setVisibility(View.GONE);
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
               // timeout=true;
                latestUrl=url;
                Log.d("Cookies","onPageStarted :"+url);
               if( url.contains(API.COMPLAINANT_SIGN_IN_FAILURE_TAG_URL) || url.contains(API.ADMIN_SIGN_IN_FAILURE_TAG_URL) || url.contains(API.COMPLAINANT_LOG_OUT_TAG_URL )
                       || url.contains(API.ADMIN_LOG_OUT_TAG_URL)   || (url.contains(API.COMPLAINANT_SIGN_IN_TAG_URL)&& !isLoginRequest) || (url.contains(API.ADMIN_SIGN_IN_TAG_URL)&& !isLoginRequest)){
                   logOut();
                   Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                   if( url.contains(API.COMPLAINANT_SIGN_IN_FAILURE_TAG_URL) ||  url.contains(API.ADMIN_SIGN_IN_FAILURE_TAG_URL)) {
                       intent.putExtra(Constant.message, getResources().getString(R.string.valid_username_pass));
                   }else  if( url.contains(API.COMPLAINANT_LOG_OUT_TAG_URL) || url.contains(API.ADMIN_LOG_OUT_TAG_URL)) {
                       intent.putExtra(Constant.message, getResources().getString(R.string.logout_success));
                   }
                   startActivity(intent);

                   finish();
               }

//                Runnable run = new Runnable() {
//                    public void run() {
//                        if(timeout) {
//                            // do what you want
//                            showError();
//                        }
//                    }
//                };
//                Handler myHandler = new Handler(Looper.myLooper());
//                myHandler.postDelayed(run, 30*1000);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("Cookies","onPageFinished :"+url);
                isLoginRequest=false;
             //   timeout=false;

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
               String postData = "password="+userStatus.getUser_password()+"&username="+userStatus.getUser_mobile()+"&device_token="+userStatus.getFCM_Token();
               webview.postUrl(
                       API.COMPLAINANT_SIGN_IN_TAG_URL,
                       EncodingUtils.getBytes(postData, "BASE64"));
           }else if(url.contains(API.ADMIN_SIGN_IN_TAG_URL) && isLoginRequest){
               String postData = "password="+userStatus.getUser_password()+"&username="+userStatus.getUser_mobile()+"&device_token="+userStatus.getFCM_Token();;
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

        //editor.putString("url", url).apply();
       // invalidateOptionsMenu();
    }

    private void showError(){
        if(!isShowing) {
            isShowing=true;
            if(this!=null) {
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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webview.clearCache(true);

        webview.clearHistory();

        webview.destroy();

      //  CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();
    }

    @Override
    public void onBackPressed() {
        if(latestUrl.compareTo(API.ADMIN_DASHBOARD_TAG_URL)==0 || latestUrl.compareTo(API.COMPLAINANT_DASHBOARD_TAG_URL)==0) {
            showAlertDialog();
        }else{
            if (webview.copyBackForwardList().getCurrentIndex() > 0) {
                webview.goBack();
            } else {
                showAlertDialogShort();
            }

        }
    }


    private void showAlertDialogShort(){
        android.app.AlertDialog.Builder alertDialog2 = new android.app.AlertDialog.Builder(
                MainActivity.this);

        alertDialog2.setTitle(getResources().getString(R.string.prompt_exit));

        alertDialog2.setPositiveButton(getResources().getString(R.string.dialog_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog2.setNegativeButton(getResources().getString(R.string.dialog_no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog2.show();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void load_webview(){
     //   String url = sharedPref.getString("url","");
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
//        if (18 < Build.VERSION.SDK_INT ){
//            //18 = JellyBean MR2, KITKAT=19
//            webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        }
    }



  private void showAlertDialog(){
      AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
              MainActivity.this);
      alertDialog2.setTitle(getResources().getString(R.string.choose_selection));

      alertDialog2.setNeutralButton(getResources().getString(R.string.keep_looged_in),
              new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                      finish();
                  }
              });
      // Setting Positive "Yes" Btn
      alertDialog2.setNegativeButton(getResources().getString(R.string.log_out),
              new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                      logOut();
                      finish();
                  }
              });
// Setting Negative "NO" Btn
      alertDialog2.setPositiveButton(getResources().getString(R.string.cancel),
              new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                      dialog.cancel();
                  }
              });

// Showing Alert Dialog
      alertDialog2.show();

  }

  private void logOut(){
      userStatus.setUser_password("");
      userStatus.setUser_mobile("");
      userStatus.setUser_id("");
  }

    // Create an image file
    private File createImageFile() throws IOException {

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    protected void onResume() {
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(info.androidhive.firebasenotifications.app.Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(info.androidhive.firebasenotifications.app.Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
