package com.monkeyk.sos.domain;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

/**
 * Created by poppet on 16/10/25.
 */
public class AuthCode implements Serializable {

    private String num;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getAuthentication() {
        return authentication;
    }

    public void setAuthentication(byte[] authentication) {
        this.authentication = authentication;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private byte[] authentication;

    private Date createTime;

}
