package com.mosh.srb.core.service.impl;

import com.mosh.srb.core.pojo.entity.UserInfo;
import com.mosh.srb.core.mapper.UserInfoMapper;
import com.mosh.srb.core.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author MoShuangHao
 * @since 2021-08-25
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
