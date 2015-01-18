package info.danjenson.hephaestus;

import android.util.Log;

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
        super.onCreate();
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
        Action a = sActionServerProxyManager.getAction(message);
        if (a != null) {
            // TODO: fix this so user can resolve from wear
            AsyncPostRequest.sendPostRequest(this, a.getAllowedHosts().get(0), a.getName());
        }
    }

    @Override
    public void onDestroy() {
        sActionServerProxyManager = null;
    }
}
