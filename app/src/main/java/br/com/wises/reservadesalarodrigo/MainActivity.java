package br.com.wises.reservadesalarodrigo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
	ProgressBar loadingProgressBar;
	String usuario = "";
	String senha = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final TextInputLayout usuarioTextInputLayout = findViewById(R.id.et_usuario);
		final TextInputLayout senhaTextInputLayout = findViewById(R.id.et_senha);
		final MaterialButton loginButton = findViewById(R.id.login);
		loadingProgressBar = findViewById(R.id.loading);

		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadingProgressBar.setVisibility(View.VISIBLE);
				usuario = usuarioTextInputLayout.getEditText().getText().toString();
				senha = senhaTextInputLayout.getEditText().getText().toString();
				System.out.println("usuario: "+usuario + " - " + "senha: " + senha);

//				Map<String, String> params = new HashMap<String, String>();
//				params.put("authorization", "secret");
//				params.put("email", usuario);
//				params.put("password", senha);
//				String url = "http://172.30.248.56:8080/ReservaDeSala/rest/usuario/login";
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("authorization", "secret");
					params.put("email", usuario);
					params.put("password", senha);
					String url = "http://172.30.248.56:8080/ReservaDeSala/rest/usuario/login";

					loadingProgressBar.setVisibility(View.VISIBLE);
					new HttpRequest(
							getApplicationContext(),
							params,
							url,
							"GET").doRequest();
				} catch (Exception e) {
					e.printStackTrace();
				}
				loadingProgressBar.setVisibility(View.VISIBLE);
			}
		});
	}

	public void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}
	public void onStop(){
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	@Subscribe
	public void customEventReceived(Event event){
		if (event.getEventName().equals("LoginSucesso")) {
			showToastLogin("Login Sucesso ! Redirecionar para Tela Princial (" + event.getEventMsg() + ")");
		} else if (event.getEventName().equals("LoginErro")) {
			showToastLogin("Erro ao fazer login ! (" + event.getEventMsg() + ")");
		}
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

