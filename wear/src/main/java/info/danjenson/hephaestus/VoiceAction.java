package info.danjenson.hephaestus;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class VoiceAction extends Activity {
    private static final int SPEECH_REQUEST_CODE = 0;
    private static final List<String> exitWords = Arrays.asList("close", "quit", "exit");
    private static GoogleApiClient mGoogleApiClient;
    private HashSet<String> mConnectedNodeIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
        mConnectedNodeIds = new HashSet<String>();
        new NodeManager().execute();
        startVoiceAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exit();
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
                exit();
            } else if (mConnectedNodeIds.size() > 0) {
                new MessageSender().execute(spokenText);
                try {
                    Thread.sleep(1000);
                    startVoiceAction();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Toast toast = Toast.makeText(this, "No connected nodes!", Toast.LENGTH_SHORT);
                toast.show();
                exit();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class NodeManager extends AsyncTask<Void, Void, HashSet<String>> {

        @Override
        protected HashSet<String> doInBackground(Void... voids) {
            HashSet<String> results = new HashSet<String>();
            NodeApi.GetConnectedNodesResult nodes =
                    Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
            for (Node node : nodes.getNodes()) {
                results.add(node.getId());
            }
            return results;
        }

        @Override
        protected void onPostExecute(HashSet<String> nodeIds) {
            mConnectedNodeIds = nodeIds;
        }
    }

    private class MessageSender extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String...messages) {
            for (String message : messages) {
                for (String nodeId : mConnectedNodeIds) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mGoogleApiClient, nodeId, "/wear", message.getBytes()).await();
                    if (!result.getStatus().isSuccess()) {
                        Log.e("ERROR", "Failed to send message!");
                    } else {
                        return message;
                    }
                }
            }
        return "Failed!";
        }

        @Override
        protected void onPostExecute(String message) {
            Toast toast = Toast.makeText(VoiceAction.this, message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void exit() {
        mGoogleApiClient = null;
        setResult(RESULT_OK);
        finish();
    }
}
