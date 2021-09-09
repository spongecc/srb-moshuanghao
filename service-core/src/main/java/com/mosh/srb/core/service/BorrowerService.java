package com.mosh.srb.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mosh.srb.core.pojo.entity.Borrower;
import com.mosh.srb.core.pojo.vo.BorrowerDetailVO;
import com.mosh.srb.core.pojo.vo.BorrowerVO;

/**
 * <p>
 * 借款人 服务类
 * </p>
 *
 * @author MoShuangHao
 * @since 2021-08-25
 */
public interface BorrowerService extends IService<Borrower> {

    void saveBorrowerVOByUserId(BorrowerVO borrowerVO, Long userId);

    IPage<Borrower> listPage(Page<Borrower> borrowerPage, String keyword);

    BorrowerDetailVO getBorrowerDetailVO(Long id);
}
