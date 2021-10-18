package com.hwadee.cb.sys.entity;

import java.util.Arrays;

public class Sensor {
	private float CpuTemperature;//cpu温度
	private float CpuVoltage;//cpu电压
	private Arrays FanSpeeds;//风扇速度
	public float getCpuTemperature() {
		return CpuTemperature;
	}
	public void setCpuTemperature(float cpuTemperature) {
		CpuTemperature = cpuTemperature;
	}
	public float getCpuVoltage() {
		return CpuVoltage;
	}
	public void setCpuVoltage(float cpuVoltage) {
		CpuVoltage = cpuVoltage;
	}
	public Arrays getFanSpeeds() {
		return FanSpeeds;
	}
	public void setFanSpeeds(Arrays fanSpeeds) {
		FanSpeeds = fanSpeeds;
	}
	
	
}
