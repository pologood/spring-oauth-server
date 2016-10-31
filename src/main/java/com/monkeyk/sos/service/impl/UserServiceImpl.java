package com.monkeyk.sos.service.impl;

import com.monkeyk.sos.dao.IPrivilegeDao;
import com.monkeyk.sos.dao.IUserDao;
import com.monkeyk.sos.domain.UserDto;
import com.monkeyk.sos.domain.UserFormDto;
import com.monkeyk.sos.domain.UserJsonDto;
import com.monkeyk.sos.domain.UserOverviewDto;
import com.monkeyk.sos.domain.shared.security.WdcyUserDetails;
import com.monkeyk.sos.domain.user.Privilege;
import com.monkeyk.sos.domain.user.User;
import com.monkeyk.sos.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理用户, 账号, 安全相关业务
 *
 * @author yaoguang.du@duolabao.com
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private IUserDao userDao;

    @Resource
    private IPrivilegeDao privilegeDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userDao.findByUsername(username);
            if (user == null || user.isArchived()) {
                throw new UsernameNotFoundException("Not found any user for username[" + username + "]");
            }

            return new WdcyUserDetails(findPrivilieges(user));
        } catch (Exception e) {
            logger.error("exception occurred", e);
        }
        return null;

    }


    private User findPrivilieges(User user) {
        try {
            user.getPrivileges().addAll(privilegeDao.findByUserNum(user.getId()));
            return user;
        } catch (Exception e) {
            logger.error("exception occurred", e);
        }
        return user;
    }

    @Override
    public UserJsonDto loadCurrentUserJsonDto() {
        try {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            final Object principal = authentication.getPrincipal();

            if (authentication instanceof OAuth2Authentication &&
                    (principal instanceof String || principal instanceof org.springframework.security.core.userdetails.User)) {
                return loadOauthUserJsonDto((OAuth2Authentication) authentication);
            } else {
                final WdcyUserDetails userDetails = (WdcyUserDetails) principal;

                return new UserJsonDto(findPrivilieges(userDao.findByGuid(userDetails.user().getGuid())));
            }
        } catch (Exception e) {
            logger.error("exception occurred", e);
        }
        return null;

    }

    @Override
    public UserOverviewDto loadUserOverviewDto(UserOverviewDto overviewDto) {
        try {
            List<User> users = userDao.findUsersByUsername(overviewDto.getUsername());
            overviewDto.setUserDtos(UserDto.toDtos(users));
            return overviewDto;
        } catch (Exception e) {
            logger.error("exception occurred", e);
        }

        return null;
    }

    @Override
    public boolean isExistedUsername(String username) {
        try {
            final User user = userDao.findByUsername(username);
            return user != null;
        } catch (Exception e) {
            logger.error("exception occurred", e);
        }
        return false;
    }

    @Override
    public String saveUser(UserFormDto formDto) {
        try {
            User user = formDto.newUser();
            user.setId(user.getGuid());
            userDao.saveUser(user);

            List<Privilege> privileges = formDto.getPrivileges();

            if (privileges != null && privileges.size() > 0) {

                for (Privilege privilege : privileges) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("userNum", user.getId());
                    params.put("num", user.getId());
                    params.put("privilege", privilege.getPrivilege());

                    privilegeDao.savePrivilege(params);
                }

            }

            return user.getGuid();
        } catch (Exception e) {
            logger.error("exception occurred", e);
        }
        return null;
    }


    private UserJsonDto loadOauthUserJsonDto(OAuth2Authentication oAuth2Authentication) {
        try {
            UserJsonDto userJsonDto = new UserJsonDto();
            userJsonDto.setUsername(oAuth2Authentication.getName());

            final Collection<GrantedAuthority> authorities = oAuth2Authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                userJsonDto.getPrivileges().add(authority.getAuthority());
            }

            return userJsonDto;
        } catch (Exception e) {
            logger.error("exception occurred", e);

        }
        return null;

    }
}