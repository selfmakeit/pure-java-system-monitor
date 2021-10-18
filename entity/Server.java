package com.hwadee.cb.sys.entity;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.alibaba.fastjson.JSONObject;
import com.hwadee.cb.sys.util.Arith;
import com.hwadee.cb.sys.util.IPUtils;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.Sensors;
import oshi.software.os.FileSystem;
import oshi.software.os.NetworkParams;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import oshi.util.Util;

/**
 * 服务器相关信息
 * 
 * @author Q
 */
public class Server implements Serializable{
    private static final int OSHI_WAIT_SECOND = 1000;
    
    /**
     * CPU相关信息
     */
    private Cpu cpu = new Cpu();

    /**
     * 內存相关信息
     */
    private Mem mem = new Mem();

    /**
     * JVM相关信息
     */
    private Jvm jvm = new Jvm();

    /**
     * 服务器相关信息
     */
    private Sys sys = new Sys();

    /**
     * 磁盘相关信息
     */
    private List<SysFile> sysFiles = new LinkedList<SysFile>();
    /**
     * 网卡
     */
    private List<Net> nets = new LinkedList<Net>();
    /**
     * 传感器
     */
    private Sensor sensors=new Sensor();
    
    public Sensor getSensors() {
		return sensors;
	}

	public List<Net> getNets() {
		return nets;
	}

	public Cpu getCpu()
    {
        return cpu;
    }

    public Mem getMem()
    {
        return mem;
    }

    public Jvm getJvm()
    {
        return jvm;
    }

    public Sys getSys()
    {
        return sys;
    }

    public List<SysFile> getSysFiles()
    {
        return sysFiles;
    }

    /**
     * 获取服务器主机相关信息
     * @throws Exception
     */
    public void setAll() throws Exception
    {
        // 获取系统信息
        SystemInfo si = new SystemInfo();
        // 根据SystemInfo获取硬件实例
        HardwareAbstractionLayer hal = si.getHardware();
//       OperatingSystem os = si.getOperatingSystem();
        // 获取硬件CPU信息
        setCpuInfo(hal.getProcessor());
        // 获取硬件内存信息
        setMemInfo(hal.getMemory());
        //设置网卡信息
        setNetInfo(hal.getNetworkIFs());
        // 设置服务器信息
        setSysInfo();
        // 设置Java虚拟机
        setJvmInfo();
        // 设置磁盘信息
        setSysFiles(si.getOperatingSystem());
        //设置传感器数据
        setSensorsInfo(hal.getSensors());
        Sensors ss= hal.getSensors();
        System.out.println("temperature: "+ss.getCpuTemperature());
        System.out.println("fans: "+Arrays.toString(ss.getFanSpeeds()));
        System.out.println("CpuVoltage: "+ss.getCpuVoltage());
    }

