package com.hunterdev.premier;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
	private static final String PREMIER_LOGIN_DATA = "premier_login_data";
	private FloatingActionButton m_fab;
	private View m_loginView;
	private NavigationView m_navigationView;
	private Button m_trackButton;

	private String m_userName;
	private String m_userEmail;
	private String m_supersEmail; // Use this if user chooses to 'Share'
	private boolean m_userLoggedIn;
	private boolean m_keepUserLoggedIn;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		String loginPref = sharedPrefs.getString(getString(R.string.pref_login_key), getString(R.string.pref_login_default));
		m_keepUserLoggedIn = (loginPref == getString(R.string.pref_login_default)) ? true : false;

		m_fab = (FloatingActionButton) findViewById(R.id.fab);
		m_fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				userLogin(view);
			}
		});

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar,
			R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		m_navigationView = (NavigationView) findViewById(R.id.nav_view);
		m_navigationView.setNavigationItemSelectedListener(this);

		setUserLoggedIn(checkUserLoginStatus());
		int m_navLoginTitleId = isUserLoggedIn() ? R.string.title_activity_logout : R.string.title_activity_login;

		m_trackButton = (Button) findViewById(R.id.track_button);
		m_trackButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent trackIntent = new Intent(MainActivity.this, RiskActivity.class);
				trackIntent.putExtra("userName", m_userName);
				trackIntent.putExtra("userEmail", m_userEmail);
				startActivity(trackIntent);
			}
		});

		if (isUserLoggedIn())
		{
			m_fab.setVisibility(View.GONE);
			m_trackButton.setVisibility(View.VISIBLE);
			setNavigationTitleId(m_navLoginTitleId);
			//setLoginInfoInMenuDrawer();
		}
	}

	private void setNavigationTitleId(int titleId)
	{
		m_navigationView.getMenu().getItem(0).setTitle(titleId);
	}

	public boolean isUserLoggedIn()
	{
		return m_userLoggedIn;
	}

	private void setUserLoggedIn(boolean userLoggedIn)
	{
		m_userLoggedIn = userLoggedIn;
	}

	private boolean checkUserLoginStatus()
	{
		SharedPreferences prefs = getSharedPreferences(PREMIER_LOGIN_DATA, MODE_PRIVATE);
		String restoredText = prefs.getString("name", null);
		if (restoredText != null)
		{
			m_userName = prefs.getString("name", null);
			m_userEmail = prefs.getString("email", null);
			m_supersEmail = prefs.getString("supers_email", null);
			return true;
		}
		return false;
	}

	private void userLogin(View view)
	{
		Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
		int loginRequestCode = 1;
		startActivityForResult(loginIntent, loginRequestCode);
		m_loginView = view;
	}

	@Override
	public void onBackPressed()
	{
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{ drawer.closeDrawer(GravityCompat.START); }
		else
		{ super.onBackPressed(); }
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item)
	{
		// Handle navigation view item clicks here.
		switch (item.getItemId())
		{
			case R.id.nav_login:
				if (isUserLoggedIn())
				{
					showLogOutConfirmation();
				}
				else
				{
					m_fab.callOnClick();
				}
				break;
			case R.id.nav_settings:
				startActivity(new Intent(this, SettingsActivity.class));
				break;
			default:
				// Invalid id returned
				break;
		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	private void showLogOutConfirmation()
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				AlertDialog.Builder logoutDialog = new AlertDialog.Builder(MainActivity.this);
				logoutDialog.setTitle(R.string.title_activity_logout)
					.setCancelable(false)
					.setIcon(R.drawable.ic_account_circle_black_24dp)
					.setMessage(R.string.logout_confirmation_text)
					.setPositiveButton(R.string.yes_text, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialogInterface, int i)
						{
							userLogout();
						}
					})
					.setNegativeButton(R.string.no_text, null)
					.show();
			}
		});
	}

	private void userLogout()
	{
		// Erase the user info from the shared prefs...
		SharedPreferences.Editor editor = getSharedPreferences(PREMIER_LOGIN_DATA, MODE_PRIVATE).edit();
		editor.remove("name");
		editor.remove("email");
		editor.remove("supers_email");
		editor.apply();

		setUserLoggedIn(false);
		setNavigationTitleId(R.string.title_activity_login);
		m_fab.setVisibility(View.VISIBLE);
		m_trackButton.setVisibility(View.GONE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && data != null)
		{
			if (m_loginView != null)
			{
				Snackbar.make(m_loginView, "Login Successful", Snackbar.LENGTH_LONG).setAction("Action", null).show();
				m_fab.setVisibility(View.GONE);
				m_trackButton.setVisibility(View.VISIBLE);

				m_userName = data.getExtras().getString("user", "Unknown User");
				m_userEmail = data.getExtras().getString("email", "Unknown Email");
				m_supersEmail = data.getExtras().getString("supers_email", "");

				saveLoginData();
				setNavigationTitleId(R.string.title_activity_logout);
			}
		}
	}

	@Override
	protected void onDestroy()
	{
		if (!m_keepUserLoggedIn)
		{
			userLogout();
		}
		super.onDestroy();
	}

	private void saveLoginData()
	{
		setLoginInfoInMenuDrawer();

		// Save the user info in the shared prefs for future logins
		SharedPreferences.Editor editor = getSharedPreferences(PREMIER_LOGIN_DATA, MODE_PRIVATE).edit();
		editor.putString("name", m_userName);
		editor.putString("email", m_userEmail);
		editor.putString("supers_email", m_supersEmail);
		editor.apply();

		setUserLoggedIn(true);
	}

	private void setLoginInfoInMenuDrawer()
	{
		View view = this.getCurrentFocus();
		TextView loginName = view.findViewById(R.id.premier_user_name);
		if (loginName != null)
		{ loginName.setText(m_userName); }

		TextView loginEmail = view.findViewById(R.id.premier_user_email);
		if (loginEmail != null)
		{ loginEmail.setText(m_userEmail); }
	}
}
