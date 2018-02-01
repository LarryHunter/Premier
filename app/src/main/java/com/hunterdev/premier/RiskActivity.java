package com.hunterdev.premier;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toolbar;

public class RiskActivity extends Activity
{
	//private static final String LOG_TAG = RiskActivity.class.getSimpleName();
	private String m_userName;
	private String m_userEmail;
	private RadioGroup m_section1radioGroup1;
	private RadioGroup m_section1radioGroup2;
	private RadioGroup m_section1radioGroup3;
	private RadioGroup m_section2radioGroup1;
	private RadioGroup m_section2radioGroup2;
	private RadioGroup m_section2radioGroup3;
	private RadioGroup m_section3radioGroup1;
	private RadioGroup m_section3radioGroup2;
	private RadioGroup m_section3radioGroup3;
	private RadioGroup m_section4radioGroup1;
	private RadioGroup m_section4radioGroup2;
	private RadioGroup m_section4radioGroup3;
	private RadioGroup m_section5radioGroup1;
	private RadioGroup m_section5radioGroup2;
	private RadioGroup m_section5radioGroup3;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_risk);

		Toolbar toolbar = findViewById(R.id.risk_activity_toolbar);
		toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
		toolbar.setTitle(R.string.app_name);
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				onBackPressed();
			}
		});

		// Section 1
		m_section1radioGroup1 = findViewById(R.id.section1_question1_options);
		m_section1radioGroup2 = findViewById(R.id.section1_question2_options);
		m_section1radioGroup3 = findViewById(R.id.section1_question3_options);

		// Section 2
		m_section2radioGroup1 = findViewById(R.id.section2_question1_options);
		m_section2radioGroup2 = findViewById(R.id.section2_question2_options);
		m_section2radioGroup3 = findViewById(R.id.section2_question3_options);

		// Section 3
		m_section3radioGroup1 = findViewById(R.id.section3_question1_options);
		m_section3radioGroup2 = findViewById(R.id.section3_question2_options);
		m_section3radioGroup3 = findViewById(R.id.section3_question3_options);

		// Section 4
		m_section4radioGroup1 = findViewById(R.id.section4_question1_options);
		m_section4radioGroup2 = findViewById(R.id.section4_question2_options);
		m_section4radioGroup3 = findViewById(R.id.section4_question3_options);

		// Section 5
		m_section5radioGroup1 = findViewById(R.id.section5_question1_options);
		m_section5radioGroup2 = findViewById(R.id.section5_question2_options);
		m_section5radioGroup3 = findViewById(R.id.section5_question3_options);

		m_userName = getIntent().getStringExtra("userName") != null ?
			getIntent().getStringExtra("userName") : "";

		m_userEmail = getIntent().getStringExtra("userEmail") != null ?
			getIntent().getStringExtra("userEmail") : "";
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		Button emailHazardButton = findViewById(R.id.email_button);
		emailHazardButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				createEmail();
			}
		});
	}

	public void createEmail()
	{
		if (!allQuestionsAnswered())
		{
			showErrorDialog();
		}
		else
		{
			Intent email = EmailBuilder.createEmailIntent(getApplicationContext(), m_userEmail, m_userName);
			startActivity(Intent.createChooser(email, getString(R.string.choose_an_email_client)));
		}
	}

	private void showErrorDialog()
	{
		AlertDialog.Builder logoutDialog = new AlertDialog.Builder(RiskActivity.this);
		logoutDialog.setTitle(R.string.title_email_error)
			.setCancelable(false)
			.setIcon(R.drawable.ic_email_black_24dp)
			.setMessage(R.string.email_error_text)
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

	private boolean allQuestionsAnswered()
	{
		// Verify all questions have been answered...
		final int totalNumberOfQuestions = 15;
		int numberOfQuestiosAnswered = 0;

		// Section 1
		if (m_section1radioGroup1.getCheckedRadioButtonId() != -1) { numberOfQuestiosAnswered++; }
		if (m_section1radioGroup2.getCheckedRadioButtonId() != -1) { numberOfQuestiosAnswered++; }
		if (m_section1radioGroup3.getCheckedRadioButtonId() != -1) { numberOfQuestiosAnswered++; }

		// Section 2
		if (m_section2radioGroup1.getCheckedRadioButtonId() != -1) { numberOfQuestiosAnswered++; }
		if (m_section2radioGroup2.getCheckedRadioButtonId() != -1) { numberOfQuestiosAnswered++; }
		if (m_section2radioGroup3.getCheckedRadioButtonId() != -1) { numberOfQuestiosAnswered++; }

		// Section 3
		if (m_section3radioGroup1.getCheckedRadioButtonId() != -1) { numberOfQuestiosAnswered++; }
		if (m_section3radioGroup2.getCheckedRadioButtonId() != -1) { numberOfQuestiosAnswered++; }
		if (m_section3radioGroup3.getCheckedRadioButtonId() != -1) { numberOfQuestiosAnswered++; }

		// Section 4
		if (m_section4radioGroup1.getCheckedRadioButtonId() != -1) { numberOfQuestiosAnswered++; }
		if (m_section4radioGroup2.getCheckedRadioButtonId() != -1) { numberOfQuestiosAnswered++; }
		if (m_section4radioGroup3.getCheckedRadioButtonId() != -1) { numberOfQuestiosAnswered++; }

		// Section 5
		if (m_section5radioGroup1.getCheckedRadioButtonId() != -1) { numberOfQuestiosAnswered++; }
		if (m_section5radioGroup2.getCheckedRadioButtonId() != -1) { numberOfQuestiosAnswered++; }
		if (m_section5radioGroup3.getCheckedRadioButtonId() != -1) { numberOfQuestiosAnswered++; }

		return (numberOfQuestiosAnswered == totalNumberOfQuestions);
	}
}
