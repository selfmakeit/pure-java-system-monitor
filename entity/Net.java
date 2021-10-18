package com.hwadee.cb.sys.entity;

public class Net {
	private String Name;
	private String MacAddress;
	private String MTU;
	private String IPv4;
	private String IPv6;
	private String Speed;
	public String getSpeed() {
		return Speed;
	}
	public void setSpeed(String speed) {
		Speed = speed;
	}
	public Net() {
		super();
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getMacAddress() {
		return MacAddress;
	}
	public void setMacAddress(String macAddress) {
		MacAddress = macAddress;
	}
	public String getMTU() {
		return MTU;
	}
	public void setMTU(String mTU) {
		MTU = mTU;
	}
	public String getIPv4() {
		return IPv4;
	}
	public void setIPv4(String iPv4) {
		IPv4 = iPv4;
	}
	public String getIPv6() {
		return IPv6;
	}
	public void setIPv6(String iPv6) {
		IPv6 = iPv6;
	}

}
