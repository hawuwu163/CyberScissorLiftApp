package com.cyber.ScissorLiftApp.module.bluetooth;

import com.ble.ble.LeScanRecord;

public class BleDevice {
	private final String address;
	private String name;
	private String rxData = "No data";
	private LeScanRecord mRecord;
	private int rssi;
	private boolean oadSupported = false;
	private boolean isConnected = false;

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean connected) {
		isConnected = connected;
	}

	public BleDevice(String name, String address) {
		this.name = name;
		this.address = address;
	}

	public BleDevice(String name, String address, int rssi, byte[] scanRecord) {
		this.name = name==null? "":name;
		this.address = address;
		this.rssi = rssi;
		this.mRecord = LeScanRecord.parseFromBytes(scanRecord);
	}

	@Override
	public String toString() {
		return "BleDevice{" +
				"address='" + address + '\'' +
				", name='" + name + '\'' +
				", rxData='" + rxData + '\'' +
				", mRecord=" + mRecord +
				", rssi=" + rssi +
				", oadSupported=" + oadSupported +
				", isConnected=" + isConnected +
				'}';
	}

	public boolean isOadSupported() {
		return oadSupported;
	}

	public void setOadSupported(boolean oadSupported) {
		this.oadSupported = oadSupported;
	}

	public LeScanRecord getLeScanRecord() {
		return mRecord;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public String getRxData() {
		return rxData;
	}

	public void setRxData(String rxData) {
		this.rxData = rxData;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof BleDevice) {
			return ((BleDevice) o).getAddress().equals(address);
		}
		return false;
	}
}