package info.danjenson.hephaestus;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.UnsupportedEncodingException;

/**
 * Created by danj on 1/15/15.
 */
public class VoiceActionService extends WearableListenerService {
    private static ActionServerProxyManager sActionServerProxyManager;

    @Override
    public void onCreate() {
        sActionServerProxyManager = ActionServerProxyManager.get(this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String message = null;
        try {
            message = new String(messageEvent.getData(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // TODO: fix by sending message back if need be to resolve
        String hostname = "Daniels-Mac-mini";
        AsyncPostRequest.sendPostRequest(hostname, message);
    }
}
