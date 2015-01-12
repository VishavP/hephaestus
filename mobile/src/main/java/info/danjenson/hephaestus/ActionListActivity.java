package info.danjenson.hephaestus;

import android.app.Fragment;


public class ActionListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() { return new ActionListFragment(); }
    }

