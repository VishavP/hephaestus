package info.danjenson.hephaestus;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by danj on 1/12/15.
 */
public class ActionServerProxyManager {
    private ArrayList<Action> mActions;
    private ArrayList<ActionServerProxy> mActionServerProxies;
    private static ActionServerProxyManager sActionServerProxyManager;
    private Context mAppContext;
    private static final String mRpcConfig = "rpc_config.json";

    private ActionServerProxyManager(Context appContext) {
        mAppContext = appContext;
        JSONObject jObject = getConfig(appContext);
        initializeActions(jObject);
        initializeActionServerProxies(jObject);
    }

    private static JSONObject getConfig(Context appContext) {
        JSONObject jObject = null;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File config = new File(appContext.getExternalFilesDir(null), mRpcConfig);
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(config));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
                jObject = new JSONObject(text.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jObject;
    }

    private void initializeActions(JSONObject jObject) {
        mActions = new ArrayList<Action>();
        JSONObject jActions = null;
        try {
            jActions = jObject.getJSONObject("actions");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Iterator<String> actions = jActions.keys();
        while (actions.hasNext()) {
            Action action;
            String name;
            JSONObject details;
            String command;
            JSONArray jAllowedHosts;
            ArrayList<String> allowedHosts;
            try {
                name = (String) actions.next();
                details = jActions.getJSONObject(name);
                command = details.getString("command");
                jAllowedHosts = details.getJSONArray("allowed_hosts");
                allowedHosts = new ArrayList<String>();
                for (int i = 0; i < jAllowedHosts.length(); i++) {
                   allowedHosts.add(jAllowedHosts.getString(i));
                }
                action = new Action(name, command, allowedHosts);
                mActions.add(action);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeActionServerProxies(JSONObject jObject) {
        mActionServerProxies = new ArrayList<ActionServerProxy>();
        JSONObject jHosts = null;
        try {
            jHosts = jObject.getJSONObject("hosts");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Iterator<String> hosts = jHosts.keys();
        while (hosts.hasNext()) {
            String hostname;
            JSONObject details;
            String remoteIpAddress;
            String localIpAddress;
            int port;
            String macAddress;
            try {
                hostname = (String) hosts.next();
                details = jHosts.getJSONObject(hostname);
                remoteIpAddress = details.getString("remote_ip_address");
                localIpAddress = details.getString("local_ip_address");
                port = details.getInt("port");
                macAddress = details.getString("mac_address");
                ActionServerProxy asp = new ActionServerProxy(hostname, remoteIpAddress,
                        localIpAddress, port, macAddress);
                mActionServerProxies.add(asp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
