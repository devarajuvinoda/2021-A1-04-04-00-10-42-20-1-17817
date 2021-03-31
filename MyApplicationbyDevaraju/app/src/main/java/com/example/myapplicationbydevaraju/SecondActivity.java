package com.example.myapplicationbydevaraju;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SecondActivity extends AppCompatActivity {

    String EMAIL_VALID = "^\\s*[a-zA-Z0-9_+&*-]+(\\.[a-zA-Z0-9_+&*-]+)*@([a-zA-Z0-9]+\\.)+[a-zA-Z]{2,7}\\s*$";

    String PHONE_VALID = "^\\s*(\\+?(\\d{1,3}))?(([(]?(\\d{3})[)]?)|([.-]?(\\d{3})[.-]?))(\\d{3})[-.]?(\\d{4})\\s*$";

    private static final String LOG_TAG = SecondActivity.class.getSimpleName();
    EditText email_phone;
    Button confirm_but;
    String msgStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // creating reference to views for later usage
        email_phone = (EditText) findViewById(R.id.text_phone_email);
        confirm_but = (Button) findViewById(R.id.button_confirm);

        // get the message passed along with intent from first activity
        Intent intent  = getIntent();
        msgStr = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

    }
    // capture back-arrow press from options menu
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean validEmail(String emailId){
        //define search pattern for email ID
        Pattern pat = Pattern.compile(EMAIL_VALID);
        if (emailId == null)
            return false;
        //search & match the pattern and return the result
        return pat.matcher(emailId).matches();
    }

    public boolean validPhoneNumber(String phoneNumber){
        //define search pattern for phone number
        Pattern pat = Pattern.compile(PHONE_VALID);
        if (phoneNumber == null)
            return false;
        //search & match the pattern and return the result
        return pat.matcher(phoneNumber).matches();
    }

    public void validatePhoneEmail(View view) {
        String message = email_phone.getText().toString();

        if(validEmail(message)){
            // create an intent to call email client
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            // add receiver of the mail and the content of the email
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{message});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "default subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, msgStr);

            // create and instance of toast and show toast message
            Toast toast = Toast.makeText(getApplicationContext(), "Sending email ...", Toast.LENGTH_SHORT);
            toast.show();

            // start a new activity which opens sharesheet to send text from this app to email app
            startActivity(Intent.createChooser(emailIntent, "Email client :"));
        }
        else if(validPhoneNumber(message)) {
            // create an intent to call SMS client
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO);

            // add receiver of the SMS and the content
            String smsNumber = String.format("smsto:%s", message);
            smsIntent.setData(Uri.parse(smsNumber));
            smsIntent.putExtra("sms_body", msgStr);

            // create and instance of toast and show toast message
            Toast toast = Toast.makeText(getApplicationContext(), "Sending SMS ...", Toast.LENGTH_SHORT);
            toast.show();

            // start a new activity which opens sharesheet to send text from this app to SMS app
            startActivity(Intent.createChooser(smsIntent, "SMS client :"));
        }
        else{
//            email_phone.setText("Enter a valid phone number or email address");
            email_phone.setHint("Enter a valid phone number or email address");
            email_phone.setText("");
        }
    }

}