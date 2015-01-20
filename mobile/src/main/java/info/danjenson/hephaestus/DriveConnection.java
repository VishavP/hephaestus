package info.danjenson.hephaestus;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by danj on 1/18/15.
 */
public class DriveConnection {
    private static DriveConnection sDriveConnection;
    private static com.google.api.services.drive.Drive sDrive;
    private static Activity sActivity;
    private static final NetHttpTransport sNetHttpTransport = new NetHttpTransport();
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final int AUTH_REQUEST_CODE = 0;
    private static final int COMPLETE_AUTHORIZATION_REQUEST_CODE = 1;

    public static DriveConnection get(Activity activity) {
       if (sDriveConnection == null) {
           sDriveConnection = new DriveConnection(activity);
       }
       return sDriveConnection;
    }

    private DriveConnection(Activity activity) {
        sActivity = activity;
        initializeDrive();
    }

    public static void download(String drive_filename, File local_file) {
        if (sDrive == null) {
            Toast toast = Toast.makeText(sActivity, "Drive not yet initialized", Toast.LENGTH_SHORT);
            toast.show();
        }
        downloadAsync(drive_filename, local_file);
    }

    private void initializeDriveBlocking() {
        try {
            Account account =  AccountManager.get(sActivity).getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE)[0];
            String token = GoogleAuthUtil.getToken(sActivity, account.name, "oauth2:" + DriveScopes.DRIVE_READONLY);
            GoogleCredential credential = new GoogleCredential().setAccessToken(token);
            sDrive = new Drive.Builder(sNetHttpTransport, JSON_FACTORY, credential)
                    .setApplicationName(sActivity.getString(R.string.app_name))
                    .build();
        } catch (GooglePlayServicesAvailabilityException playEx) {
            Dialog alert = GooglePlayServicesUtil.getErrorDialog(
                    playEx.getConnectionStatusCode(),
                    sActivity,
                    AUTH_REQUEST_CODE);
            alert.show();
        } catch (UserRecoverableAuthException userAuthEx) {
            sActivity.startActivityForResult(userAuthEx.getIntent(), COMPLETE_AUTHORIZATION_REQUEST_CODE);
        } catch (IOException transientEx) {
            transientEx.printStackTrace();
        } catch (GoogleAuthException authEx) {
            authEx.printStackTrace();
        }
    }

    private void initializeDrive() {
        AsyncTask task = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                initializeDriveBlocking();
                return (Void) null;
            }
        };
        task.execute();
    }

    private static void downloadAsync(final String drive_filename, final File local_file) {
        AsyncTask task = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                List<com.google.api.services.drive.model.File> result = new ArrayList<com.google.api.services.drive.model.File>();
                Drive.Files.List request = null;
                try {
                    request = sDrive.files().list().setQ("title='" + drive_filename + "'");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                do {
                    try {
                        FileList files = request.execute();
                        result.addAll(files.getItems());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } while (request.getPageToken() != null &&
                         request.getPageToken().length() > 0);

                Collections.sort(result, new Comparator<com.google.api.services.drive.model.File>() {
                    @Override
                    public int compare(com.google.api.services.drive.model.File f1, com.google.api.services.drive.model.File f2) {
                        Long t1 = f1.getModifiedDate().getValue();
                        Long t2 = f2.getModifiedDate().getValue();
                        return t1.compareTo(t2);
                    }
                });
                com.google.api.services.drive.model.File mostRecent = result.get(result.size() - 1);
                try {
                    OutputStream out = new FileOutputStream(local_file);
                    MediaHttpDownloader downloader = new MediaHttpDownloader(sNetHttpTransport, sDrive.getRequestFactory().getInitializer());
                    downloader.setDirectDownloadEnabled(true);
                    downloader.download(new GenericUrl(mostRecent.getDownloadUrl()), out);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return (Void) null;
            }

            @Override
            protected void onPostExecute(final Void result) {
                ActionListFragment f = (ActionListFragment) sActivity.getFragmentManager().findFragmentByTag(ActionListActivity.TAG);
                f.update();
                Toast toast = Toast.makeText(sActivity, "Updated!", Toast.LENGTH_SHORT);
                toast.show();
            }
        };
        task.execute();
    }
}
