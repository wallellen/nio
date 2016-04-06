package com.wallellen.netty.section6.c1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/6/16.
 */
public class PerformanceTestUserInfo {
    public static void main(String[] args) throws IOException {
        UserInfo info = new UserInfo();

        info.buildUserName("wallellen").buildUserID(100);

        int loop = 1000000;

        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;

        int fsize = 0;

        long start = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);

            os.writeObject(info);

            os.flush();
            os.close();

            byte[] b = bos.toByteArray();
            fsize += b.length;
            bos.close();
        }

        long endTime = System.currentTimeMillis();

        System.err.println(" === " + fsize + "[" + (endTime - start) + "]");

        fsize = 0;
        start = System.currentTimeMillis();
        ByteBuffer buffer = null;
        for(int i = 0; i < loop; i++) {
            buffer = ByteBuffer.allocate(1024);
            byte[] bytes = info.codeC(buffer);
            fsize += bytes.length;
        }
        endTime = System.currentTimeMillis();

        System.err.println(" === " + fsize + "[" + (endTime - start) + "]");

    }
}
