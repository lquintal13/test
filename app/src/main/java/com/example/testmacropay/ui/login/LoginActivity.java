package com.example.testmacropay.ui.login;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.testmacropay.R;
import com.example.testmacropay.data.interfaces.LoginAPI;
import com.example.testmacropay.data.model.Login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private  SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);

        if (token!=null){
            JWT jwt = new JWT(token);
            if (!jwt.isExpired(10)) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        }

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://testandroid.macropay.com.mx/")
                    .addConverterFactory(GsonConverterFactory.create()).build();

            LoginAPI loginAPI = retrofit.create(LoginAPI.class);
            Call<Login> call = loginAPI.postLogin(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    try {
                        if (response.isSuccessful()){
                            Login l = response.body();
                            if (l.getSuccess()){

                                SharedPreferences.Editor editor= sharedPreferences.edit();
                                editor.putString("token",l.getToken());
                                editor.apply();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();

                            }else {
                                Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }catch (Exception ex){
                        Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Problema de conexión al consumir el API", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}