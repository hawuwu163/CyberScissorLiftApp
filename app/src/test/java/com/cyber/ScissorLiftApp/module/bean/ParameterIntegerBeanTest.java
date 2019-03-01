package com.cyber.ScissorLiftApp.module.bean;

import android.support.v7.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cyber.ScissorLiftApp.module.parameter.ParameterIntegerBean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.module.bean
 * @date 2019/1/18 21:24
 * @Description:
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AppCompatActivity.class})
public class ParameterIntegerBeanTest {

    @Test
    public void test(){
        
        ParameterIntegerBean single = JSON.parseObject(jsonSingle, ParameterIntegerBean.class);
        System.out.println(single);
//        List<ParameterIntegerBean> list = new ArrayList<>();
//        list.add(single);
//        list.add(single);
        System.out.println(jsonList);
        List<ParameterIntegerBean> list = JSONObject.parseArray(jsonList, ParameterIntegerBean.class);
        System.out.println(list);
        A a = new A();
        String sth = "dad";
        a.setS(sth);
        sth = "212";
        System.out.println(a.getS());

    }
    class A{
        String s;

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }
    }
    String jsonSingle = "{\n" +
            "    \"name\": \"压力开关\",\n" +
            "    \"description\": \"0：无效；1：有效\",\n" +
            "    \"val\": \"\",\n" +
            "    \"measurementUnit\": \"\",\n" +
            "    \"type\": 1,\n" +
            "    \"limit\": [0,1]\n" +
            "  }";
    String jsonList =
            "[\n" +
                    "          {\n" +
                    "            \"name\": \"机器模式代码\",\n" +
                    "            \"description\": \"\",\n" +
                    "            \"val\": \"\",\n" +
                    "            \"measurementUnit\": \"\",\n" +
                    "            \"type\": 0,\n" +
                    "            \"limit\": \"\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"name\": \"机器类型\",\n" +
                    "            \"description\": \"0：大液压；1：大电驱；2：小电驱；3：小液压\",\n" +
                    "            \"val\": \"\",\n" +
                    "            \"measurementUnit\": \"\",\n" +
                    "            \"type\": 0,\n" +
                    "            \"limit\": \"\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"name\": \"防夹手\",\n" +
                    "            \"description\": \"0：无效；1：有效\",\n" +
                    "            \"val\": \"\",\n" +
                    "            \"measurementUnit\": \"\",\n" +
                    "            \"type\": 1,\n" +
                    "            \"limit\": [0,1]\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"name\": \"动作报警\",\n" +
                    "            \"description\": \"0：无效；1：有效\",\n" +
                    "            \"val\": \"\",\n" +
                    "            \"measurementUnit\": \"\",\n" +
                    "            \"type\": 1,\n" +
                    "            \"limit\": [0,1]\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"name\": \"亏电报警\",\n" +
                    "            \"description\": \"0：无效；1：有效\",\n" +
                    "            \"val\": \"\",\n" +
                    "            \"measurementUnit\": \"\",\n" +
                    "            \"type\": 1,\n" +
                    "            \"limit\": [0,1]\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"name\": \"称重\",\n" +
                    "            \"description\": \"0：无效；1：有效\",\n" +
                    "            \"val\": \"\",\n" +
                    "            \"measurementUnit\": \"\",\n" +
                    "            \"type\": 1,\n" +
                    "            \"limit\": [0,1]\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"name\": \"坑洞保护\",\n" +
                    "            \"description\": \"0：有效；1：无效\",\n" +
                    "            \"val\": \"\",\n" +
                    "            \"measurementUnit\": \"\",\n" +
                    "            \"type\": 1,\n" +
                    "            \"limit\": [0,1]\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"name\": \"压力开关\",\n" +
                    "            \"description\": \"0：无效；1：有效\",\n" +
                    "            \"val\": \"\",\n" +
                    "            \"measurementUnit\": \"\",\n" +
                    "            \"type\": 1,\n" +
                    "            \"limit\": [0,1]\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"name\": \"速度阀控制\",\n" +
                    "            \"description\": \"0：高速有效；1：低速有效\",\n" +
                    "            \"val\": \"\",\n" +
                    "            \"measurementUnit\": \"\",\n" +
                    "            \"type\": 1,\n" +
                    "            \"limit\": [0,1]\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"name\": \"水平倾角功能\",\n" +
                    "            \"description\": \"0：无效；1：有效\",\n" +
                    "            \"val\": \"\",\n" +
                    "            \"measurementUnit\": \"\",\n" +
                    "            \"type\": 1,\n" +
                    "            \"limit\": [0,1]\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"name\": \"高度倾角功能\",\n" +
                    "            \"description\": \"0：无效；1：有效\",\n" +
                    "            \"val\": \"\",\n" +
                    "            \"measurementUnit\": \"\",\n" +
                    "            \"type\": 1,\n" +
                    "            \"limit\": [0,1]\n" +
                    "          }\n" +
                    "        ]";
}