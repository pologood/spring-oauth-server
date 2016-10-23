package com.monkeyk.sos.domain.oauth;

import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * Created by poppet on 16/10/22.
 */
public class ClientDetailsService extends JdbcClientDetailsService {

    public ClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }
}
