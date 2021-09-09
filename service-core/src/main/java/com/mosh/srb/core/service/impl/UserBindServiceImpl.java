package com.mosh.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mosh.common.exception.Assert;
import com.mosh.common.result.ResponseEnum;
import com.mosh.srb.core.enums.UserBindEnum;
import com.mosh.srb.core.hfb.FormHelper;
import com.mosh.srb.core.hfb.HfbConst;
import com.mosh.srb.core.hfb.RequestHelper;
import com.mosh.srb.core.mapper.UserBindMapper;
import com.mosh.srb.core.mapper.UserInfoMapper;
import com.mosh.srb.core.pojo.entity.UserBind;
import com.mosh.srb.core.pojo.entity.UserInfo;
import com.mosh.srb.core.pojo.vo.UserBindVO;
import com.mosh.srb.core.service.UserBindService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务实现类
 * </p>
 *
 * @author MoShuangHao
 * @since 2021-08-25
 */
@Service
public class UserBindServiceImpl extends ServiceImpl<UserBindMapper, UserBind> implements UserBindService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public String commitBindUser(UserBindVO userBindVO, Long userId) {
        //查询身份证号码是否绑定
        QueryWrapper<UserBind> userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper
                .eq("id_card",userBindVO.getIdCard())
                .ne("user_id", userId);;
        Integer count = baseMapper.selectCount(userBindQueryWrapper);
        //数据库有记录，则抛出异常，身份信息已绑定
        Assert.isTrue(count == 0, ResponseEnum.USER_BIND_IDCARD_EXIST_ERROR);

        //查询用户绑定信息
        userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper.eq("user_id",userId);
        UserBind userBind = baseMapper.selectOne(userBindQueryWrapper);

        //判断是否有绑定记录
        if (null == userBind) {
            UserBind userBindInsert = new UserBind();
            //如果未创建绑定记录，则创建一条记录，
            BeanUtils.copyProperties(userBindVO,userBindInsert);
            userBindInsert.setUserId(userId);
            //设置状态为未绑定
            userBindInsert.setStatus(UserBindEnum.NO_BIND.getStatus());
            baseMapper.insert(userBindInsert);
        }else {
            //曾经跳转到托管平台，但是未操作完成，此时将用户最新填写的数据同步到userBind对象
            BeanUtils.copyProperties(userBindVO,userBind);
            baseMapper.updateById(userBind);
        }
        //构建充值自动提交表单 FormHelper
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("agentId",HfbConst.AGENT_ID);
        paramMap.put("agentUserId", userId);
        paramMap.put("idCard",userBindVO.getIdCard());
        paramMap.put("personalName",userBindVO.getName());
        paramMap.put("bankType",userBindVO.getBankType());
        paramMap.put("bankNo",userBindVO.getBankNo());
        paramMap.put("mobile",userBindVO.getMobile());
        paramMap.put("email",null);
        paramMap.put("returnUrl",HfbConst.USERBIND_RETURN_URL);
        paramMap.put("notifyUrl",HfbConst.USERBIND_NOTIFY_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        paramMap.put("sign",RequestHelper.getSign(paramMap));

        String formStr = FormHelper.buildForm(HfbConst.USERBIND_URL,paramMap);
        return formStr;
    }

    @Override
    public void bindNotify(Map<String, Object> map) {
        //获取用户ID
        String agentUserId = (String) map.get("agentUserId");

        //根据user_id查询'用户绑定表'数据
        QueryWrapper<UserBind> userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper.eq("user_id",agentUserId);
        UserBind userBind = baseMapper.selectOne(userBindQueryWrapper);
        //设置用户绑定表 的bingcode和status
        userBind.setBindCode((String) map.get("bindCode"));
        userBind.setStatus(UserBindEnum.BIND_OK.getStatus());

        //查询用户信息表，把用户绑定表的数据同步到用户信息表
        UserInfo userInfo = userInfoMapper.selectById(agentUserId);
        userInfo.setIdCard(userBind.getIdCard());
        userInfo.setName(userBind.getName());
        userInfo.setBindCode((String) map.get("bindCode"));
        userInfo.setBindStatus(UserBindEnum.BIND_OK.getStatus());

        baseMapper.updateById(userBind);
        userInfoMapper.updateById(userInfo);
    }
}
