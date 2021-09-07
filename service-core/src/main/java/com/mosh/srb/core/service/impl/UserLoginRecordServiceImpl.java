package com.mosh.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mosh.srb.core.pojo.entity.UserLoginRecord;
import com.mosh.srb.core.mapper.UserLoginRecordMapper;
import com.mosh.srb.core.service.UserLoginRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务实现类
 * </p>
 *
 * @author MoShuangHao
 * @since 2021-08-25
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> implements UserLoginRecordService {

    @Override
    public List<UserLoginRecord> listTop50(Long userId) {

        QueryWrapper<UserLoginRecord> userLoginRecordQueryWrapper = new QueryWrapper<>();
        userLoginRecordQueryWrapper.eq("user_Id",userId)
                .orderByDesc("id")
                .last("limit 50");

        List<UserLoginRecord> loginRecordList = baseMapper.selectList(userLoginRecordQueryWrapper);

        return loginRecordList;
    }
}
