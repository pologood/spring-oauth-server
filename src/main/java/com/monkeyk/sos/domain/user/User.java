package com.monkeyk.sos.domain.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.monkeyk.sos.domain.AbstractDO;
import com.monkeyk.sos.domain.AbstractDomain;

/**
 * 定义用户
 *
 * @author Shengzhao Li
 */
public class User implements Serializable {

    private static final long serialVersionUID = -2921689304753120556L;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private boolean archived;

    private String guid;//GuidGenerator.generate();

    private String username;
    private String password;

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setDefaultUser(boolean defaultUser) {
        this.defaultUser = defaultUser;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    private String phone;
    private String email;
    //Default user is initial when create database, do not delete
    private boolean defaultUser = false;

    private Date lastLoginTime;

    private List<Privilege> privileges = new ArrayList<>();

    public User() {
    }

    public User(String username, String password, String phone, String email) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public boolean isDefaultUser() {
        return defaultUser;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{username='").append(username).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", guid='").append(guid).append('\'');
        sb.append(", defaultUser='").append(defaultUser).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }


    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public User setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }
}