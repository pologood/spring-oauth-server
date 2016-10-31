package com.monkeyk.sos.domain.user;


public enum Privilege {
//    USER,          //Default privilege
//    UNITY,
//    MOBILE


    USER("USER"), CUSTOMER("CUSTOMER");

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    private String num;

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    private String userNum;

    private Privilege(String privilege) {
        this.privilege = privilege;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    private String privilege;

}