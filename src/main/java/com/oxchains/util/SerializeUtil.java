package com.oxchains.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SerializeUtil {

    /**
     * 序列化一个对象
     * @param obj
     * @return
     */
    public static byte[] serializeObject(Object obj){
        ObjectOutputStream oos = null;
        ByteArrayOutputStream byteOut = null;
        try{
            byteOut = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(byteOut);
            oos.writeObject(obj);
            byte[] bytes = byteOut.toByteArray();
            return bytes;
        }catch (Exception e) {
            log.error("对象序列化失败",e);
        }
        finally {
            close(oos);
            close(byteOut);
        }
        return null;
    }

    /**
     * 反序列化一个对象
     * @param bytes
     * @return
     */
    public static Object unSerializeObject(byte[] bytes){
        ByteArrayInputStream in = null;
        ObjectInputStream objIn = null;
        try{
            in = new ByteArrayInputStream(bytes);
            objIn = new ObjectInputStream(in);
            return objIn.readObject();
        }catch (Exception e) {
            log.error("对象反序列化失败",e);
        }
        finally {
            close(in);
            close(objIn);
        }
        return null;
    }

    /**
     * 序列化 list 集合
     *
     * @param list
     * @return
     */
    public static byte[] serializeList(List<?> list) {

        if (list.isEmpty()) {
            return null;
        }
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        byte[] bytes = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            for (Object obj : list) {
                oos.writeObject(obj);
            }
            bytes = baos.toByteArray();
        } catch (Exception e) {
            log.error("list对象序列化失败",e);
        } finally {
            close(oos);
            close(baos);
        }
        return bytes;
    }

    /**
     * 反序列化 list 集合
     * @param bytes
     * @return
     */
    public static List<?> unSerializeList(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        List<Object> list = new ArrayList<Object>();
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            while (bais.available() > 0) {
                Object obj = (Object) ois.readObject();
                if (obj == null) {
                    break;
                }
                list.add(obj);
            }
        } catch (Exception e) {
            log.error("list对象反序列化失败",e);
        }
        finally {
            close(bais);
            close(ois);
        }
        return list;
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                log.error("关闭流错误",e);
            }
        }
    }
}
