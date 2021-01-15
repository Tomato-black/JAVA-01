package com.cz;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Base64;

public class CustomClassLoad extends URLClassLoader {

    public CustomClassLoad(URL[] urls) {
        super(urls);
    }

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        URL[] arr = {new File("D:\\作业\\Hello.xlass").toURI().toURL()};
        CustomClassLoad customClassLoad = new CustomClassLoad(arr);
        Class<?> helloClass = customClassLoad.findClass("Hello");
        Object hello = helloClass.newInstance();
        Method helloMethod = helloClass.getMethod("hello");
        helloMethod.invoke(hello);
    }

    /**
     * InputStream转化为byte[]数组
     *
     * @param input
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        try {
            File file = new File("D:\\作业\\Hello.xlass");
            InputStream inputStream = new FileInputStream(file);
            byte[] bytes = toByteArray(inputStream);
            decode(bytes);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void decode(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (255 - bytes[i]);
        }
    }

}
