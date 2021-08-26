package com.mosh.srb.core;

import com.mosh.srb.core.mapper.IntegralGradeMapper;
import com.mosh.srb.core.pojo.entity.IntegralGrade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/8/25
 */
@SpringBootTest
public class testMP {

    @Autowired
    private IntegralGradeMapper integralGradeMapper;

    @Test
    public void testConn(){
        List<IntegralGrade> integralGrades = integralGradeMapper.selectList(null);
        System.out.println("integralGrades = " + integralGrades);

    }

}
