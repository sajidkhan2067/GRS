package com.revesoft.grs.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revesoft.grs.R;
import com.revesoft.grs.api.data.item.Login.LoginVerification;
import com.revesoft.grs.application.AppController;
import com.revesoft.grs.util.API;
import com.revesoft.grs.util.AppManager;
import com.revesoft.grs.util.Constant;
import com.revesoft.grs.util.GetUrlBuilder;
import com.revesoft.grs.util.ObjectRequest;
import com.revesoft.grs.util.api.data.item.user.UserStatus;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.support.design.widget.Snackbar.make;
import static android.widget.Toast.LENGTH_SHORT;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements DialogInterface.OnClickListener,OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    //validation check
  //  @Password(min = 6, message = "Your password must contain minimum 6 characters and Maximum 20 characters")
    private EditText passwordEditText;

    private static final String VALID_MOBILE_REG_EX = "^01(1|5|6|7|8|9)\\d{8}$";
    // UI references.
    private EditText mobileNumberEditText;
    private TextView signUpText;


    private LinearLayout complainant_sign_in_button,admin_sign_in_button;
    private Button signUpActivityTransferButton;
    private ImageButton backButton;

    private ProgressBar loginProgressBar;
    private View loginForm;
    private AppManager appManager;
    private String mobile,password;
    ObjectRequest<LoginVerification> loginVerificationObjectRequest;
    protected TextView progressBarMessageTextView;
    private AlertDialog noInternetAlertDialog;
    private AlertDialog errorAlertDialog;
    protected AlertDialog progressBarDialog;
    private AppController appController = AppController.getInstance();
    int timeoutCounter = 0;
    UserStatus userStatus;
    String message;
