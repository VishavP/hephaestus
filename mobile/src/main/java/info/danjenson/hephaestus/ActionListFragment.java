package info.danjenson.hephaestus;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by danj on 1/12/15.
 */
public class ActionListFragment extends ListFragment {
    private ArrayList<Action> mActions;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActions = ActionCenter.get(getActivity()).getActions();
        ActionAdapter adapter = new ActionAdapter(mActions);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Action a = ((ActionAdapter) getListAdapter()).getItem(position);
        // TODO: POST REQUEST
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
