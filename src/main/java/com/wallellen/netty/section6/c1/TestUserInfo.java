package com.wallellen.netty.section6.c1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/6/16.
 */
public class TestUserInfo {
    public static void main(String[] args) throws IOException {
        UserInfo info = new UserInfo();

        info.buildUserID(100).buildUserName("wallellen");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);

        os.writeObject(info);
        os.flush();
        os.close();

        byte[] b = bos.toByteArray();


        System.err.println("The jdk serializable length is : " + b.length);

        bos.close();

        System.err.println("The byte array serializable length is: " + info.codeC().length);
    }
}
