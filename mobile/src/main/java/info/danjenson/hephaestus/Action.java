package info.danjenson.hephaestus;

import java.util.ArrayList;

/**
 * Created by danj on 1/12/15.
 */
public class Action {
    private String mName;
    private String mCommand;
    private ArrayList<String> mAllowedHosts;

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getCommand() {
        return mCommand;
    }

    public void setCommand(String mCommand) {
        this.mCommand = mCommand;
    }

    public ArrayList<String> getAllowedHosts() {
        return mAllowedHosts;
    }

    public void setAllowedHosts(ArrayList<String> allowedHosts) {
        mAllowedHosts = allowedHosts;
    }

    public Action(String name, String command, ArrayList<String> allowedHosts) {
        mName = name;
        mCommand = command;
        mAllowedHosts = allowedHosts;
    }

    public String toString() {
        return mName + ": " + mCommand;
    }
}
