package com.hunterdev.premier;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EmailBuilder
{
    public static Intent createEmailIntent(Context context, String userEmail, String userName)
    {
        Resources res = context.getResources();
	    String date;
        if (DateFormat.is24HourFormat(context))
        { date = new SimpleDateFormat("MMM dd, yyyy H:mm", Locale.getDefault()).format(new Date()); }
        else
        { date = new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault()).format(new Date()); }

        String subject = res.getString(R.string.email_message_subject);

        String message = "";
        message += "***" + userName + " is reporting the following hazard ***";
        message += "\n";
        message += "\n";
        message += res.getString(R.string.message_text_1);
        message += "\n";
        message += "\n";
        message += res.getString(R.string.message_text_2);
        message += "\n";
        message += "\n";
        message += res.getString(R.string.message_text_3);
        message += "\n";
        message += "\n";
	    message += res.getString(R.string.message_text_4);
	    message += "\n";
	    message += "\n";
	    message += res.getString(R.string.message_text_5);
	    message += "\n";
	    message += "\n";
        message += "\n";
        message += userName;
        message += "\n";
        message += userEmail;
        message += "\n";
        message += res.getString(R.string.message_text_time) + ": " + date;
        message += "\n";
        message += "\n";
        message += "\n";


        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.putExtra(Intent.EXTRA_EMAIL, new String[] { userEmail });
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, message);
        email.setType("message/rfc822");
	    email.setData(Uri.parse("mailto:"));

        return email;
    }
}
