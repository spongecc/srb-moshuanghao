package com.mosh.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mosh.common.exception.Assert;
import com.mosh.common.result.ResponseEnum;
import com.mosh.common.util.MD5;
import com.mosh.srb.base.utils.JwtUtils;
import com.mosh.srb.core.mapper.UserAccountMapper;
import com.mosh.srb.core.mapper.UserInfoMapper;
import com.mosh.srb.core.mapper.UserLoginRecordMapper;
import com.mosh.srb.core.pojo.entity.UserAccount;
import com.mosh.srb.core.pojo.entity.UserInfo;
import com.mosh.srb.core.pojo.entity.UserLoginRecord;
import com.mosh.srb.core.pojo.query.UserInfoQuery;
import com.mosh.srb.core.pojo.vo.LoginVO;
import com.mosh.srb.core.pojo.vo.RegisterVO;
import com.mosh.srb.core.pojo.vo.UserInfoVO;
import com.mosh.srb.core.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserAccountMapper userAccountMapper;

    @Resource
    private UserLoginRecordMapper userLoginRecordMapper;

    @Transactional( rollbackFor = {Exception.class})
    @Override
    public void regist(RegisterVO registerVO) {

        String redisCode = (String) redisTemplate.opsForValue().get("srb:sms:code:" + registerVO.getMobile());
        Assert.equals(registerVO.getCode(),redisCode, ResponseEnum.CODE_ERROR);

        //判断用户是否被注册
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile ",registerVO.getMobile());
        Integer count = baseMapper.selectCount(queryWrapper);
        Assert.isTrue(count == 0,ResponseEnum.MOBILE_EXIST_ERROR);

        //插入用户基本信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserType(registerVO.getUserType());
        userInfo.setMobile(registerVO.getMobile());
        userInfo.setPassword(MD5.encrypt(registerVO.getPassword()));
        userInfo.setNickName(registerVO.getMobile());
        userInfo.setName(registerVO.getMobile());
        userInfo.setStatus(UserInfo.STATUS_NORMAL);
        userInfo.setHeadImg("qwerty.jpg");
        baseMapper.insert(userInfo);

        //创建会员账户
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userInfo.getId());
        userAccountMapper.insert(userAccount);


    }

    @Transactional( rollbackFor = {Exception.class})
    @Override
    public UserInfoVO login(LoginVO loginVO,String ip) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile",loginVO.getMobile());
        userInfoQueryWrapper.eq("user_type",loginVO.getUserType());
        UserInfo userInfo = baseMapper.selectOne(userInfoQueryWrapper);

        // 查询不到用户时，提示用户不存在
        Assert.notNull(userInfo,ResponseEnum.LOGIN_MOBILE_ERROR);

        // 输入密码与数据库密码不一致，提示密码错误
        Assert.equals(MD5.encrypt(loginVO.getPassword()),userInfo.getPassword(),ResponseEnum.LOGIN_PASSWORD_ERROR);

        // 检查用户是否被锁定
        Assert.equals(userInfo.getStatus(),UserInfo.STATUS_NORMAL,ResponseEnum.LOGIN_LOKED_ERROR);

        // 记录登录日志
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(userInfo.getId());
        userLoginRecord.setIp(ip);
        userLoginRecordMapper.insert(userLoginRecord);

        // 生成token
        String token = JwtUtils.createToken(userInfo.getId(), userInfo.getName());

        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfo,userInfoVO);
        userInfoVO.setToken(token);

        return userInfoVO;
    }

    @Override
    public IPage<UserInfo> listPage(Page<UserInfo> userInfoPage, UserInfoQuery userInfoQuery) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        if (null == userInfoQuery.getMobile() && null == userInfoQuery.getStatus() && null == userInfoQuery.getUserType()){
            return baseMapper.selectPage(userInfoPage,null);
        }

        userInfoQueryWrapper
                .eq(StringUtils.isNoneBlank(userInfoQuery.getMobile()),"mobile",userInfoQuery.getMobile())
                .eq(null != userInfoQuery.getStatus(),"status",userInfoQuery.getStatus())
                .eq(null != userInfoQuery.getUserType(),"user_type",userInfoQuery.getUserType());

        IPage<UserInfo> pageModel = baseMapper.selectPage(userInfoPage, userInfoQueryWrapper);
        return pageModel;
    }

    @Override
    public void lock(Long id, Integer status) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setStatus(status);
        baseMapper.updateById(userInfo);
    }

    @Override
    public boolean checkMobile(String mobile) {
        boolean flag = false;
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(userInfoQueryWrapper);
        if (count <= 0){
            flag = true;
        }
        return flag;
    }
}
