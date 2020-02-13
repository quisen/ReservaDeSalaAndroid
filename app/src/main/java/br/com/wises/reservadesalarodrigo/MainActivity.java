package br.com.wises.reservadesalarodrigo;

import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Fade;
import androidx.transition.Transition;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    LottieAnimationView lottieLoading, calendarLottie;
    ProgressBar loadingProgressBar;
    TextInputLayout usuarioTextInputLayout;
    TextInputLayout senhaTextInputLayout;
    MaterialButton loginButton, registerButton;
    String usuario = "";
    String senha = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuarioTextInputLayout = findViewById(R.id.et_usuario);

        senhaTextInputLayout = findViewById(R.id.et_senha);

        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        loadingProgressBar = findViewById(R.id.loading);
        lottieLoading = findViewById(R.id.loadingLottie);
        calendarLottie = findViewById(R.id.calendarLottie);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = usuarioTextInputLayout.getEditText().getText().toString();
                senha = senhaTextInputLayout.getEditText().getText().toString();
                if (usuario.isEmpty()) {
                    usuarioTextInputLayout.setError("Digite um email válido");
                    senhaTextInputLayout.setErrorEnabled(false);
                }
                else if (senha.isEmpty()) {
                    senhaTextInputLayout.setError("Digite sua senha");
                    usuarioTextInputLayout.setErrorEnabled(false);
                }
                else {
                    try {
                        usuarioTextInputLayout.setErrorEnabled(false);
                        senhaTextInputLayout.setErrorEnabled(false);
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("authorization", "secret");
                        params.put("email", usuario);
                        params.put("password", senha);
                        String url = "http://172.30.248.56:8080/ReservaDeSala/rest/usuario/login";

                        loginButton.setEnabled(false);
                        calendarLottie.setVisibility(View.GONE);
//                        loadingProgressBar.setVisibility(View.VISIBLE);
                        lottieLoading.setVisibility(View.VISIBLE);
                        loginButton.setVisibility(View.GONE);
                        usuarioTextInputLayout.setVisibility(View.GONE);
                        senhaTextInputLayout.setVisibility(View.GONE);
                        registerButton.setVisibility(View.GONE);
                        new HttpRequest(
                                getApplicationContext(),
                                params,
                                url,
                                "GET").doRequest();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void customEventReceived(Event event) {
        if (event.getEventName().equals("RequestSuccess")) {
            showToastLogin("Login Sucesso ! Redirecionar para Tela Princial (" + event.getEventMsg() + ")");
        } else if (event.getEventName().equals("RequestError")) {
            showToastLogin("Erro ao fazer login ! (" + event.getEventMsg() + ")");
        }
//        loadingProgressBar.setVisibility(View.GONE);
        lottieLoading.setVisibility(View.GONE);
        usuarioTextInputLayout.setVisibility(View.VISIBLE);
        senhaTextInputLayout.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        loginButton.setEnabled(true);
        calendarLottie.setVisibility(View.VISIBLE);
    }

    public void showToastLogin(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);

    }

    public void login() {
        try {
            String url = "http://172.30.248.56:8080/ReservaDeSala/rest/usuario/login";
            // Formulate the request and handle the response.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("Response Login: " + response);
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            loadingProgressBar.setVisibility(View.GONE);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                String errorResult = new String(error.networkResponse.data, "UTF-8");
                                Toast.makeText(getApplicationContext(), errorResult, Toast.LENGTH_SHORT).show();
                                System.out.println("Response Error: " + errorResult);
                                loadingProgressBar.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                loadingProgressBar.setVisibility(View.GONE);
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("authorization", "secret");
                    params.put("email", usuario);
                    params.put("password", senha);

                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

