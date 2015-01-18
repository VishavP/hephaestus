package info.danjenson.hephaestus;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.api.client.util.StringUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by danj on 1/13/15.
 */
public class AsyncPostRequest extends AsyncTask<HttpPost, Void, String> {
    private static final HttpClient mHttpClient = new DefaultHttpClient();
    private static Context mAppContext;

    public AsyncPostRequest(Context appContext) {
        mAppContext = appContext;
    }

    public static void sendPostRequest(Context appContext, final String hostname, final String actionName) {
        AsyncPostRequest request = new AsyncPostRequest(appContext);
        ActionServerProxy asp = ActionServerProxyManager.get(mAppContext).getActionServerProxy(hostname);
        String currentSSID = getCurrentSSID(appContext);
        String destinationIpAddress = null;
        if (asp.getLocalNetworkSSID().equals(currentSSID)) {
           destinationIpAddress = asp.getLocalIpAddress();
        } else {
            destinationIpAddress = asp.getRemoteIpAddress();
        }
        try {
            HttpPost httpPost = new HttpPost("http://" + destinationIpAddress + ':' + asp.getPort());
            String s = "<?xml version='1.0'?><methodCall><methodName>" + actionName + "</methodName></methodCall>";
            StringEntity se = new StringEntity(s, "UTF-8");
            se.setContentType("application/xml");
            httpPost.setEntity(se);
            new AsyncPostRequest(mAppContext).execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected static String getCurrentSSID(Context context) {
        String ssid = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null && wifiInfo.getSSID() != null)  {
                String quotedSSID = wifiInfo.getSSID();
                ssid = quotedSSID.substring(1, quotedSSID.length() - 1);
            }
        }
        return ssid;
    }

    @Override
    protected String doInBackground(HttpPost... httpPosts) {
        for (HttpPost httpPost : httpPosts) {
            try {
                HttpResponse httpResponse = mHttpClient.execute(httpPost);
                HttpEntity resEntity = httpResponse.getEntity();
                return EntityUtils.toString(resEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Toast toast = Toast.makeText(mAppContext, result, Toast.LENGTH_SHORT);
        toast.show();
    }

}
