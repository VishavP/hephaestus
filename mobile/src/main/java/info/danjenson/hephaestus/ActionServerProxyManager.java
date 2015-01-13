package info.danjenson.hephaestus;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by danj on 1/12/15.
 */
public class ActionServerProxyManager {
    private ArrayList<Action> mActions;
    private ArrayList<ActionServerProxy> mActionServerProxies;
    private static ActionServerProxyManager sActionServerProxyManager;
    private Context mAppcontext;
    private static final String mRpcConfig = "rpc_config.json";

    private ActionServerProxyManager(Context appContext) {
        mAppcontext = appContext;
        FileOutputStream outputStream;
        try {
            outputStream = mAppcontext.openFileOutput(mRpcConfig, Context.MODE_WORLD_WRITEABLE);
            outputStream.write("test".getBytes());
            outputStream.close();
            Log.d("WRITE", "FILE WRITTEN!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mActionServerProxies = new ArrayList<ActionServerProxy>();
        for(int i = 0; i < 2; i++) {
            mActionServerProxies.add(new ActionServerProxy("name " + i, "ipR", "ipL", 5, "mac"));
        }
        mActions = new ArrayList<Action>();
        for(int j = 0; j < 50; j++) {
            mActions.add(new Action("Action " + j, "Command " + j, new ArrayList<String>(Arrays.asList("mini", "pi"))));
        }
    }

    public static ActionServerProxyManager get(Context c) {
       if (sActionServerProxyManager == null) {
           sActionServerProxyManager = new ActionServerProxyManager(c.getApplicationContext());
       }
       return sActionServerProxyManager;
    }

    public ArrayList<Action> getActions() { return mActions; }
    public ActionServerProxy getActionProxyServerByHostname(String hostname) {
       for (ActionServerProxy asp : mActionServerProxies) {
            if (asp.getHostName().equals(hostname)) {
                return asp;
            }
        }
        return null;
    }
}
