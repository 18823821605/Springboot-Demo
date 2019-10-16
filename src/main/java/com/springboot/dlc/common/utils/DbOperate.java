package com.springboot.dlc.common.utils;

import java.io.*;

/**
 * @auther: liujiebang
 * @Date: Create in 2018/12/6
 * @Description:
 **/
public class DbOperate {
    /**
     * 数据库表备份
     *
     * @throws Exception
     */
    public static void tableBackup(String dbUser,String dbPass,String dbHost,String dbPort,String dbName,String savePath, String tableName)
            throws Exception {

        Runtime runtime = Runtime.getRuntime();
        // -u后面是用户名，-p是密码-p后面最好不要有空格，-family是数据库的名字
        Process process = runtime.exec("mysqldump -h " + dbHost + " -P "
                + dbPort + " -u " + dbUser + " -p" + dbPass + " " + dbName
                + " " + tableName);
        // 得到输入流，写成.sql文件
        InputStream inputStream = process.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(reader);
        String s = null;
        StringBuffer sb = new StringBuffer();
        while ((s = br.readLine()) != null) {
            sb.append(s + "\r\n");
        }
        s = sb.toString();
        File file = new File(savePath);
        file.getParentFile().mkdirs();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(s.getBytes());
        fileOutputStream.close();
        br.close();
        reader.close();
        inputStream.close();
    }

    /**
     * 备份数据库
     *
     * @param savePath
     * @throws Exception
     */
    public static void dbBackup(String dbUser, String dbPass, String dbHost,
                                String dbPort, String dbName, String savePath) throws Exception {

        Runtime runtime = Runtime.getRuntime();
        // -u后面是用户名，-p是密码-p后面最好不要有空格，-family是数据库的名字
        Process process = runtime.exec("mysqldump -h " + dbHost + " -P "
                + dbPort + " -u " + dbUser + " -p" + dbPass + " " + dbName);
        // 得到输入流，写成.sql文件
        InputStream inputStream = process.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(reader);
        String s = null;
        StringBuffer sb = new StringBuffer();
        while ((s = br.readLine()) != null) {
            sb.append(s + "\r\n");
        }
        s = sb.toString();
        File file = new File(savePath);
        file.getParentFile().mkdirs();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(s.getBytes());
        fileOutputStream.close();
        br.close();
        reader.close();
        inputStream.close();
    }

    /**
     * 执行sql文件
     *
     * @param savePath
     * @throws Exception
     */
    public static void dbRecover(String dbUser, String dbPass, String dbHost,
                                 String dbPort, String dbName, String savePath) throws Exception {
        // 获取操作数据库的相关属性
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("mysql -h" + dbHost + " -P " + dbPort
                + " -u " + dbUser + " -p" + dbPass
                + " --default-character-set=utf8 " + dbName);
        OutputStream outputStream = process.getOutputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(savePath)));
        String str = null;
        StringBuffer sb = new StringBuffer();
        while ((str = br.readLine()) != null) {
            sb.append(str + "\r\n");
        }
        str = sb.toString();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream,
                "utf-8");
        writer.write(str);
        writer.flush();
        outputStream.close();
        br.close();
        writer.close();
    }

    public static void main(String[] args) {
        try {
            dbBackup("lzsharebed_root", "Szdlclzsharebed@43", "rm-wz9at4hz13l4e5vw58o.mysql.rds.aliyuncs.com","3306", "lzsharebed", "D:/lzsharebed.sql");
            System.out.println("完成！");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
