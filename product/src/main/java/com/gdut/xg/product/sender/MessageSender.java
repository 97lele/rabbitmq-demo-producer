package com.gdut.xg.product.sender;

import com.gdut.xg.product.entity.MessageContent;
import com.gdut.xg.product.repositories.MessageContentDao;
import lombok.var;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.common.xcontent.json.JsonXContentParser;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import com.gdut.xg.common.output.SelfMessageOutPut;

import java.util.Optional;

/**
 * @author lulu
 * @Date 2019/6/16 22:05
 */
@Component
public class MessageSender  {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MessageContentDao contentDao;


 final RabbitTemplate.ConfirmCallback confirmCallback=new RabbitTemplate.ConfirmCallback() {
     /**
      *
      * @param correlationData 消息id
      * @param ack 是否被接受，即手动签收成功
      * @param s
      */
     @Override
     public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String s) {
         MessageContent content=contentDao.queryById(correlationData.getId());

         if(ack){
             content.setStatus(1);
             System.out.println("消息成功接受");
             contentDao.update(content);
         }else{
             System.out.println("消息接受失败");
             if(content.getReSenderCount()!=3){
                 content.setStatus(-1);
             }else{
                 content.setStatus(-2);
             }
             contentDao.update(content);
         }
     }
 };

    public void sendMessage(SelfMessageOutPut m){
        rabbitTemplate.setConfirmCallback(confirmCallback);
        CorrelationData c=new CorrelationData(m.getMessageId());
        rabbitTemplate.convertAndSend(
                "m-exchange2",//exchange
                "m.message",//routingkey
                m,//实体
                c//消息id
        );

    }
}
