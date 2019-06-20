package com.gdut.xg.product.web;

import com.gdut.xg.common.output.SelfMessageOutPut;
import com.gdut.xg.product.entity.MessageContent;
import com.gdut.xg.product.entity.SelfMessage;
import com.gdut.xg.product.repositories.MessageContentDao;
import com.gdut.xg.product.repositories.SelfMessageDao;
import com.gdut.xg.product.sender.MessageSender;
import com.google.gson.Gson;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author lulu
 * @Date 2019/6/18 10:42
 */
@RestController
public class SendMessageController {
    @Autowired
    private Gson gson;

    @Autowired
    private MessageSender sender;
    @Autowired
    private SelfMessageDao messageDao;
    @Autowired
    private MessageContentDao m;

    @GetMapping("/send")
    public String sendMessage(@RequestParam("content") String content) {

        String id = UUID.randomUUID().toString().replace("-", "");
        Long date = System.currentTimeMillis();
        SelfMessage message = new SelfMessage().setContent(content).setMessageId(id).setCreateDate(new Date(date));
        messageDao.save(message);

        MessageContent c = new MessageContent(id, gson.toJson(message),new Date(date+1000L*30).toString(), 0, 0);
        c.setMessageContent(gson.toJson(message));
        m.insert(c);
        SelfMessageOutPut outPut = new SelfMessageOutPut();
        outPut.setContent(message.getContent());
        outPut.setId(message.getId());
        outPut.setMessageId(message.getMessageId());
        sender.sendMessage(outPut);
        return "ok";
    }

}