    /**
     * 设置CPU信息
     */
    public void setCpuInfo(CentralProcessor processor)
    {
    	if(null==processor){
    		 SystemInfo si = new SystemInfo();
    	        // 根据SystemInfo获取硬件实例
    	        HardwareAbstractionLayer hal = si.getHardware();
    	        processor=hal.getProcessor();
    	}
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        double userPercent = 100d * user / totalCpu;
        double nicePercent = 100d * nice / totalCpu;
        double cSysPercent = 100d * cSys / totalCpu;
        double idlePercent = 100d * idle / totalCpu;
        double iowaitPercent = 100d * iowait / totalCpu;
        double irqPercent = 100d * irq / totalCpu;
        double softirqPercent = 100d * softirq / totalCpu;
        double stealPercent = 100d * steal / totalCpu;
        
//        System.out.println("userPercent :"+(userPercent));
//        System.out.println("nicePercent :"+(nicePercent));
//        System.out.println("cSysPercent :"+(cSysPercent));
//        System.out.println("idlePercent :"+(idlePercent));
//        System.out.println("iowaitPercent :"+(iowaitPercent));
//        System.out.println("irqPercent :"+(irqPercent));
//        System.out.println("softirqPercent :"+(softirqPercent));
//        System.out.println("stealPercent :"+(stealPercent));
//        System.out.println("stealPercent :"+(stealPercent));
        cpu.setCpuNum(processor.getLogicalProcessorCount());// Cpu 核数
        cpu.setTotalUsed(userPercent+cSysPercent);// CPU总的使用率
        cpu.setSysUsed(cSysPercent);// CPU系统使用率
        cpu.setUserUsed(userPercent);// CPU用户使用率
        cpu.setWait(iowaitPercent);// CPU当前等待率
        cpu.setFree(idlePercent);// CPU当前空闲率
//        System.out.println("userPercent :"+getDoubleWith2f(userPercent));
//        System.out.println("nicePercent :"+getDoubleWith2f(nicePercent));
//        System.out.println("cSysPercent :"+getDoubleWith2f(cSysPercent));
//        System.out.println("idlePercent :"+getDoubleWith2f(idlePercent));
//        System.out.println("iowaitPercent :"+getDoubleWith2f(iowaitPercent));
//        System.out.println("irqPercent :"+getDoubleWith2f(irqPercent));
//        System.out.println("softirqPercent :"+getDoubleWith2f(softirqPercent));
//        System.out.println("stealPercent :"+getDoubleWith2f(stealPercent));
//        System.out.println("stealPercent :"+getDoubleWith2f(stealPercent));
//        cpu.setCpuNum(processor.getLogicalProcessorCount());// Cpu 核数
//        cpu.setTotal(getDoubleWith2f(userPercent+cSysPercent));// CPU总的使用率
//        cpu.setSys(getDoubleWith2f(cSysPercent));// CPU系统使用率
//        cpu.setUsed(getDoubleWith2f(userPercent));// CPU用户使用率
//        cpu.setWait(getDoubleWith2f(iowaitPercent));// CPU当前等待率
//        cpu.setFree(getDoubleWith2f(idlePercent));// CPU当前空闲率
    }

    /**
     * 设置内存信息
     */
    public void setMemInfo(GlobalMemory memory)
    {
    	if(null==memory){
   		SystemInfo si = new SystemInfo();
        // 根据SystemInfo获取硬件实例
        HardwareAbstractionLayer hal = si.getHardware();
   	    memory=hal.getMemory();
   	}
        mem.setTotal(memory.getTotal());// 总内存大小
        mem.setUsed(memory.getTotal() - memory.getAvailable());// 已使用内存大小
        mem.setFree(memory.getAvailable());// 空闲内存大小
    }
    /**
     * 设置网卡
     */
    public void setNetInfo(List<NetworkIF> list)
    {
    	if(null==list){
   		 SystemInfo si = new SystemInfo();
        // 根据SystemInfo获取硬件实例
        HardwareAbstractionLayer hal = si.getHardware();
   	     list=hal.getNetworkIFs();
    	}
    	for(NetworkIF n:list){
    		Net net =new Net();
    		net.setName(n.getName()+"("+n.getDisplayName()+")");
    		net.setMacAddress(n.getMacaddr());
    		net.setIPv4(Arrays.toString(n.getIPv4addr()));
    		net.setIPv6(Arrays.toString(n.getIPv6addr()));
    		net.setMTU(net.getMTU());
    		net.setSpeed(FormatUtil.formatValue(n.getSpeed(), "bps"));
            if(n.getIPv4addr().length>0){
            	nets.add(net);
            }
//            boolean hasData = n.getBytesRecv() > 0 || n.getBytesSent() > 0 || n.getPacketsRecv() > 0
//            		|| n.getPacketsSent() > 0;
//            System.out.format("   Traffic: received %s/%s%s; transmitted %s/%s%s %n",
//                    hasData ? net.getPacketsRecv() + " packets" : "?",
//                    hasData ? FormatUtil.formatBytes(net.getBytesRecv()) : "?",
//                    hasData ? " (" + net.getInErrors() + " err)" : "",
//                    hasData ? net.getPacketsSent() + " packets" : "?",
//                    hasData ? FormatUtil.formatBytes(net.getBytesSent()) : "?",
//                    hasData ? " (" + net.getOutErrors() + " err)" : "");
        }
    }

