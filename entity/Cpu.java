package com.hwadee.cb.sys.entity;

import java.io.Serializable;

import com.hwadee.cb.sys.util.Arith;


/**
* CPU相关信息
* 
*/
public class Cpu implements Serializable
{
   /**
    * 核心数
    */
   private int cpuNum;

   /**
    * CPU总的使用率
    */
   private double totalUsed;

   /**
    * CPU系统使用率
    */
   private double sysUsed;

   /**
    * CPU用户使用率
    */
   private double userUsed;

   /**
    * CPU当前等待率
    */
   private double wait;

   /**
    * CPU当前空闲率
    */
   private double free;

   public int getCpuNum()
   {
       return cpuNum;
   }

   public void setCpuNum(int cpuNum)
   {
       this.cpuNum = cpuNum;
   }

   public double getTotalUsed()
   {
       return Arith.round(totalUsed, 2);
   }

   public void setTotalUsed(double total)
   {
       this.totalUsed = total;
   }

   public double getSysUsed()
   {
       return Arith.round(sysUsed , 2);
   }

   public void setSysUsed(double sys)
   {
       this.sysUsed = sys;
   }

   public double getUserUsed()
   {
       return Arith.round(userUsed, 2);
   }

   public void setUserUsed(double used)
   {
       this.userUsed = used;
   }

   public double getWait()
   {
       return Arith.round(wait, 2);
   }

   public void setWait(double wait)
   {
       this.wait = wait;
   }

   public double getFree()
   {
       return Arith.round(free , 2);
   }

   public void setFree(double free)
   {
       this.free = free;
   }
}
