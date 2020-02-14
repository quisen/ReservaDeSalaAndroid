package br.com.wises.reservadesalarodrigo.ui.sala;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import br.com.wises.reservadesalarodrigo.R;

public class SalasFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_salas, container, false);
	}

	public static SalasFragment newInstance() {
		return new SalasFragment();
	}
}