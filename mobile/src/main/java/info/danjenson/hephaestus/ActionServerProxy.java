package info.danjenson.hephaestus;

/**
 * Created by danj on 1/12/15.
 */
public class ActionServerProxy {
    private String mHostName;
    private String mRemoteIpAddress;
    private String mLocalIpAddress;
    private int mPort;
    private String mMacAddress;

    public String getHostName() {
        return mHostName;
    }

    public void setHostName(String hostName) {
        mHostName = hostName;
    }

    public String getRemoteIpAddress() {
        return mRemoteIpAddress;
    }

    public void setRemoteIpAddress(String remoteIpAddress) {
        mRemoteIpAddress = remoteIpAddress;
    }

    public String getLocalIpAddress() {
        return mLocalIpAddress;
    }

    public void setLocalIpAddress(String localIpAddress) {
        mLocalIpAddress = localIpAddress;
    }

    public int getPort() {
        return mPort;
    }

    public void setPort(int port) {
        mPort = port;
    }

    public String getMacAddress() {
        return mMacAddress;
    }

    public void setMacAddress(String macAddress) {
        mMacAddress = macAddress;
    }

    public ActionServerProxy(String hostName, String remoteIpAddress, String localIpAddress,
                             int port, String macAddress) {
        mHostName = hostName;
        mRemoteIpAddress = remoteIpAddress;
        mLocalIpAddress = localIpAddress;
        mPort = port;
        mMacAddress = macAddress;
    }

}
