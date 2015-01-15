package info.danjenson.hephaestus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class VoiceAction extends Activity {
    private static final int SPEECH_REQUEST_CODE = 0;
    private static final List<String> exitWords = Arrays.asList("close", "quit", "exit");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startVoiceAction();
    }

    private void startVoiceAction() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            if (exitWords.contains(spokenText)) {
                setResult(RESULT_OK);
                finish();
            } else {
                Toast toast = Toast.makeText(this, spokenText, Toast.LENGTH_SHORT);
                toast.show();
                startVoiceAction();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
