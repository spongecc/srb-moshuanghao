package com.mosh.srb.core.service.impl;

import com.mosh.srb.core.pojo.entity.UserAccount;
import com.mosh.srb.core.mapper.UserAccountMapper;
import com.mosh.srb.core.service.UserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author MoShuangHao
 * @since 2021-08-25
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

}
