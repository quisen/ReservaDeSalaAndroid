package br.com.wises.reservadesalarodrigo.ui.configuracao;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import br.com.wises.reservadesalarodrigo.ui.MainActivity;
import br.com.wises.reservadesalarodrigo.R;

public class ConfiguracoesFragment extends Fragment {
	MaterialButton logoutButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_configuracoes, container, false);
		logoutButton = v.findViewById(R.id.logout);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), MainActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
		return v;
	}

	public static ConfiguracoesFragment newInstance() {
		return new ConfiguracoesFragment();
	}
}