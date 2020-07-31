package com.example.module2;

import com.example.module2.model.dto.ProductDTO;
import com.example.module2.service.ProductService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class Test1 {
    /**
     *
     */
    @Autowired
    private ProductService productService;

    @Test
    @Ignore
    public void test1() {

        Map<String, Object> params = new HashMap<>();
        int count = productService.count(params);
        System.out.println(count);

    }

    @Test
    public void test2() {

        Map<String, Object> params = new HashMap<>();
        List<ProductDTO> productDTOS = productService.find(params, null, null);
        System.out.println(productDTOS);

    }

    @Before
    public void init() {
        System.out.println("--------------test start-----------------");
    }

    @After
    public void after() {
        System.out.println("--------------test end-------------------");
    }


}
