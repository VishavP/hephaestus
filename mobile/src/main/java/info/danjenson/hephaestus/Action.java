package info.danjenson.hephaestus;

/**
 * Created by danj on 1/12/15.
 */
public class Action {

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

    private String mName;
    private String mCommand;

    public Action(String name, String command) {
        mName = name;
        mCommand = command;
    }
}
