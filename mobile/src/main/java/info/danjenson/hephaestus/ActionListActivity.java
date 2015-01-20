package info.danjenson.hephaestus;

import android.app.Fragment;


public class ActionListActivity extends SingleFragmentActivity {
    public static final String TAG = "ACTION_LIST";

    @Override
    protected Fragment createFragment() { return new ActionListFragment(); }

    @Override
    protected String getTag() {
        return TAG;
    }
}
