package info.danjenson.hephaestus;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by danj on 1/12/15.
 */
public class ActionListFragment extends ListFragment {
    private ArrayList<Action> mActions;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActions = ActionServerProxyManager.get(getActivity()).getActions();
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
        Action a = ((ActionAdapter) getListAdapter()).getItem(position);
        String name = null;
        if (a.getAllowedHosts().size() > 1) {
            // show popup menu
        } {
            name = a.getName();
        }
        Toast toast = Toast.makeText(getActivity(), "Executing: " + name, Toast.LENGTH_LONG);
        toast.show();
        // TODO: SUBMIT POST REQUEST
    }

    public void showMenu(View v, Action a) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
       switch (item.getItemId()) {
       }
       return true;
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
