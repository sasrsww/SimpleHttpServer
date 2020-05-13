package com.sww;

import static org.junit.Assert.assertTrue;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        Integer i = JSON.parseObject("30", Integer.class);
        System.out.println(i);

        String name = JSON.parseObject("sww", String.class);
        System.out.println(name);
    }
}
