package com.monkeyk.sos.service.impl;

import com.monkeyk.sos.dao.IPrivilegeDao;
import com.monkeyk.sos.dao.IUserDao;
import com.monkeyk.sos.domain.UserDto;
import com.monkeyk.sos.domain.UserFormDto;
import com.monkeyk.sos.domain.UserJsonDto;
import com.monkeyk.sos.domain.UserOverviewDto;
import com.monkeyk.sos.domain.shared.security.WdcyUserDetails;
import com.monkeyk.sos.domain.user.User;
import com.monkeyk.sos.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 处理用户, 账号, 安全相关业务
 *
 * @author Shengzhao Li
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private IUserDao userDao;

    @Resource
    private IPrivilegeDao privilegeDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null || user.archived()) {
            throw new UsernameNotFoundException("Not found any user for username[" + username + "]");
        }

        return new WdcyUserDetails(findPrivilieges(user));
    }


    private User findPrivilieges(User user){
        user.privileges().addAll(privilegeDao.findByUserId(user.getId()));
        return user;
    }

    @Override
    public UserJsonDto loadCurrentUserJsonDto() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Object principal = authentication.getPrincipal();

        if (authentication instanceof OAuth2Authentication &&
                (principal instanceof String || principal instanceof org.springframework.security.core.userdetails.User)) {
            return loadOauthUserJsonDto((OAuth2Authentication) authentication);
        } else {
            final WdcyUserDetails userDetails = (WdcyUserDetails) principal;

            return new UserJsonDto(findPrivilieges(userDao.findByGuid(userDetails.user().guid())));
        }
    }

    @Override
    public UserOverviewDto loadUserOverviewDto(UserOverviewDto overviewDto) {
        List<User> users = userDao.findUsersByUsername(overviewDto.getUsername());
        overviewDto.setUserDtos(UserDto.toDtos(users));
        return overviewDto;
    }

    @Override
    public boolean isExistedUsername(String username) {
        final User user = userDao.findByUsername(username);
        return user != null;
    }

    @Override
    public String saveUser(UserFormDto formDto) {
        User user = formDto.newUser();
        userDao.saveUser(user);
        return user.guid();
    }


    private UserJsonDto loadOauthUserJsonDto(OAuth2Authentication oAuth2Authentication) {
        UserJsonDto userJsonDto = new UserJsonDto();
        userJsonDto.setUsername(oAuth2Authentication.getName());

        final Collection<GrantedAuthority> authorities = oAuth2Authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            userJsonDto.getPrivileges().add(authority.getAuthority());
        }

        return userJsonDto;
    }
}