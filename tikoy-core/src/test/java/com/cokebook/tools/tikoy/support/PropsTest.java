package com.cokebook.tools.tikoy.support;

import com.alibaba.fastjson2.JSON;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @date 2025/2/3
 */
public class PropsTest {

    @Test
    public void test() {


        Conf conf = new Conf();
        conf.name = "app";

        Map<String, Object> map = Props.toMap(conf);

        System.out.println(JSON.toJSONString(map));


        Conf conf2 = Props.of(key -> map.get(key) != null ? map.get(key).toString() : null,
                null,
                Conf.class);

        System.out.println("name = " + conf2.name);


        Assert.assertEquals(conf.name, conf2.name);
    }

    @Data
    public static class Conf {
        @Props.Key("conf.name")
        private String name;
    }
}
