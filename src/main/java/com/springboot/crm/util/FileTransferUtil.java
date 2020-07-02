package com.springboot.crm.util;

import org.apache.commons.codec.binary.Base64;


import java.io.*;

public class FileTransferUtil {
    public static  String fileToBase64(String path) {
        System.out.println(path);
        String base64 = null;
        InputStream in = null;
        try {
            File file = new File(path);
            in = new FileInputStream(file);
            byte[] bytes=new byte[(int)file.length()];
            in.read(bytes);
            base64 = Base64.encodeBase64String(bytes);
            if (path.contains("jpg")) {
                base64 = "data:image/jpeg;base64," + base64;
            } else if (path.contains("png")){
                base64 = "data:image/png;base64," + base64;
            } else if(path.contains("txt")){
                base64 = "data:text/plain;base64," + base64;
            } else if (path.contains("doc")){
                base64 = "data:application/vnd.openxmlformats-officedocument.wordprocessingml.document;base64," + base64;
            } else {
                base64 = "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64," + base64;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return base64;
    }

//return the path
    public static String base64ToFile(String base64, String fileName, String type) {
        File file = null;
        //创建文件目录
        String filePath= "src/main/resources/RecordFiles";
        System.out.println(base64);
        File  dir = new File(filePath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = null;
        java.io.FileOutputStream fos = null;
        try {
//            byte[] bytes = Base64.decodeBase64(base64);
            byte[] bytes = null;
            if (type.contains("jpg")) {
                bytes = Base64.decodeBase64(base64.replace("data:image/jpeg;base64,", ""));
            } else if ((type.contains("png"))){
                bytes = Base64.decodeBase64(base64.replace("data:image/png;base64,", ""));
            } else if(type.contains("txt")) {
                bytes = Base64.decodeBase64(base64.replace("data:text/plain;base64,", ""));
            } else if ((type.contains("doc"))) {
                bytes = Base64.decodeBase64(base64.replace("data:application/vnd.openxmlformats-officedocument.wordprocessingml.document;base64,", ""));
            } else {
                bytes = Base64.decodeBase64(base64.replace("data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,", ""));
            }
            file= new File(filePath+"/"+fileName);
            fos = new java.io.FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath+"/"+fileName;
    }

}
