package com.revesoft.grs.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import com.revesoft.grs.util.GetUrlBuilder;
import com.revesoft.grs.util.ObjectRequest;
import com.revesoft.grs.util.api.data.item.user.UserStatus;

import java.util.HashMap;
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


    private LinearLayout shohozSignInButton;
    private Button signUpActivityTransferButton;
    private ImageButton backButton;

    private ProgressBar loginProgressBar;
    private View shohozLoginForm;
    private AppManager appManager;
    private String mobile,password;
    ObjectRequest<LoginVerification> loginVerificationObjectRequest;
    protected TextView progressBarMessageTextView;
    private AlertDialog noInternetAlertDialog;
    private AlertDialog errorAlertDialog;
    protected AlertDialog progressBarDialog;
    private AppController appController = AppController.getInstance();
    int timeoutCounter = 0;
//    private Validator loginValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeUIViews();
        initializeButtonListeners();
      //  loginValidator = new Validator(this);
      //  loginValidator.setValidationListener(this);
        // Obtain the shared Tracker instance.
     //   AppController application = (AppController) getApplication();
        appManager = new AppManager(this);
    }

    private void initializeButtonListeners() {
        Log.d(TAG, "initializeButtonListeners()");
//        shohozSignInButton.setOnClickListener(this);
 //       signUpActivityTransferButton.setOnClickListener(this);
//        backButton.setOnClickListener(this);
    }

    private void initializeUIViews() {
        Log.d(TAG, "initializeUIViews()");
        mobileNumberEditText = (EditText) findViewById(R.id.mobile_number_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
//        shohozSignInButton = (LinearLayout) findViewById(R.id.shohoz_sign_in_button);
//        backButton=(ImageButton) findViewById(R.id.back_button);
//        signUpActivityTransferButton = (Button) findViewById(R.id.sign_up_activity_transfer_button);
//        loginProgressBar = (ProgressBar) findViewById(R.id.login_progress_bar);
//        shohozLoginForm = findViewById(R.id.shohoz_login_form);
//        createAlertDialog();
//        signUpActivityTransferButton = (Button) findViewById(R.id.sign_up_activity_transfer_button);
//        loginProgressBar = (ProgressBar) findViewById(R.id.login_progress_bar);
 //       shohozLoginForm = findViewById(R.id.shohoz_login_form);
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

            shohozLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            shohozLoginForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    shohozLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
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
            shohozLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick(View view)");
        switch (view.getId()) {
//            case R.id.shohoz_sign_in_button:
//                Log.d(TAG, "Log In Button Pressed.");
//                if (!checkEmpty(mobileNumberEditText)) {
//                    Toast.makeText(LoginActivity.this, "Enter your mobile number.", Toast.LENGTH_SHORT).show();
//                } else if (!checkEmpty(passwordEditText)) {
//                    Toast.makeText(LoginActivity.this, "Enter your password.", Toast.LENGTH_SHORT).show();
//                } else if (!mobileNumberEditText.getText().toString().trim().matches(VALID_MOBILE_REG_EX)) {
//                    Toast.makeText(LoginActivity.this, "Enter a valid mobile number.", LENGTH_SHORT).show();
//                }else if (checkEmpty(mobileNumberEditText) && checkEmpty(passwordEditText)) {
//                   // loginValidator.validate();
//                }
//                break;
//            case R.id.sign_up_activity_transfer_button:
//                Log.d(TAG, "Sign Up Activity Transfer Button Pressed.");
//              //  startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
//                finish();
//                break;
//            case R.id.back_button:
//                Log.d(TAG, "back button Pressed.");
//                finish();
//                break;
        }
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

        GetUrlBuilder getUrlBuilder = new GetUrlBuilder(API.USER_LOGIN_API_URL, params);
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
                   // make(shohozLoginForm, message, Snackbar.LENGTH_SHORT).show();
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
                make(shohozLoginForm, message, Snackbar.LENGTH_SHORT).show();
            }
        },LoginVerification.class);

        progressBarDialog.show();
        appController.addToRequestQueue(loginVerificationObjectRequest);

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

}


