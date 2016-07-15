package com.itexico.utilities.lockmydevice.model;

/**
 * Created by iTexico Developer on 7/7/2016.
 */
public class DeviceInfo {

    private String mDeviceIMEI;
    private String mDeviceModel;
    private String mDeviceOSVersion;
    private String mDeviceSerialNumber;
    private String mDeviceiTexicoID;
    private String mDeviceOwnerEmailID;
    private String mDeviceOwnerUsername;
    private String mDeviceLastLogin;
    private String mDeviceInventoryLogo;
    private String mDeviceUserLogo;

    public String getDeviceUserLogo() {
        return mDeviceUserLogo;
    }

    public void setDeviceUserLogo(String mDeviceUserLogo) {
        this.mDeviceUserLogo = mDeviceUserLogo;
    }

    public String getDeviceInventoryLogo() {
        return mDeviceInventoryLogo;
    }

    public void setDeviceInventoryLogo(String mDeviceInventoryLogo) {
        this.mDeviceInventoryLogo = mDeviceInventoryLogo;
    }

    public String getDeviceLastLogin() {
        return mDeviceLastLogin;
    }

    public void setDeviceLastLogin(String deviceLastLogin) {
        mDeviceLastLogin = deviceLastLogin;
    }

    public String getDeviceIMEI() {
        return mDeviceIMEI;
    }

    public void setDeviceIMEI(String mDeviceIMEI) {
        this.mDeviceIMEI = mDeviceIMEI;
    }

    public String getDeviceModel() {
        return mDeviceModel;
    }

    public void setDeviceModel(String mDeviceModel) {
        this.mDeviceModel = mDeviceModel;
    }

    public String getDeviceSerialNumber() {
        return mDeviceSerialNumber;
    }

    public void setDeviceSerialNumber(String mDeviceSerialNumber) {
        this.mDeviceSerialNumber = mDeviceSerialNumber;
    }

    public String getDeviceOSVersion() {
        return mDeviceOSVersion;
    }

    public void setDeviceOSVersion(String mDeviceOSVersion) {
        this.mDeviceOSVersion = mDeviceOSVersion;
    }

    public String getDeviceiTexicoID() {
        return mDeviceiTexicoID;
    }

    public void setDeviceiTexicoID(String mDeviceiTexicoID) {
        this.mDeviceiTexicoID = mDeviceiTexicoID;
    }

    public String getDeviceOwnerEmailID() {
        return mDeviceOwnerEmailID;
    }

    public void setDeviceOwnerEmailID(String mDeviceOwnerEmailID) {
        this.mDeviceOwnerEmailID = mDeviceOwnerEmailID;
    }

    public String getDeviceOwnerUsername() {
        return mDeviceOwnerUsername;
    }

    public void setDeviceOwnerUsername(String mDeviceOwnerUsername) {
        this.mDeviceOwnerUsername = mDeviceOwnerUsername;
    }
}
