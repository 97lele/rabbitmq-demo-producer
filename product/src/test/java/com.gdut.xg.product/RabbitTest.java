package com.gdut.xg.product;

import com.gdut.xg.common.output.SelfMessageOutPut;
import com.gdut.xg.product.annoation.NoAdd;
import com.gdut.xg.product.entity.MessageContent;
import com.gdut.xg.product.entity.SelfMessage;
import com.gdut.xg.product.sender.MessageSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lulu
 * @Date 2019/6/16 22:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitTest {
    @Autowired
    private MessageSender messageSender;
    @Test
    public void test(){
     /*   SelfMessageOutPut m=new SelfMessageOutPut();
m.setMessageId("xxx");
m.setContent("xx");
m.setId(1);
        messageSender.sendMessage(m);*/


    }
}
