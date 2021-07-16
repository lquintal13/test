package com.example.testmacropay.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.example.testmacropay.R;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView titulo = findViewById(R.id.titulo);
        final TextView url = findViewById(R.id.url);
        final TextView email = findViewById(R.id.email);
        final TextView solicitud = findViewById(R.id.solicitud);
        final Button btnSalir = findViewById(R.id.salir);

        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        String token = sharedPreferences.getString("token",null);
        JWT parsedJWT = new JWT(token);

        titulo.setText(parsedJWT.getClaim("titular").asString());
        url.setText(parsedJWT.getClaim("url").asString());
        email.setText(parsedJWT.getClaim("email").asString());
        solicitud.setText(parsedJWT.getClaim("solicitud").asString());

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.clear().apply();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);                      //If token is expired show Login Activity to user.
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}