    /**
     * 设置服务器信息
     */
    public void setSysInfo()
    {	
    	 // 获取系统信息
        SystemInfo si = new SystemInfo();
    	OperatingSystem os = si.getOperatingSystem();
    	NetworkParams np =os.getNetworkParams();
        // 获取当前的系统属性
        Properties props = System.getProperties();
        sys.setDomainName(np.getDomainName());
        sys.setHostName(np.getHostName());
        sys.setDns(np.getDnsServers());
        sys.setComputerName(IPUtils.getHostName());// 获取主机名称
        sys.setComputerIp(IPUtils.getHostIp());// 获取主机IP
        sys.setOsName(props.getProperty("os.name"));// 获取主机类型 Windows 10
        sys.setOsArch(props.getProperty("os.arch"));// 获取主机显卡类型 amd64
        sys.setUserDir(props.getProperty("user.dir"));// 获取项目所在路径 F:\git\ruoyi\RuoYi-Vue
    }

    /**
     * 设置Java虚拟机
     */
    public void setJvmInfo() throws UnknownHostException
    {
        Properties props = System.getProperties();
        jvm.setTotal(Runtime.getRuntime().totalMemory());// JVM总内存 625.5M
        jvm.setMax(Runtime.getRuntime().maxMemory());// JVM已使用内存 347.99M
        jvm.setFree(Runtime.getRuntime().freeMemory());// JVM空闲内存 277.51M
        jvm.setVersion(props.getProperty("java.version"));// jdk版本 1.8
        jvm.setHome(props.getProperty("java.home"));// JDK安装路径 C:\Program Files\Java\jdk1.8.0_201\jre
    }
    /**
     * 设置传感器数据
     */
    public void setSensorsInfo(Sensors sen)
    {	
    	if(null==sen){
   		    SystemInfo si = new SystemInfo();
   		    HardwareAbstractionLayer hal = si.getHardware();
   		    sen=hal.getSensors();
    	}
    	sensors.setCpuTemperature((float)(Math.round(sensors.getCpuTemperature()*10))/10);//保留一位小数
    	sensors.setFanSpeeds(sensors.getFanSpeeds());
    	sensors.setCpuVoltage((float)(Math.round(sensors.getCpuVoltage()*10))/10);
//    	System.out.format(" CPU Temperature: %.1f°C%n", sensors.getCpuTemperature());
//        System.out.println(" Fan Speeds: " + Arrays.toString(sensors.getFanSpeeds()));
//        System.out.format(" CPU Voltage: %.1fV%n", sensors.getCpuVoltage());
    }

    /**
     * 设置磁盘信息
     */
    public void setSysFiles(OperatingSystem os)
    {
    	if(null==os){
   		    SystemInfo si = new SystemInfo();
   	        os	=si.getOperatingSystem();
    	}
        // 根据 操作系统（OS） 获取 FileSystem
        FileSystem fileSystem = os.getFileSystem();
        // 根据 FileSystem 获取主机磁盘信息list集合
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray)
        {
            long free = fs.getUsableSpace();// 磁盘空闲容量
            long total = fs.getTotalSpace();// 磁盘总容量
            long used = total - free;// 磁盘已使用容量
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());// 磁盘符号 C:\
            sysFile.setSysTypeName(fs.getType());// 磁盘类型 NTFS
            sysFile.setTypeName(fs.getName());// 磁盘名称 本地固定磁盘 (C:)
            sysFile.setTotal(convertFileSize(total));// 磁盘总容量
            sysFile.setFree(convertFileSize(free));// 磁盘空闲容量
            sysFile.setUsed(convertFileSize(used));// 磁盘已使用容量
            sysFile.setUsage(Arith.mul(Arith.div(used, total, 4), 100));// 磁盘资源的使用率
            sysFiles.add(sysFile);
        }
    }

    /**
     * 字节转换
     * 
     * @param size 字节大小
     * @return 转换后值
     */
    public String convertFileSize(long size)
    {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb)
        {
            return String.format("%.1f GB", (float) size / gb);
        }
        else if (size >= mb)
        {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        }
        else if (size >= kb)
        {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        }
        else
        {
            return String.format("%d B", size);
        }
    }
    public double getDoubleWith2f(double result_value){
    	double get_double = (double)(Math.round(result_value*100)/100.0);
		return get_double;
    }
}
