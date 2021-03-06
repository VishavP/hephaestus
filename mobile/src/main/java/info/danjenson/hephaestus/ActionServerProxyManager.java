package info.danjenson.hephaestus;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by danj on 1/12/15.
 */
public class ActionServerProxyManager {
    private ArrayList<Action> mActions;
    private ArrayList<ActionServerProxy> mActionServerProxies;
    private Context mAppContext;
    private static ActionServerProxyManager sActionServerProxyManager;
    private static File sRpcConfigFile;
    public static final String RPC_CONFIG_FILENAME = "rpc_config.json";

    private ActionServerProxyManager(Context appContext) {
        mAppContext = appContext;
        sRpcConfigFile = new File(appContext.getExternalFilesDir(null), RPC_CONFIG_FILENAME);
        JSONObject jObject = getConfig(appContext);
        initializeActions(jObject);
        initializeActionServerProxies(jObject);
    }

    private static JSONObject getConfig(Context appContext) {
        JSONObject jObject = null;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(sRpcConfigFile));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
                jObject = new JSONObject(text.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
            String localNetworkSSID;
            String localGatewayIPAddress;
            JSONObject details;
            String remoteIpAddress;
            String localIpAddress;
            int port;
            String macAddress;
            try {
                hostname = (String) hosts.next();
                details = jHosts.getJSONObject(hostname);
                localNetworkSSID = details.getString("local_network_ssid");
                localGatewayIPAddress = details.getString("local_gateway_ip_address");
                remoteIpAddress = details.getString("remote_ip_address");
                localIpAddress = details.getString("local_ip_address");
                port = details.getInt("port");
                macAddress = details.getString("mac_address");
                ActionServerProxy asp = new ActionServerProxy(hostname,
                                                              localNetworkSSID,
                                                              localGatewayIPAddress,
                                                              remoteIpAddress,
                                                              localIpAddress,
                                                              port,
                                                              macAddress);
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

    public static void update(Context c) {
        sActionServerProxyManager = new ActionServerProxyManager(c.getApplicationContext());
    }

    public File getRpcConfigFile() {
        return sRpcConfigFile;
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

    public Action getAction(String name) {
       for (Action a : mActions) {
           if (a.getName().equals(name)) {
               return a;
           }
       }
       return null;
    }

    public ArrayList<ActionServerProxy> getActionServerProxies () {
        return mActionServerProxies;
    }

    public ActionServerProxy getActionServerProxy(String hostname) {
       for (ActionServerProxy asp : mActionServerProxies) {
           if (asp.getHostName().equals(hostname)) {
               return asp;
           }
       }
       return null;
    }

}
