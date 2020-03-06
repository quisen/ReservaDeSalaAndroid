package br.com.wises.reservadesalarodrigo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import br.com.wises.reservadesalarodrigo.R;
import br.com.wises.reservadesalarodrigo.models.Event;
import br.com.wises.reservadesalarodrigo.models.Usuario;
import br.com.wises.reservadesalarodrigo.network.HttpRequest;
import br.com.wises.reservadesalarodrigo.utils.Constants;
import br.com.wises.reservadesalarodrigo.utils.TinyDB;

public class MainActivity extends AppCompatActivity {
	LottieAnimationView lottieLoading, calendarLottie;
	ProgressBar loadingProgressBar;
	TextInputLayout usuarioTextInputLayout;
	TextInputLayout senhaTextInputLayout;
	MaterialButton loginButton, cadastrarButton;
	String usuario = "";
	String senha = "";
	long millis = 0L;
	TinyDB tinyDB;
	Usuario usuarioLogado = new Usuario();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tinyDB = new TinyDB(getApplicationContext());

		setContentView(R.layout.activity_main);

		usuarioTextInputLayout = findViewById(R.id.et_usuario);
		senhaTextInputLayout = findViewById(R.id.et_senha);
		String userEmail = tinyDB.getString("userEmail");
		if (userEmail != null) {
			usuarioTextInputLayout.getEditText().setText(userEmail);
		}
		loginButton = findViewById(R.id.login);
		cadastrarButton = findViewById(R.id.bt_cadastrar);
		loadingProgressBar = findViewById(R.id.loading);
		lottieLoading = findViewById(R.id.loadingLottie);
		calendarLottie = findViewById(R.id.calendarLottie);
		cadastrarButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Cadastro.class);
				startActivity(intent);
			}
		});
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				closeKeyBoard();
				usuario = usuarioTextInputLayout.getEditText().getText().toString();
				senha = senhaTextInputLayout.getEditText().getText().toString();
				if (usuario.isEmpty()) {
					usuarioTextInputLayout.setError("Digite um email válido");
					senhaTextInputLayout.setErrorEnabled(false);
				} else if (senha.isEmpty()) {
					senhaTextInputLayout.setError("Digite sua senha");
					usuarioTextInputLayout.setErrorEnabled(false);
				} else {
					try {
						usuarioTextInputLayout.setErrorEnabled(false);
						senhaTextInputLayout.setErrorEnabled(false);
						Map<String, String> params = new HashMap<String, String>();
						params.put("authorization", "secret");
						params.put("email", usuario);
						params.put("password", senha);
						String url = "usuario/login";

						loginButton.setEnabled(false);
						calendarLottie.setVisibility(View.GONE);
//                        loadingProgressBar.setVisibility(View.VISIBLE);
						lottieLoading.setVisibility(View.VISIBLE);
						loginButton.setVisibility(View.GONE);
						usuarioTextInputLayout.setVisibility(View.GONE);
						senhaTextInputLayout.setVisibility(View.GONE);
						cadastrarButton.setVisibility(View.GONE);
						new HttpRequest(
								getApplicationContext(),
								params,
								url,
								"GET", "Login").doRequest();
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
		if (event.getEventName().equals("Login" + Constants.eventSuccessLabel)) {
//            showToastLogin("Login Sucesso ! Redirecionar para Tela Princial (" + event.getEventMsg() + ")");
			Gson gson = new Gson();
			String responseJson = event.getEventMsg();
			System.out.println(responseJson);
			usuarioLogado = gson.fromJson(responseJson, Usuario.class);

			tinyDB.putString("userEmail", usuarioLogado.getEmail());
			tinyDB.putString("id_organizacao", String.valueOf(usuarioLogado.getIdOrganizacao()));
			tinyDB.putString("id_usuario", String.valueOf(usuarioLogado.getId()));
			Intent intent = new Intent(getApplicationContext(), MenuBottomNavigation.class);
			startActivity(intent);
			finish();

		} else if (event.getEventName().equals("Login" + Constants.eventErrorLabel)) {
			Snackbar snackbar = Snackbar.make(loginButton, "Erro ao realizar login", Snackbar.LENGTH_LONG);
			snackbar.getView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
			snackbar.show();
		}

//        loadingProgressBar.setVisibility(View.GONE);
		lottieLoading.setVisibility(View.GONE);
		usuarioTextInputLayout.setVisibility(View.VISIBLE);
		senhaTextInputLayout.setVisibility(View.VISIBLE);
		cadastrarButton.setVisibility(View.VISIBLE);
		loginButton.setVisibility(View.VISIBLE);
		loginButton.setEnabled(true);
		calendarLottie.setVisibility(View.VISIBLE);
	}

	public void showToast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	private void closeKeyBoard() {
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager)
					getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	@Override
	public void onBackPressed() {
		if (millis == 0) {
			millis = System.currentTimeMillis();
		} else {
			long end = System.currentTimeMillis() - millis;
			if (end < 2000) {
				moveTaskToBack(true);
			} else {
				millis = System.currentTimeMillis();
			}
		}
	}
}

