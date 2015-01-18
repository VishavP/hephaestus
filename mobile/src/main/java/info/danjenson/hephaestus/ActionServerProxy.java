package info.danjenson.hephaestus;

import android.util.Log;

/**
 * Created by danj on 1/12/15.
 */
public class ActionServerProxy {
    private String mHostName;
    private String mLocalNetworkSSID;
    private String mLocalGatewayIpAddress;
    private String mRemoteIpAddress;
    private String mLocalIpAddress;
    private int mPort;
    private String mMacAddress;

    public String getHostName() {
        return mHostName;
    }

    public String getLocalNetworkSSID() { return mLocalNetworkSSID; }

    public String getLocalGatewayIpAddress() { return mLocalGatewayIpAddress; }

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

    public ActionServerProxy(String hostName, String localNetworkSSID, String localGatewayIpAddress,
                             String remoteIpAddress, String localIpAddress, int port, String macAddress) {
        mHostName = hostName;
        mLocalNetworkSSID =  localNetworkSSID;
        mLocalGatewayIpAddress = localGatewayIpAddress;
        mRemoteIpAddress = remoteIpAddress;
        mLocalIpAddress = localIpAddress;
        mPort = port;
        mMacAddress = macAddress;
    }

    public String toString() {
        return "HostName: " + mHostName + ", RemoteIP: " + mRemoteIpAddress +
               ", LocalIP: " + mLocalIpAddress + ", Port: " + mPort + ", Mac: " + mMacAddress;
    }
}
