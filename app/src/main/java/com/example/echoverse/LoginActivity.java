package com.example.echoverse;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    // Login UI elements
    private EditText etLoginEmail, etLoginPassword;
    private Button btnLogin, btnGuest;
    private TextView tvForgotPassword;

    // Register UI elements
    private RadioGroup rgAccountType;
    private RadioButton rbChild, rbParent;
    private EditText etChildName, etChildAge, etParentEmail, etRegisterPassword, etConfirmPassword;
    @SuppressWarnings("unused")
    private ImageView ivChildAvatar;
    private CheckBox cbTerms;
    private Button btnRegister, btnChooseAvatar;
    private com.google.android.material.textfield.TextInputLayout tilChildName, tilChildAge, tilParentEmail;

    // Tabs
    private TabLayout tabLayout;
    private LinearLayout loginTab, registerTab;

    // Social login buttons
    private Button btnFacebookLogin, btnGoogleLogin;

    // User data
    @SuppressWarnings("unused")
    private String childAvatarUrl = "";
    private boolean isParentAccount = false;

    // Local storage (temporary alternative to Firebase)
    private final Map<String, String> localUsers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setup full screen UI
        setupUI();

        // Setup back pressed handler
        setupBackPressedHandler();

        // Check if user is already logged in (temporary alternative)
        if (isUserLoggedInLocally()) {
            navigateToMain();
        }

        initViews();
        setupTabLayout();
        setupClickListeners();
        setupRadioGroup();

        // Add test user
        localUsers.put("test@example.com", "password123");
        localUsers.put("parent@example.com", "password123");
    }

    private void setupUI() {
        // Make sure the UI is properly sized
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Set status bar color to match design
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary));
        }
    }

    private void setupBackPressedHandler() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Show exit dialog
                new android.app.AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Exit EchoVerse")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            finishAffinity();
                            System.exit(0);
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        };

        // Add the callback
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void initViews() {
        // Login
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGuest = findViewById(R.id.btnGuest);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Register
        rgAccountType = findViewById(R.id.rgAccountType);
        rbChild = findViewById(R.id.rbChild);
        rbParent = findViewById(R.id.rbParent);
        etChildName = findViewById(R.id.etChildName);
        etChildAge = findViewById(R.id.etChildAge);
        etParentEmail = findViewById(R.id.etParentEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        ivChildAvatar = findViewById(R.id.ivChildAvatar);
        cbTerms = findViewById(R.id.cbTerms);
        btnRegister = findViewById(R.id.btnRegister);
        btnChooseAvatar = findViewById(R.id.btnChooseAvatar);

        // Dynamic fields - تم التعديل هنا
        tilChildName = findViewById(R.id.tilChildName);
        tilChildAge = findViewById(R.id.tilChildAge);
        tilParentEmail = findViewById(R.id.tilParentEmail);

        // Tabs
        tabLayout = findViewById(R.id.tabLayout);
        loginTab = findViewById(R.id.loginTab);
        registerTab = findViewById(R.id.registerTab);

        // Social login
        btnFacebookLogin = findViewById(R.id.btnFacebookLogin);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
    }

    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    // Login
                    loginTab.setVisibility(View.VISIBLE);
                    registerTab.setVisibility(View.GONE);
                    // Reset focus
                    etLoginEmail.requestFocus();
                } else {
                    // Register
                    loginTab.setVisibility(View.GONE);
                    registerTab.setVisibility(View.VISIBLE);
                    // Reset to default selection
                    if (rbChild != null) {
                        rbChild.setChecked(true);
                        isParentAccount = false;
                        updateRegisterFieldsVisibility();
                    }
                    // Set focus to first field
                    etChildName.requestFocus();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupClickListeners() {
        // Login
        btnLogin.setOnClickListener(v -> loginUser());

        // Register
        btnRegister.setOnClickListener(v -> registerUser());

        // Guest
        btnGuest.setOnClickListener(v -> loginAsGuest());

        // Forgot password
        tvForgotPassword.setOnClickListener(v -> resetPassword());

        // Choose avatar
        btnChooseAvatar.setOnClickListener(v -> chooseAvatar());

        // Social login
        btnFacebookLogin.setOnClickListener(v -> loginWithFacebook());
        btnGoogleLogin.setOnClickListener(v -> loginWithGoogle());
    }

    private void setupRadioGroup() {
        rgAccountType.setOnCheckedChangeListener((group, checkedId) -> {
            isParentAccount = (checkedId == R.id.rbParent);
            updateRegisterFieldsVisibility();
        });

        // Default selection
        rbChild.setChecked(true);
        isParentAccount = false;
        updateRegisterFieldsVisibility();
    }

    private void updateRegisterFieldsVisibility() {
        if (tilChildName != null) tilChildName.setVisibility(isParentAccount ? View.GONE : View.VISIBLE);
        if (tilChildAge != null) tilChildAge.setVisibility(isParentAccount ? View.GONE : View.VISIBLE);
        if (tilParentEmail != null) tilParentEmail.setVisibility(isParentAccount ? View.GONE : View.VISIBLE);

        LinearLayout avatarSection = findViewById(R.id.avatarSection);
        if (avatarSection != null) {
            avatarSection.setVisibility(isParentAccount ? View.GONE : View.VISIBLE);
        }

        if (etParentEmail != null) {
            if (isParentAccount) {
                etParentEmail.setHint("Your Email");
            } else {
                etParentEmail.setHint("Parent Email for Notifications");
            }
        }
    }

    private void loginUser() {
        String email = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etLoginEmail.setError("Please enter email");
            etLoginEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etLoginPassword.setError("Please enter password");
            etLoginPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etLoginPassword.setError("Password must be at least 6 characters");
            etLoginPassword.requestFocus();
            return;
        }

        showLoading(true);

        // Simulate login (temporary alternative)
        new Handler().postDelayed(() -> {
            showLoading(false);

            if (localUsers.containsKey(email) && localUsers.get(email).equals(password)) {
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                saveLoginLocally(email, false);
                navigateToMain();
            } else {
                Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                etLoginPassword.setText("");
                etLoginPassword.requestFocus();
            }
        }, 1500);
    }

    private void registerUser() {
        // Validate data
        if (!validateRegistrationData()) {
            return;
        }

        showLoading(true);

        String email = "";
        String password = etRegisterPassword.getText().toString().trim();

        try {
            // Determine account type
            if (!isParentAccount) {
                // Child account - email is optional
                email = etParentEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    // Create guest account without email
                    createGuestAccount();
                    return;
                }
            } else {
                // Parent account - email is required
                email = etParentEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    etParentEmail.setError("Please enter email");
                    etParentEmail.requestFocus();
                    showLoading(false);
                    return;
                }
            }

            // Validate email format
            if (!isValidEmail(email)) {
                etParentEmail.setError("Please enter valid email address");
                etParentEmail.requestFocus();
                showLoading(false);
                return;
            }

            // Make sure password is not empty
            if (TextUtils.isEmpty(password)) {
                etRegisterPassword.setError("Please enter password");
                etRegisterPassword.requestFocus();
                showLoading(false);
                return;
            }

            // Continue with registration
            continueRegistration(email, password);

        } catch (Exception e) {
            showLoading(false);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateRegistrationData() {
        // Check terms
        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "You must accept terms and conditions", Toast.LENGTH_SHORT).show();
            cbTerms.requestFocus();
            return false;
        }

        // If child account
        if (!isParentAccount) {
            String childName = etChildName.getText().toString().trim();
            String childAge = etChildAge.getText().toString().trim();

            if (TextUtils.isEmpty(childName)) {
                etChildName.setError("Please enter child name");
                etChildName.requestFocus();
                return false;
            }

            if (TextUtils.isEmpty(childAge)) {
                etChildAge.setError("Please enter child age");
                etChildAge.requestFocus();
                return false;
            }

            try {
                int age = Integer.parseInt(childAge);
                if (age < 4 || age > 12) {
                    etChildAge.setError("Age must be between 4 and 12");
                    etChildAge.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                etChildAge.setError("Please enter valid age");
                etChildAge.requestFocus();
                return false;
            }
        }

        // Check password
        String password = etRegisterPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            etRegisterPassword.setError("Please enter password");
            etRegisterPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etRegisterPassword.setError("Password must be at least 6 characters");
            etRegisterPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void continueRegistration(String email, String password) {
        // Simulate account creation
        new Handler().postDelayed(() -> {
            showLoading(false);

            // Check if email already exists
            if (localUsers.containsKey(email)) {
                Toast.makeText(LoginActivity.this, "Email already registered", Toast.LENGTH_LONG).show();
                etParentEmail.requestFocus();
                return;
            }

            // Save user locally
            localUsers.put(email, password);
            saveLoginLocally(email, isParentAccount);

            // Save additional data for child accounts
            if (!isParentAccount) {
                String childName = etChildName.getText().toString().trim();
                String childAge = etChildAge.getText().toString().trim();
                String parentEmail = etParentEmail.getText().toString().trim();

                // Save child info in preferences
                SharedPreferences prefs = getSharedPreferences("EchoVersePrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("childName", childName);
                editor.putInt("childAge", Integer.parseInt(childAge));
                editor.apply();

                // Simulate parent notification
                if (!TextUtils.isEmpty(parentEmail)) {
                    Toast.makeText(this, "Notifications will be sent to " + parentEmail + " for dangerous sounds", Toast.LENGTH_LONG).show();
                }
            }

            Toast.makeText(LoginActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();

            // Clear form
            clearRegisterForm();

            navigateToMain();
        }, 2000);
    }

    private void createGuestAccount() {
        showLoading(true);

        new Handler().postDelayed(() -> {
            showLoading(false);

            // Guest account without email
            String guestId = "guest_" + System.currentTimeMillis();
            saveLoginLocally(guestId, false);

            // Clear form
            clearRegisterForm();

            Toast.makeText(LoginActivity.this, "Guest account created successfully", Toast.LENGTH_SHORT).show();
            navigateToMain();
        }, 1500);
    }

    private void loginAsGuest() {
        showLoading(true);

        new Handler().postDelayed(() -> {
            showLoading(false);

            String guestId = "guest_" + System.currentTimeMillis();
            saveLoginLocally(guestId, false);

            Toast.makeText(LoginActivity.this, "Logged in as guest", Toast.LENGTH_SHORT).show();
            navigateToMain();
        }, 1000);
    }

    private void resetPassword() {
        String email = etLoginEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etLoginEmail.setError("Enter your email first");
            etLoginEmail.requestFocus();
            return;
        }

        if (!isValidEmail(email)) {
            etLoginEmail.setError("Please enter valid email address");
            etLoginEmail.requestFocus();
            return;
        }

        showLoading(true);

        new Handler().postDelayed(() -> {
            showLoading(false);
            Toast.makeText(this, "Password reset link has been sent to: " + email, Toast.LENGTH_LONG).show();
        }, 1500);
    }

    private void chooseAvatar() {
        Toast.makeText(this, "Avatar selection will be available in full version", Toast.LENGTH_SHORT).show();
    }

    private void loginWithFacebook() {
        Toast.makeText(this, "Facebook login will be available in full version", Toast.LENGTH_SHORT).show();
    }

    private void loginWithGoogle() {
        Toast.makeText(this, "Google login will be available in full version", Toast.LENGTH_SHORT).show();
    }

    private void saveLoginLocally(String userId, boolean isParent) {
        // Save login data locally
        SharedPreferences prefs = getSharedPreferences("EchoVersePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userId", userId);
        editor.putBoolean("isLoggedIn", true);
        editor.putBoolean("isGuest", userId.startsWith("guest_"));
        editor.putBoolean("isParent", isParent);
        editor.putLong("loginTime", System.currentTimeMillis());
        editor.apply();
    }

    private boolean isUserLoggedInLocally() {
        SharedPreferences prefs = getSharedPreferences("EchoVersePrefs", MODE_PRIVATE);
        return prefs.getBoolean("isLoggedIn", false);
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean show) {
        if (show) {
            // Disable all buttons
            if (btnLogin != null) btnLogin.setEnabled(false);
            if (btnRegister != null) btnRegister.setEnabled(false);
            if (btnGuest != null) btnGuest.setEnabled(false);
            if (btnFacebookLogin != null) btnFacebookLogin.setEnabled(false);
            if (btnGoogleLogin != null) btnGoogleLogin.setEnabled(false);
            if (btnChooseAvatar != null) btnChooseAvatar.setEnabled(false);

            // Change button text to show loading
            if (btnLogin != null) btnLogin.setText("LOGGING IN...");
            if (btnRegister != null) btnRegister.setText("CREATING ACCOUNT...");
            if (btnGuest != null) btnGuest.setText("LOADING...");
        } else {
            // Enable all buttons
            if (btnLogin != null) {
                btnLogin.setEnabled(true);
                btnLogin.setText("LOGIN");
            }
            if (btnRegister != null) {
                btnRegister.setEnabled(true);
                btnRegister.setText("CREATE ACCOUNT");
            }
            if (btnGuest != null) {
                btnGuest.setEnabled(true);
                btnGuest.setText("TRY AS GUEST");
            }
            if (btnFacebookLogin != null) btnFacebookLogin.setEnabled(true);
            if (btnGoogleLogin != null) btnGoogleLogin.setEnabled(true);
            if (btnChooseAvatar != null) btnChooseAvatar.setEnabled(true);
        }
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void clearRegisterForm() {
        // Clear all register form fields
        if (etChildName != null) etChildName.setText("");
        if (etChildAge != null) etChildAge.setText("");
        if (etParentEmail != null) etParentEmail.setText("");
        if (etRegisterPassword != null) etRegisterPassword.setText("");
        if (etConfirmPassword != null) etConfirmPassword.setText("");
        if (cbTerms != null) cbTerms.setChecked(false);

        // Reset to default selection
        if (rgAccountType != null) {
            rbChild.setChecked(true);
            isParentAccount = false;
            updateRegisterFieldsVisibility();
        }
    }
}