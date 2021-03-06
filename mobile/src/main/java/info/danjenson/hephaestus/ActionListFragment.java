package info.danjenson.hephaestus;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by danj on 1/12/15.
 */
public class ActionListFragment extends ListFragment {
    private ActionServerProxyManager mActionServerProxyManager;
    private ArrayList<Action> mActions;
    private ActionAdapter mAdapter;
    private DriveConnection mDriveConnection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDriveConnection = DriveConnection.get(getActivity());
        mActionServerProxyManager = ActionServerProxyManager.get(getActivity());
        mActions = mActionServerProxyManager.getActions();
        mAdapter = new ActionAdapter(mActions);
        mAdapter.sort(new Comparator<Action>() {
            @Override
            public int compare(Action a1, Action a2) {
                return a1.compareTo(a2);
            }
        });
        setListAdapter(mAdapter);
    }

    public void update() {
        ActionServerProxyManager.update(getActivity());
        mActionServerProxyManager = ActionServerProxyManager.get(getActivity());
        mActions.clear();
        mActions.addAll(mActionServerProxyManager.getActions());
        mAdapter.notifyDataSetChanged();
        mAdapter.sort(new Comparator<Action>() {
            @Override
            public int compare(Action a1, Action a2) {
                return a1.compareTo(a2);
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final Action a = ((ActionAdapter) getListAdapter()).getItem(position);
        String hostname = null;
        if (a.getAllowedHosts().size() > 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Host");
            ArrayList<String> allowedHosts = a.getAllowedHosts();
            final String[] options = allowedHosts.toArray(new String[allowedHosts.size()]);
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AsyncPostRequest.sendPostRequest(getActivity(), options[which], a.getName());
                }
            });
            builder.show();
        } else {
            hostname = a.getAllowedHosts().get(0);
            AsyncPostRequest.sendPostRequest(getActivity(), hostname, a.getName());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
           case R.id.refresh_actions:
               ArrayList<ActionServerProxy> asps = mActionServerProxyManager.getActionServerProxies();
               for (ActionServerProxy asp : asps) {
                   AsyncPostRequest.sendPostRequest(getActivity(), asp.getHostName(), "refresh actions");
               }
               mDriveConnection.download(ActionServerProxyManager.RPC_CONFIG_FILENAME, mActionServerProxyManager.getRpcConfigFile());
               return true;
           default:
               return super.onOptionsItemSelected(item);
       }
    }

    private class ActionAdapter extends ArrayAdapter<Action> {
        public ActionAdapter(ArrayList<Action> actions) { super(getActivity(), 0, actions); }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           if (convertView == null) {
               convertView = getActivity().getLayoutInflater()
                       .inflate(R.layout.list_item_action, null);
           }

           Action a = getItem(position);

           TextView nameTextView = (TextView) convertView
                   .findViewById(R.id.action_list_item_nameTextView);
           nameTextView.setText(a.getName());

           return convertView;
        }

    }
}
