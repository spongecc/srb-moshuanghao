package com.mosh.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mosh.srb.core.listener.ExcelDictDTOListener;
import com.mosh.srb.core.mapper.DictMapper;
import com.mosh.srb.core.pojo.dto.ExcelDictDTO;
import com.mosh.srb.core.pojo.entity.Dict;
import com.mosh.srb.core.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author MoShuangHao
 * @since 2021-08-25
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private RedisTemplate redisTemplate;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void importData(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelDictDTO.class,new ExcelDictDTOListener(baseMapper)).sheet().doRead();
    }

    @Override
    public List<ExcelDictDTO> listDictData() {
        List<Dict> dicts = baseMapper.selectList(null);
        ArrayList<ExcelDictDTO> excelDictDTOS = new ArrayList<>();
        for (Dict dict : dicts) {
            ExcelDictDTO excelDictDTO = new ExcelDictDTO();
            BeanUtils.copyProperties(dict,excelDictDTO);
            excelDictDTOS.add(excelDictDTO);
        }
        return excelDictDTOS;
    }

    @Override
    public List<Dict> listByParentId(Long parentId) {

        List<Dict> dicts = new ArrayList<>();
        //先查询redis缓存中是否存在数据
        dicts = (List<Dict>) redisTemplate.opsForValue().get("srb:core:dictList:" + parentId);
        // 如果缓存中没有值，则调用数据库查询，并存入redis缓存，设置过期时间5分钟
        if (null == dicts) {
            dicts = this.listByParentIdFromDb(parentId);
            redisTemplate.opsForValue().set("srb:core:dictList:" + parentId, dicts, 5, TimeUnit.MINUTES);
        }
        // 如果有值，获取redis缓存中的数据并返回
        return dicts;
    }

    private List<Dict> listByParentIdFromDb(Long parentId){
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id",parentId);
        List<Dict> dictList = baseMapper.selectList(dictQueryWrapper);
        for (Dict dict : dictList) {
            boolean hasChildren = this.hasChildren(dict.getId());
            dict.setHasChildren(hasChildren);
        }
        return dictList;
    }

    /**
     * 判断是否有子节点
     * @param id 父节点ID
     * @return 有子节点返回 true
     */
    private boolean hasChildren(Long id) {
        boolean hasChildren = false;

        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(dictQueryWrapper);
        if (count > 0){
            hasChildren = true;
        }
        return hasChildren;
    }


}
