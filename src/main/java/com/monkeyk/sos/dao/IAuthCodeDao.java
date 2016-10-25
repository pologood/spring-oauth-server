package com.monkeyk.sos.dao;

import com.monkeyk.sos.domain.AuthCode;
import com.monkeyk.sos.domain.user.Privilege;

import java.util.List;

public interface IAuthCodeDao extends IBaseDao<AuthCode, String> {

    public AuthCode save(AuthCode authCode);

    public void remove(String code);

    public AuthCode findByCode(String code);
}
