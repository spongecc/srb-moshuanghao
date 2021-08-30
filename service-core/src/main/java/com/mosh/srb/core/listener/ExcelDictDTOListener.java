package com.mosh.srb.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mosh.srb.core.mapper.DictMapper;
import com.mosh.srb.core.pojo.dto.ExcelDictDTO;
import com.mosh.srb.core.pojo.entity.Dict;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/8/28
 */
@NoArgsConstructor
public class ExcelDictDTOListener extends AnalysisEventListener<ExcelDictDTO> {

    private DictMapper dictMapper = null;

    private static final int BATCH_COUNT = 5;
    private List<Dict> list = new ArrayList<>();

    public ExcelDictDTOListener(DictMapper dictMapper){
        this.dictMapper = dictMapper;
    }

    @Override
    public void invoke(ExcelDictDTO excelDictDTO, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(excelDictDTO,dict);
        list.add(dict);
        if (list.size() >= BATCH_COUNT){
            //写入数据库
            dictMapper.insertBatch(list);

            System.out.println("list = " + list.size());
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //加入判断，如果没有需要首位的数据，直接结束
        if (list.size() <= 0){
            return;
        }
        dictMapper.insertBatch(list);
    }
}
