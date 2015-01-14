package info.danjenson.hephaestus;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by danj on 1/13/15.
 */
public class AsyncPostRequest extends AsyncTask<HttpPost, Void, String> {
    private static final HttpClient mHttpClient = new DefaultHttpClient();
    private static Context mAppContext;

    public AsyncPostRequest(Context appContext) {
        mAppContext = appContext;
    }

    @Override
    protected String doInBackground(HttpPost... httpPosts) {
        for (HttpPost httpPost : httpPosts) {
            try {
                HttpResponse httpResponse = mHttpClient.execute(httpPost);
                HttpEntity resEntity = httpResponse.getEntity();
                String res = EntityUtils.toString(resEntity);
                return res;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected void onPostExecute(String result) {
        Toast toast = Toast.makeText(mAppContext, result, Toast.LENGTH_SHORT);
        toast.show();
    }

}
