package com.mosh.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mosh.common.exception.Assert;
import com.mosh.common.result.ResponseEnum;
import com.mosh.srb.core.enums.BorrowerStatusEnum;
import com.mosh.srb.core.mapper.BorrowerAttachMapper;
import com.mosh.srb.core.mapper.BorrowerMapper;
import com.mosh.srb.core.mapper.UserInfoMapper;
import com.mosh.srb.core.pojo.entity.Borrower;
import com.mosh.srb.core.pojo.entity.BorrowerAttach;
import com.mosh.srb.core.pojo.entity.UserInfo;
import com.mosh.srb.core.pojo.vo.BorrowerAttachVO;
import com.mosh.srb.core.pojo.vo.BorrowerDetailVO;
import com.mosh.srb.core.pojo.vo.BorrowerVO;
import com.mosh.srb.core.service.BorrowerService;
import com.mosh.srb.core.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 *
 * @author MoShuangHao
 * @since 2021-08-25
 */
@Service
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private BorrowerAttachMapper borrowerAttachMapper;

    @Resource
    private DictService dictService;

    @Override
    public void saveBorrowerVOByUserId(BorrowerVO borrowerVO, Long userId) {
        // 查询 借款人表 ，为 null 插入数据
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper.eq("user_id",userId);
        Borrower borrower = baseMapper.selectOne(borrowerQueryWrapper);

        // 不为 null 提示 借款人信息已存在
        Assert.isNull(borrower, ResponseEnum.BORROW_INFO_ERROR);

        UserInfo userInfo = userInfoMapper.selectById(userId);

        // 保存Borrower，状态认证中
        borrower = new Borrower();
        BeanUtils.copyProperties(borrowerVO,borrower);
        borrower.setUserId(userId);
        borrower.setName(userInfo.getName());
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setMobile(userInfo.getMobile());
        borrower.setStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        baseMapper.insert(borrower);

        // 生成Borrower主键,保存borrower_attach
        List<BorrowerAttach> borrowerAttachList = borrowerVO.getBorrowerAttachList();
        for (BorrowerAttach borrowerAttach : borrowerAttachList) {
            borrowerAttach.setBorrowerId(borrower.getId());
            borrowerAttachMapper.insert(borrowerAttach);
        }

        //更新会员状态，更新为认证中
        userInfo.setBorrowAuthStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public IPage<Borrower> listPage(Page<Borrower> borrowerPage, String keyword) {

        if (null == keyword){
            return baseMapper.selectPage(borrowerPage,null);
        }
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper
                .like("name",keyword).or()
                .like("mobile",keyword).or()
                .like("id_card",keyword);

        Page<Borrower> pageModel = baseMapper.selectPage(borrowerPage, borrowerQueryWrapper);
        return pageModel;
    }

    @Override
    public BorrowerDetailVO getBorrowerDetailVO(Long id) {

        BorrowerDetailVO borrowerDetailVO = new BorrowerDetailVO();

        Borrower borrower = baseMapper.selectById(id);
        BeanUtils.copyProperties(borrower,borrowerDetailVO);
        borrowerDetailVO.setSex(borrower.getSex() == 1 ? "男":"女");
        borrowerDetailVO.setMarry(borrower.getMarry()? "已婚" : "未婚");
        String education = dictService.getNameByParentDictCodeAndValue("education", borrower.getEducation());

        borrowerDetailVO.setEducation(dictService.getNameByParentDictCodeAndValue("education", borrower.getEducation()));
        borrowerDetailVO.setIndustry(dictService.getNameByParentDictCodeAndValue("industry",borrower.getIndustry()));
        borrowerDetailVO.setIncome(dictService.getNameByParentDictCodeAndValue("income",borrower.getIncome()));
        borrowerDetailVO.setReturnSource(dictService.getNameByParentDictCodeAndValue("returnSource",borrower.getReturnSource()));
        borrowerDetailVO.setContactsRelation(dictService.getNameByParentDictCodeAndValue("relation",borrower.getContactsRelation()));
        borrowerDetailVO.setStatus(BorrowerStatusEnum.getMsgByStatus(borrower.getStatus()));

        QueryWrapper<BorrowerAttach> borrowerAttachQueryWrapper = new QueryWrapper<>();
        borrowerAttachQueryWrapper.eq("borrower_id",id);

        List<BorrowerAttachVO> borrowerAttachList = new ArrayList<>();

        List<BorrowerAttach> borrowerAttachListByDb = borrowerAttachMapper.selectList(borrowerAttachQueryWrapper);

        for (BorrowerAttach borrowerAttach : borrowerAttachListByDb) {
            BorrowerAttachVO borrowerAttachVO = new BorrowerAttachVO();
            borrowerAttachVO.setImageType(borrowerAttach.getImageType());
            borrowerAttachVO.setImageUrl(borrowerAttach.getImageUrl());

            borrowerAttachList.add(borrowerAttachVO);
        }
        borrowerDetailVO.setBorrowerAttachVOList(borrowerAttachList);

        return borrowerDetailVO;
    }

}
