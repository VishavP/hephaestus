package info.danjenson.hephaestus;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by danj on 1/12/15.
 */
public class ActionListFragment extends ListFragment {
    private ArrayList<Action> mActions;
    private ActionServerProxyManager mActionServerProxyManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionServerProxyManager = ActionServerProxyManager.get(getActivity());
        mActions = mActionServerProxyManager.getActions();
        ActionAdapter adapter = new ActionAdapter(mActions);
        adapter.sort(new Comparator<Action>() {
            @Override
            public int compare(Action a1, Action a2) {
                return a1.compareTo(a2);
            }
        });
        setListAdapter(adapter);
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
                    executePostRequest(options[which], a);
                }
            });
            builder.show();
        } else {
            hostname = a.getAllowedHosts().get(0);
            executePostRequest(hostname, a);
        }
    }

    private void executePostRequest(String hostname, Action a) {
        Toast toast = Toast.makeText(getActivity(), hostname + ": " + a.getName(), Toast.LENGTH_SHORT);
        toast.show();
        mActionServerProxyManager.executePostRequest(hostname, a);
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
