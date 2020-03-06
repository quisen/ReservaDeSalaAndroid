package br.com.wises.reservadesalarodrigo.ui.reserva;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
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
import br.com.wises.reservadesalarodrigo.models.Reserva;
import br.com.wises.reservadesalarodrigo.network.HttpRequest;
import br.com.wises.reservadesalarodrigo.ui.MenuBottomNavigation;
import br.com.wises.reservadesalarodrigo.utils.Constants;
import br.com.wises.reservadesalarodrigo.utils.TinyDB;

public class ReservasFragment extends Fragment {

	List<Reserva> reservas = new ArrayList<>();
	TinyDB tinyDB;
	ListView reservasListView;
	ReservasAdapter adapter;
	ArrayList<Reserva> listaDeReservas = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		requestReservas();
		View view = inflater.inflate(R.layout.fragment_reservas, container, false);
		reservasListView = (ListView) view.findViewById(R.id.lv_reservas);
		adapter = new ReservasAdapter(getActivity().getApplicationContext(), listaDeReservas);
		reservasListView.setAdapter(adapter);
		return inflater.inflate(R.layout.fragment_reservas, container, false);
	}

	public static ReservasFragment newInstance() {
		return new ReservasFragment();
	}

	public void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	public void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	public void requestReservas() {
		tinyDB = new TinyDB(getActivity().getApplicationContext());
		String idOrganizacao = tinyDB.getString("id_organizacao");
		Map<String, String> params = new HashMap<String, String>();
		params.put("authorization", "secret");
		params.put("id_organizacao", idOrganizacao);
		String url = "reserva/byIdOrganizacao";
		new HttpRequest(
				getActivity().getApplicationContext(),
				params,
				url,
				"GET", "ListarReservas").doRequest();
	}

	@Subscribe
	public void customEventReceived(Event event) {
		if (event.getEventName().equals("ListarReservas" + Constants.eventSuccessLabel)) {

			String reservasJson = event.getEventMsg();
			System.out.println(reservasJson);
			Gson gson = new Gson();
			listaDeReservas = gson.fromJson(reservasJson, new TypeToken<List<Reserva>>() {}.getType());
			System.out.println("Tamanho da lista de reservas:" + listaDeReservas.size());
			adapter.addAll(listaDeReservas);


		} else if (event.getEventName().equals("ListarReservas" + Constants.eventErrorLabel)) {
//			Snackbar snackbar = Snackbar.make(loginButton, "Erro ao realizar login", Snackbar.LENGTH_LONG);
//			snackbar.getView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
//			snackbar.show();
		}

	}

	public void showToast(String msg) {
		Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
	}

}