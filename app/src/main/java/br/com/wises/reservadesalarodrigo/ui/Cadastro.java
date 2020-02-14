package br.com.wises.reservadesalarodrigo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wises.reservadesalarodrigo.R;
import br.com.wises.reservadesalarodrigo.models.Event;
import br.com.wises.reservadesalarodrigo.models.Organizacao;
import br.com.wises.reservadesalarodrigo.network.HttpRequest;
import br.com.wises.reservadesalarodrigo.utils.Constants;

public class Cadastro extends AppCompatActivity {
	MaterialButton cadastrarButton;
	TextInputLayout nomeTextInput, emailTextInput, senhaTextInput;
	Spinner empresasSpinner;
	List<String> nomesOrganizacoes = new ArrayList<>();
	String nome, emailCompleto, senha;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro);

		cadastrarButton = findViewById(R.id.bt_cadastrar);
		empresasSpinner = findViewById(R.id.sp_empresas);
		nomeTextInput = findViewById(R.id.et_nome);
		senhaTextInput = findViewById(R.id.et_senha);
		emailTextInput = findViewById(R.id.et_email);

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
					if(emailCompleto.contains("@")){
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
					nomeTextInput.setError("Digite um nome");
				} else if (emailCompleto.isEmpty()) {
					emailTextInput.setError("Digite um email");
					nomeTextInput.setErrorEnabled(false);
				} else if (senha.isEmpty()) {
					emailTextInput.setErrorEnabled(false);
					senhaTextInput.setError("Digite uma senha");
				} else {
					nomeTextInput.setErrorEnabled(false);
					emailTextInput.setErrorEnabled(false);
					senhaTextInput.setErrorEnabled(false);
				}
			}
		});

		empresasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				empSelected=lista.get(position).getId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
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

	@Subscribe
	public void customEventReceived(Event event) {
		if (event.getEventName().equals("OrganizacoesByDominio" + Constants.eventSuccessLabel)) {

			// parsear event.getEventMsg()
			String organizacoesJson = event.getEventMsg();
			System.out.println(organizacoesJson);
			Gson gson = new Gson();
			List<Organizacao> organizacoes = new ArrayList<>();
			if(nomesOrganizacoes.isEmpty()){
				nomesOrganizacoes.clear();
				organizacoes = gson.fromJson(organizacoesJson, new TypeToken<List<Organizacao>>(){}.getType());
				for(int i=0; i<organizacoes.size(); i++){
					nomesOrganizacoes.add(organizacoes.get(i).getNome());
				}
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<>(Cadastro.this, android.R.layout.simple_spinner_item, nomesOrganizacoes);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			empresasSpinner.setAdapter(adapter);
			empresasSpinner.setVisibility(View.VISIBLE);
		}
		else if(event.getEventName().equals("Login" + Constants.eventErrorLabel)){

		}
	}
}