//    private Validator loginValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        userStatus = new UserStatus(LoginActivity.this);
        setLocale("bn");
        setContentView(R.layout.activity_login);

        initializeUIViews();
        initializeButtonListeners();
      //  loginValidator = new Validator(this);
      //  loginValidator.setValidationListener(this);
        // Obtain the shared Tracker instance.
     //   AppController application = (AppController) getApplication();
        appManager = new AppManager(this);

        if(userStatus.getUser_mobile().compareTo("")!=0){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra(Constant.url, API.COMPLAINANT_SIGN_IN_TAG_URL);
            startActivity(intent);
            finish();
        }else {
            Intent intent = getIntent();
            if (intent != null) {
                message = intent.getStringExtra(Constant.message);
                if (message != null && message.length() != 0)
                    snackbarMessages(message);

            }
        }
    }

    private void initializeButtonListeners() {
        Log.d(TAG, "initializeButtonListeners()");
        signUpText.setOnClickListener(this);
        complainant_sign_in_button.setOnClickListener(this);
        admin_sign_in_button.setOnClickListener(this);
    }

    private void initializeUIViews() {
        Log.d(TAG, "initializeUIViews()");
        signUpText= (TextView) findViewById(R.id.signUpText);
        complainant_sign_in_button= (LinearLayout) findViewById(R.id.complainant_sign_in_button);
        admin_sign_in_button= (LinearLayout) findViewById(R.id.admin_sign_in_button);
        mobileNumberEditText = (EditText) findViewById(R.id.mobile_number_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        createAlertDialog();

        createProgressDialogView();
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        Log.d(TAG, "showProgress(final boolean show)");
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_longAnimTime);

            loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            loginForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            loginProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            loginProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loginProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        showAlertDialog();
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick(View view)");
        switch (view.getId()) {
            case R.id.complainant_sign_in_button:
                Log.d(TAG, "Log In Button Pressed.");
                if (!checkEmpty(mobileNumberEditText)) {
                    snackbarMessages("Enter your mobile number.");
                   // Toast.makeText(LoginActivity.this, "Enter your mobile number.", Toast.LENGTH_SHORT).show();
                } else if (!checkEmpty(passwordEditText)) {
                    snackbarMessages("Enter your password.");
                   // Toast.makeText(LoginActivity.this, "Enter your password.", Toast.LENGTH_SHORT).show();
                } else if (!mobileNumberEditText.getText().toString().trim().matches(VALID_MOBILE_REG_EX)) {
                    snackbarMessages("Enter a valid mobile number.");
                   // Toast.makeText(LoginActivity.this, "Enter a valid mobile number.", LENGTH_SHORT).show();
                }else if (checkEmpty(mobileNumberEditText) && checkEmpty(passwordEditText)) {

                    userStatus.setUser_mobile(mobileNumberEditText.getText().toString().trim());
                    userStatus.setUser_password(passwordEditText.getText().toString().trim());
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra(Constant.url, API.COMPLAINANT_SIGN_IN_TAG_URL);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.admin_sign_in_button:
                Log.d(TAG, "Sign Up Activity Transfer Button Pressed.");
              //  startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                snackbarMessages("Working on it");
                break;
//            case R.id.back_button:
//                Log.d(TAG, "back button Pressed.");
//                finish();
//                break;
            case R.id.signUpText:
                Log.d(TAG, "Sign Up Activity Transfer Button Pressed.");
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra(Constant.url, API.SIGN_UP_TAG_URL);
                startActivity(intent);

                finish();
                break;
        }
    }

    private void snackbarMessages(String messages){
        Snackbar snackbar = Snackbar
                .make(complainant_sign_in_button, messages, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    private boolean checkEmpty(EditText editText) {
        Log.d(TAG, "checkEmpty(EditText editText)");
        return editText.getText().toString().trim().length() != 0;
    }
//    @Override
//    public void onValidationSucceeded() {
//        Log.d(TAG, "onValidationSucceeded()");
//        if (appManager.isNetworkAvailable()) {
//            Log.d(TAG, "handshakeRequest done");
//
//            mobile = mobileNumberEditText.getText().toString().trim();
//            password = passwordEditText.getText().toString().trim();
//            userLogin(mobile,password);
//
//        } else {
//
//            noInternetAlertDialog.show();
//
//        }
//
//    }

//    @Override
//    public void onValidationFailed(List<ValidationError> errors) {
//        Log.d(TAG, "onValidationFailed(List<ValidationError> errors) totalError = " + errors.size());
//        for (ValidationError validationError : errors) {
//            makeText(LoginActivity.this, validationError.getCollatedErrorMessage(LoginActivity.this),
//                    LENGTH_SHORT).show();
//            break;
//        }
//    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Log.d(TAG, "onClick(DialogInterface dialog, int which)");
        if (dialog.equals(noInternetAlertDialog)) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    Log.d(TAG, "noInternetAlertDialog Positive Button Pressed");
                    if (appManager.isNetworkAvailable()) {
                        noInternetAlertDialog.cancel();
                        noInternetAlertDialog.dismiss();
                        userLogin(mobile,password);
                    } else {
                        noInternetAlertDialog.cancel();
                        noInternetAlertDialog = getNoInternetAlertDialogBuilder().create();
                        noInternetAlertDialog.show();
                    }
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    Log.d(TAG, "noInternetAlertDialog Neutral Button Pressed");
                    noInternetAlertDialog.cancel();
                    startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    Log.d(TAG, "noInternetAlertDialog Negative Button Pressed");
                    finish();
                    break;
            }
        } else if (dialog.equals(errorAlertDialog)) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    Log.d(TAG, "errorAlertDialog Positive Button Pressed");
                    userLogin(mobile,password);
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    Log.d(TAG, "errorAlertDialog Neutral Button Pressed");
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    Log.d(TAG, "errorAlertDialog Negative Button Pressed");
                    errorAlertDialog.cancel();
                    errorAlertDialog.dismiss();
                    finish();
                    break;
            }
        }
    }

    private void userLogin (String mobile, String password){
        Map<String, String> params = new HashMap<>();
        params.put(API.Parameter.MOBILE_NUMBER,mobile);
        params.put(API.Parameter.PASSWORD, password);
        params.put(API.Parameter.ANDROID_DEVICE_ID, appManager.getDeviceId());
        params.put(API.Parameter.ANDROID_APP_VERSION, appManager.getAppVersion());

        GetUrlBuilder getUrlBuilder = new GetUrlBuilder(API.COMPLAINANT_SIGN_IN_TAG_URL, params);
        String Url = getUrlBuilder.getQueryUrl();
        Log.d(TAG, "URL:" + Url);
        Log.d(TAG, "PARAMS:" + params.toString());

        loginVerificationObjectRequest = new ObjectRequest<>(API.Method.USER_lOGIN_API_METHOD, Url, params, new Response.Listener<LoginVerification>() {
            @Override
            public void onResponse(LoginVerification response) {
                progressBarDialog.cancel();

                if (response.getData()!= null) {
                    UserStatus userStatus=new UserStatus(LoginActivity.this);
                    userStatus.setLogin(true);
                    userStatus.setUser_id(response.getData().getUser_id());
                    userStatus.setUser_full_name(response.getData().getFirst_name()+" "+response.getData().getLast_name());
                    userStatus.setUser_mobile(response.getData().getMobile_number());
                    Toast.makeText(LoginActivity.this, "Logged In successfully.", LENGTH_SHORT).show();
                    finish();

                } else {

//                    String message = "";
//                    for (String m : response.getError().getMessages()) {
//                        message += (m + "\n");
//                    }
                   // make(loginForm, message, Snackbar.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Log.e(TAG, Integer.toString(error.networkResponse.statusCode));
                } catch (Exception e) {
                    if (timeoutCounter != 2) {
                        timeoutCounter++;
                        appController.addToRequestQueue(loginVerificationObjectRequest);
                        return;
                    } else {
                        timeoutCounter = 0;
                    }
                }
                progressBarDialog.cancel();
                String message = "";
                try {
                    Gson gson = new GsonBuilder().create();
                    LoginVerification response = gson.fromJson(new String(error.networkResponse.data, "UTF-8"), LoginVerification.class);
//                    for (String m : response.getError().getMessages()) {
//                        message += (m + "\n");
//                    }
                } catch (Exception e) {
                    message = "Something went wrong.Please Check your Internet Connection";
                }
                make(loginForm, message, Snackbar.LENGTH_SHORT).show();
            }
        },LoginVerification.class);

        progressBarDialog.show();
        appController.addToRequestQueue(loginVerificationObjectRequest);

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    protected void createProgressDialogView() {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
//        View view = View.inflate(LoginActivity.this, R.layout.progressbar_view, null);
//        progressBarMessageTextView = (TextView) view.findViewById(R.id.progress_bar_message_text_view);
//        alertDialogBuilder.setView(view);
//        alertDialogBuilder.setCancelable(false);
//        progressBarDialog = alertDialogBuilder.create();
    }

    public AlertDialog.Builder getNoInternetAlertDialogBuilder() {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setNeutralButton(R.string.action_settings, this);
//        alertDialogBuilder.setPositiveButton(R.string.try_again, this);
//        alertDialogBuilder.setNegativeButton(R.string.close_app, this);
//        alertDialogBuilder.setTitle(R.string.error);
//        alertDialogBuilder.setMessage(R.string.no_internet_connection);
        return alertDialogBuilder;
    }

    private void createAlertDialog() {
        Log.d(TAG, "createAlertDialog()");
        noInternetAlertDialog = getNoInternetAlertDialogBuilder().create();
        errorAlertDialog = getErrorAlertDialog().create();
    }

    private AlertDialog.Builder getErrorAlertDialog() {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setPositiveButton(R.string.try_again, this);
//        alertDialogBuilder.setNegativeButton(R.string.close_app, this);
//        alertDialogBuilder.setTitle(R.string.error);
//        alertDialogBuilder.setMessage(R.string.server_connection_fail);
        return alertDialogBuilder;
    }

    private void showAlertDialog(){
        android.app.AlertDialog.Builder alertDialog2 = new android.app.AlertDialog.Builder(
                LoginActivity.this);

// Setting Dialog Title
        alertDialog2.setTitle("Want to exit ?");

// Setting Dialog Message
        //  alertDialog2.setMessage("Are you sure you want delete this file?");

// Setting Icon to Dialog
        //   alertDialog2.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("Yes",
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
        alertDialog2.setNegativeButton("No",
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

