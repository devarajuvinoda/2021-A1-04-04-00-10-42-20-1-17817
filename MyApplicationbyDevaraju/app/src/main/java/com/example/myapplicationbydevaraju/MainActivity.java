package com.example.myapplicationbydevaraju;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myapplicationbydevaraju.MESSAGE";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    EditText editText;
    int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // creating reference to views for later usage
        editText = (EditText) findViewById(R.id.edit_message);

        //restore edit text view's data
//        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
//        String restoredText = prefs.getString("text", null);
//        if(!TextUtils.isEmpty(restoredText)){
//            editText.setText(restoredText);
//        }
    }

    // following method will be invoked on click of send button
    public void sendMessage(View view){
        //create an intent to call second activity
        Intent intent = new Intent(this, SecondActivity.class);
        //pass user entered text from edit text field
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        //start second activity
        startActivity(intent);
    }

//    protected void onPause() {
//        super.onPause();
//        // store content of user entered text, keep it private within this app's scope
//        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
//        editor.putString("text", editText.getText().toString());
//        editor.commit();
//    }

    //  intent to start speech recognizer activity
    public void inputVoice(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    //  start activity and populate intent with speech text
        startActivityForResult(intent, REQUEST_CODE);
    }

    //  following call back invoked after speech recognizer returns
    @Override
    protected void onActivityResult(int requestCode, int resultcode, Intent intent) {
        super.onActivityResult(requestCode, resultcode, intent);
        ArrayList<String> speech;

        // process the speech text and populate editText field
        if (requestCode == REQUEST_CODE && resultcode == RESULT_OK) {
            speech = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String resultSpeech = "";
            resultSpeech = speech.get(0);
            editText.setText(resultSpeech);
        }
    }

}