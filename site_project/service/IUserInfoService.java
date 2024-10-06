package com.ecommerce.site_project.service;

import java.util.List;

import com.ecommerce.site_project.entity.UserInfo;

public interface IUserInfoService {

    public List<UserInfo> getAllUserDetails();

    public void saveUserDetail(UserInfo userInfo);

    public UserInfo getUserDetail(int id);

    public void deleteUserDetail(int id);
}
