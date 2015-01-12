package info.danjenson.hephaestus;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by danj on 1/12/15.
 */
public class ActionCenter {
    private ArrayList<Action> mActions;
    private static ActionCenter sActionCenter;
    private Context mAppcontext;

    private ActionCenter(Context appContext) {
        mAppcontext = appContext;
        mActions = new ArrayList<Action>();
        for(int i = 0; i < 100; i++) {
            mActions.add(new Action("Action" + i, "Command" + i));
        }
    }

    public static ActionCenter get(Context c) {
       if (sActionCenter == null) {
           sActionCenter = new ActionCenter(c.getApplicationContext());
       }
       return sActionCenter;
    }

    public ArrayList<Action> getActions() { return mActions; }
    public void addAction(Action a) { mActions.add(a); }
    public void removeAction(Action a) { mActions.remove(a); }

    public Action getAction(String name) {
        for (Action a: mActions) {
            if (a.getName().equals(name))
                return a;
        }
        return null;
    }
}
