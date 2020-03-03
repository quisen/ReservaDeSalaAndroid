package br.com.wises.reservadesalarodrigo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wises.reservadesalarodrigo.R;
import br.com.wises.reservadesalarodrigo.models.Event;
import br.com.wises.reservadesalarodrigo.models.Organizacao;
import br.com.wises.reservadesalarodrigo.models.Usuario;
import br.com.wises.reservadesalarodrigo.network.HttpRequest;
import br.com.wises.reservadesalarodrigo.utils.Constants;

public class Cadastro extends AppCompatActivity {
	MaterialButton cadastrarButton;
	TextInputLayout nomeTextInput, emailTextInput, senhaTextInput;
	Spinner empresasSpinner;
	LottieAnimationView lottieLoading;
	List<String> nomesOrganizacoes = new ArrayList<>();
	List<Organizacao> organizacoes = new ArrayList<>();
	String nome, emailCompleto, senha;
	Map<String, String> cadastroParams = new HashMap<String, String>();
	int idOrganizacao = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro);

		cadastrarButton = findViewById(R.id.bt_cadastrar);
		empresasSpinner = findViewById(R.id.sp_empresas);
		nomeTextInput = findViewById(R.id.et_nome);
		senhaTextInput = findViewById(R.id.et_senha);
		emailTextInput = findViewById(R.id.et_email);
		lottieLoading = findViewById(R.id.loadingLottie);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);


		emailTextInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
																  @Override
																  public void onFocusChange(View v, boolean hasFocus) {
																	  String dominio = "";
																	  if (!hasFocus && emailTextInput.getEditText().getTextSize() > 0) {
																		  emailTextInput.setErrorEnabled(false);
																		  // fazer request para puxar as organizacoes com o email digitado
																		  emailCompleto = emailTextInput.getEditText().getText().toString();
																		  if (emailCompleto.contains("@")) {
																			  dominio = emailCompleto.split("@")[1];
																			  Map<String, String> params = new HashMap<String, String>();
																			  params.put("authorization", "secret");
																			  params.put("dominio", dominio);
																			  String url = "http://172.30.248.56:8080/ReservaDeSala/rest/organizacao/organizacoesByDominio";
																			  new HttpRequest(
																					  getApplicationContext(),
																					  params,
																					  url,
																					  "GET", "OrganizacoesByDominio").doRequest();
																		  } else {
																			  emailTextInput.setError("Digite um email válido");
																		  }

																	  }
																  }
															  }
		);

		cadastrarButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nome = nomeTextInput.getEditText().getText().toString();
				emailCompleto = emailTextInput.getEditText().getText().toString();
				senha = senhaTextInput.getEditText().getText().toString();
				if (nome.isEmpty()) {
					nomeTextInput.setError("Digite um nome.");
				} else if (emailCompleto.isEmpty()) {
					emailTextInput.setError("Digite um email.");
					nomeTextInput.setErrorEnabled(false);
				} else if (senha.isEmpty()) {
					emailTextInput.setErrorEnabled(false);
					senhaTextInput.setError("Digite uma senha.");
				} else {
					nomeTextInput.setErrorEnabled(false);
					emailTextInput.setErrorEnabled(false);
					senhaTextInput.setErrorEnabled(false);

					Usuario novoUsuario = new Usuario();
					novoUsuario.setEmail(emailTextInput.getEditText().getText().toString().trim());
					novoUsuario.setNome(nomeTextInput.getEditText().getText().toString().trim());
					novoUsuario.setSenha(senhaTextInput.getEditText().getText().toString().trim());
					novoUsuario.setIdOrganizacao(idOrganizacao);

					Gson gson = new Gson();
					String usuarioJson = gson.toJson(novoUsuario);
					System.out.println("Novo usuário: " + usuarioJson);
					String novoUsuarioEncoded = null;
					try {
						novoUsuarioEncoded = new String(Base64.encodeToString(usuarioJson.getBytes("UTF-8"), Base64.NO_WRAP));
					} catch (Exception e) {
						e.printStackTrace();
					}
					Map<String, String> params = new HashMap<String, String>();
					params.put("authorization", "secret");
					params.put("novoUsuario", novoUsuarioEncoded);
					String url = "http://172.30.248.56:8080/ReservaDeSala/rest/usuario/cadastro";
					new HttpRequest(
							getApplicationContext(),
							params,
							url,
							"POST", "CadastrarUsuario").doRequest();
					closeKeyBoard();
					hideUIForms();

				}
			}
		});

		empresasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position > 0) {
					idOrganizacao = organizacoes.get(position - 1).getId();
					cadastrarButton.setEnabled(true);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				((TextView) empresasSpinner.getSelectedView()).setError("Selecione uma organização.");
			}
		});
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	public void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	public void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	private void hideUIForms(){
		cadastrarButton.setVisibility(View.GONE);
		empresasSpinner.setVisibility(View.GONE);
		nomeTextInput.setVisibility(View.GONE);
		senhaTextInput.setVisibility(View.GONE);
		emailTextInput.setVisibility(View.GONE);
		lottieLoading.setVisibility(View.VISIBLE);
	}

	private void showUIForms(){
		cadastrarButton.setVisibility(View.VISIBLE);
		empresasSpinner.setVisibility(View.VISIBLE);
		nomeTextInput.setVisibility(View.VISIBLE);
		senhaTextInput.setVisibility(View.VISIBLE);
		emailTextInput.setVisibility(View.VISIBLE);
		lottieLoading.setVisibility(View.GONE);
	}

	private void closeKeyBoard() {
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager)
					getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	@Subscribe
	public void customEventReceived(Event event) {
		// Tratamento de eventos relacionados a OrganizacoesByDominio
		if (event.getEventName().equals("OrganizacoesByDominio" + Constants.eventSuccessLabel)) {

			// parsear event.getEventMsg()
			String organizacoesJson = event.getEventMsg();
			System.out.println(organizacoesJson);
			Gson gson = new Gson();

			nomesOrganizacoes.clear();
			nomesOrganizacoes.add("- Selecione uma organização -");

			if (nomesOrganizacoes.size()>0) {
				organizacoes.clear();
				organizacoes = gson.fromJson(organizacoesJson, new TypeToken<List<Organizacao>>() {
				}.getType());

				if (organizacoes.size() == 1) {
					idOrganizacao = organizacoes.get(0).getId();
					cadastrarButton.setEnabled(true);
					return;
				} else {
					for (int i = 0; i < organizacoes.size(); i++) {
						nomesOrganizacoes.add(organizacoes.get(i).getNome());
					}
				}
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<>(Cadastro.this, android.R.layout.simple_spinner_item, nomesOrganizacoes);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			empresasSpinner.setAdapter(adapter);
			if (!organizacoes.isEmpty()) {
				empresasSpinner.setVisibility(View.VISIBLE);
				emailTextInput.setErrorEnabled(false);
			} else {
				emailTextInput.setError("Nenhuma empresa foi encontrada o domínio informado.");
				empresasSpinner.setVisibility(View.GONE);
				cadastrarButton.setEnabled(false);
			}

		} else if (event.getEventName().equals("OrganizacoesByDominio" + Constants.eventErrorLabel)) {
			emailTextInput.setError("Não foi possível verificar o domínio. Verifique sua conexão com a internet.");
			empresasSpinner.setVisibility(View.GONE);
			cadastrarButton.setEnabled(false);
		}

		// Tratamento de eventos relacionados a CadastrarUsuario
		if (event.getEventName().equals("CadastrarUsuario" + Constants.eventSuccessLabel)) {
			String response = event.getEventMsg();

			if(response.contains("O email informado já está cadastrado")){
				emailTextInput.setError("O email informado já está cadastrado.");
			}
			if(response.contains("Usuário criado com sucesso")){
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
			}
			showUIForms();
		} else if (event.getEventName().equals("CadastrarUsuario" + Constants.eventErrorLabel)) {
			String response = event.getEventMsg();
			System.out.println("CadastrarUsuario" + Constants.eventErrorLabel + " - " + response);
			showUIForms();
		}

	}
}
