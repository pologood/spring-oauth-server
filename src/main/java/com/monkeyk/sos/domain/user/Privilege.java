package com.monkeyk.sos.domain.user;


public enum Privilege {
//    USER,          //Default privilege
//    UNITY,
//    MOBILE


    USER("USER"), UNITY("UNITY"), MOBILE("MOBILE");

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