package com.wallellen.netty.section6.c1;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/6/16.
 */
public class UserInfo implements Serializable {
    public static final long serialVersionUID = 1L;

    private String userName;
    private int userID;

    public UserInfo buildUserName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public UserInfo buildUserID(int userID) {
        this.setUserID(userID);
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public byte[] codeC() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.userID);
        buffer.flip();
        value = null;

        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }

    public byte[] codeC(ByteBuffer buffer) {
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.userID);
        buffer.flip();
        value = null;

        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }
}
