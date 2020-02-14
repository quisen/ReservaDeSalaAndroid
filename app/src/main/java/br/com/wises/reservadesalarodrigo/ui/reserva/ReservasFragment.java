package br.com.wises.reservadesalarodrigo.ui.reserva;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import br.com.wises.reservadesalarodrigo.R;

public class ReservasFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_reservas, container, false);
	}

	public static ReservasFragment newInstance() {
		return new ReservasFragment();
	}
}