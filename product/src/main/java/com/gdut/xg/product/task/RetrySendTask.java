package com.gdut.xg.product.task;

import com.gdut.xg.common.output.SelfMessageOutPut;
import com.gdut.xg.product.entity.MessageContent;
import com.gdut.xg.product.entity.SelfMessage;
import com.gdut.xg.product.repositories.MessageContentDao;
import com.gdut.xg.product.sender.MessageSender;
import com.google.gson.Gson;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author lulu
 * @Date 2019/6/20 22:55
 */
@Component
public class RetrySendTask {
@Autowired
private MessageContentDao messageContentDao;
@Autowired
private MessageSender sender;
@Autowired
private Gson gson;
private SimpleDateFormat dateFormat=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
//从第20秒开始,每30秒执行一次
    @Scheduled(cron="20/30 * * * * ?  ")
    public void ReSendTask() throws ParseException {
       List<MessageContent> contentList= messageContentDao.queryByRetryCount();
       for(MessageContent m:contentList){
           if(dateFormat.parse(m.getNextSendDate()).compareTo(new Date(System.currentTimeMillis()))<=0){
               SelfMessage message=gson.fromJson(m.getMessageContent(),SelfMessage.class);
               SelfMessageOutPut outPut = new SelfMessageOutPut();
               outPut.setContent(message.getContent());
               outPut.setId(message.getId());
               outPut.setMessageId(message.getMessageId());
               //更改发送次数、状态
               m.setStatus(0);
m.setMessageId(message.getMessageId());
               m.setReSenderCount(m.getReSenderCount()+1);
               messageContentDao.update(m);
               //重新发送
               sender.sendMessage(outPut);
           };

       }


    }
}
