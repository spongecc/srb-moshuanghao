package com.mosh.srb.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mosh.srb.core.pojo.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mosh.srb.core.pojo.query.UserInfoQuery;
import com.mosh.srb.core.pojo.vo.LoginVO;
import com.mosh.srb.core.pojo.vo.RegisterVO;
import com.mosh.srb.core.pojo.vo.UserInfoVO;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author MoShuangHao
 * @since 2021-08-25
 */
public interface UserInfoService extends IService<UserInfo> {

    void regist(RegisterVO registerVO);

    UserInfoVO login(LoginVO loginVO,String ip);

    IPage<UserInfo> listPage(Page<UserInfo> userInfoPage, UserInfoQuery userInfoQuery);

    void lock(Long id, Integer status);

    boolean checkMobile(String mobile);
}
