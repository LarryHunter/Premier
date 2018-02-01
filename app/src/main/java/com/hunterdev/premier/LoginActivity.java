package com.hunterdev.premier;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity
{
	private Button m_loginButton;
	private EditText m_userName;
	private EditText m_userEmail;
	private EditText m_supersEmail;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		m_userName = findViewById(R.id.userName);
		m_userEmail = findViewById(R.id.userEmail);
		m_supersEmail = findViewById(R.id.supervisorEmail);
		m_loginButton = findViewById(R.id.loginButton);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		m_loginButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (m_userName.length() == 0 || m_userEmail.length() == 0)
				{
					showErrorDialog();
					return;
				}

				Intent data = new Intent();
				data.putExtra("user", m_userName.getText().toString());
				data.putExtra("email", m_userEmail.getText().toString());
				data.putExtra("supers_email", m_supersEmail.getText().toString());
				setResult(RESULT_OK, data);
				hideKeyboard(view);
				finish();
			}

			private void showErrorDialog()
			{
				AlertDialog.Builder logoutDialog = new AlertDialog.Builder(LoginActivity.this);
				logoutDialog.setTitle(R.string.title_login_error)
					.setCancelable(false)
					.setIcon(R.drawable.ic_account_circle_black_24dp)
					.setMessage(R.string.login_error_text)
					.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialogInterface, int i)
						{
							// Do nothing the user needs to continue
						}
					})
					.show();
			}

			private void hideKeyboard(View view)
			{
				if (view != null)
				{
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
			}
		});
	}
}
