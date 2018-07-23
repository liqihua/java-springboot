package com.java.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;

/**
 * @author liqihua
 * @since 2018/5/10
 */
public class JVMUtils {

    /**
     * 返回jvm信息
     * @return
     */
    public static String info(){
        try {
            return "cpu: "+cpu()+"% | mem: "+mem()+"% | disk: "+disk()+"% | jvm-free-mem: "+jvmFreeMemory()+"M | jvm-max-mem: "+jvmMaxMemory()+"M | jvm-total-mem: "+jvmTotalMemory()+"M";
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 主机cpu使用情况
     * 单位：%
     * @return
     * @throws Exception
     */
    public static double cpu() throws Exception {
        if(!isLinux()){
            return 0;
        }
        double cpuUsed = 0;

        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec("top -b -n 1");// 调用系统的“top"命令

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = null;
            String[] strArray = null;

            while ((str = in.readLine()) != null) {
                int m = 0;

                // 只分析正在运行的进程,top进程本身除外
                if (str.indexOf(" R ") != -1) {
                    strArray = str.split(" ");
                    for (String tmp : strArray) {
                        if (tmp.trim().length() == 0){
                            continue;
                        }
                        // 第9列为CPU的使用百分比(RedHat
                        if (++m == 9) {
                            cpuUsed += Double.parseDouble(tmp);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        return cpuUsed;
    }


    /**
     * 主机内存监控
     * 单位：%
     * @return
     * @throws Exception
     */
    public static double mem() throws Exception {
        if(!isLinux()){
            return 0;
        }
        double menUsed = 0;
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec("top -b -n 1");// 调用系统的“top"命令


        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = null;
            String[] strArray = null;

            while ((str = in.readLine()) != null) {
                int m = 0;

                if (str.indexOf(" R ") != -1) {// 只分析正在运行的进程,top进程本身除外 &;&;
                    strArray = str.split(" ");
                    for (String tmp : strArray) {
                        if (tmp.trim().length() == 0)
                            continue;
                        // 9)--第10列为mem的使用百分比(RedHat 9)
                        if (++m == 10) {
                            menUsed += Double.parseDouble(tmp);

                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        return menUsed;
    }


    /**
     * 主机磁盘空间大小
     * 单位：%
     * @return
     * @throws Exception
     */
    public static double disk() throws Exception {
        if(!isLinux()){
            return 0;
        }
        double totalHD = 0;
        double usedHD = 0;
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec("df -hl");//df -hl 查看硬盘空间


        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = null;
            String[] strArray = null;
            while ((str = in.readLine()) != null) {
                int m = 0;
                strArray = str.split(" ");
                for (String tmp : strArray) {
                    if (tmp.trim().length() == 0)
                        continue;
                    ++m;
                    if (tmp.indexOf("G") != -1) {
                        if (m == 2) {
                            if (!tmp.equals("") && !tmp.equals("0")){
                                totalHD += Double.parseDouble(tmp.substring(0, tmp.length() - 1)) * 1024;
                            }
                        }
                        if (m == 3) {
                            if (!tmp.equals("none") && !tmp.equals("0")){
                                usedHD += Double.parseDouble(tmp.substring(0, tmp.length() - 1)) * 1024;
                            }

                        }
                    }
                    if (tmp.indexOf("M") != -1) {
                        if (m == 2) {
                            if (!tmp.equals("") && !tmp.equals("0")){
                                totalHD += Double.parseDouble(tmp.substring(0, tmp.length() - 1));
                            }
                        }
                        if (m == 3) {
                            if (!tmp.equals("none") && !tmp.equals("0")){
                                usedHD += Double.parseDouble(tmp.substring(0, tmp.length() - 1));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        //return (usedHD / totalHD) * 100;
        return new BigDecimal(usedHD).divide(new BigDecimal(totalHD),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).doubleValue();
    }


    /**
     * 获取jvm的free memory
     * jvm从主机挖过来而又没有用上的内存
     * 单位：M
     * @return
     */
    public static double jvmFreeMemory(){
        Runtime runtime = Runtime.getRuntime();
        return new BigDecimal(runtime.freeMemory()).divide(new BigDecimal(1024*1024),2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 获取jvm的max memory
     * jvm能从主机挖过来的内存大小
     * 单位：M
     * @return
     */
    public static double jvmMaxMemory(){
        Runtime runtime = Runtime.getRuntime();
        return new BigDecimal(runtime.maxMemory()).divide(new BigDecimal(1024*1024),2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 获取jvm的total memory
     * jvm已经从主机挖过来的的内存大小
     * 单位：M
     * @return
     */
    public static double jvmTotalMemory(){
        Runtime runtime = Runtime.getRuntime();
        return new BigDecimal(runtime.totalMemory()).divide(new BigDecimal(1024*1024),2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }



    /**
     * 判断是否linux系统
     * @return
     */
    public static boolean isLinux(){
        return osName().indexOf("linux") >= 0;
    }

    /**
     * 判断是否windows系统
     * @return
     */
    public static boolean isWindows(){
        return osName().indexOf("win") >= 0;
    }


    /**
     * 获取系统名
     * @return
     */
    public static String osName(){
        return System.getProperty("os.name").toLowerCase();
    }




}
