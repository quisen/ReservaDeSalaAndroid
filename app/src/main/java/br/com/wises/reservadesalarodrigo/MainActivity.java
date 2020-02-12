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

		final EditText usuarioEditText = findViewById(R.id.et_usuario);
		final EditText senhaEditText = findViewById(R.id.et_senha);
		final Button loginButton = findViewById(R.id.login);
		loadingProgressBar = findViewById(R.id.loading);

		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadingProgressBar.setVisibility(View.VISIBLE);
				usuario = usuarioEditText.getText().toString();
				senha = senhaEditText.getText().toString();
//				login();

				Map<String, String> params = new HashMap<String, String>();
				params.put("authorization", "secret");
				params.put("email", usuario);
				params.put("password", senha);
				String url = "http://172.30.248.56:8080/ReservaDeSala/rest/usuario/login";
				login();
				loadingProgressBar.setVisibility(View.GONE);
			}
		});
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

							loadingProgressBar.setVisibility(View.GONE);
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							if (error instanceof TimeoutError || error instanceof NoConnectionError) {
								System.out.println("Response TimeoutError: " + error.toString());
							} else if (error instanceof AuthFailureError) {
								try {
									System.out.println("Response AuthFailureError: " + new String(error.networkResponse.data, "UTF-8"));
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
							} else if (error instanceof ServerError) {
								System.out.println("Response ServerError: " + error.toString());
							} else if (error instanceof NetworkError) {
								System.out.println("Response NetworkError: " + error.toString());
							} else if (error instanceof ParseError) {
								System.out.println("Response ParseError: " + error.toString());
							}
							loadingProgressBar.setVisibility(View.GONE);
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

