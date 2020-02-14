package br.com.wises.reservadesalarodrigo.ui;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

import br.com.wises.reservadesalarodrigo.R;
import br.com.wises.reservadesalarodrigo.ui.configuracao.ConfiguracoesFragment;
import br.com.wises.reservadesalarodrigo.ui.reserva.ReservasFragment;
import br.com.wises.reservadesalarodrigo.ui.sala.SalasFragment;

public class MenuBottomNavigation extends AppCompatActivity {
	static long millis = 0L;
	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
			= new BottomNavigationView.OnNavigationItemSelectedListener() {

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			switch (item.getItemId()) {
				case R.id.navigation_reservas:
					Fragment reservasFragment = ReservasFragment.newInstance();
					openFragment(reservasFragment);
					break;
				case R.id.navigation_salas:
					Fragment salasFragment = SalasFragment.newInstance();
					openFragment(salasFragment);
					break;
				case R.id.navigation_configuracoes:
					Fragment configuracoesFragment = ConfiguracoesFragment.newInstance();
					openFragment(configuracoesFragment);
					break;
			}
			return true;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_bottom_navigation);
		BottomNavigationView navView = findViewById(R.id.nav_view);
		navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
		Fragment reservasFragment = ReservasFragment.newInstance();
		openFragment(reservasFragment);
	}

	private void openFragment(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.frameContainer, fragment);
//		transaction.addToBackStack(null);
		transaction.commit();
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
