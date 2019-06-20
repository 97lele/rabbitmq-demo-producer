package com.gdut.xg.product.entity;

import com.gdut.xg.product.annoation.NoAdd;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lulu
 * @Date 2019/6/17 22:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageContent implements Serializable {

    @NoAdd
    private static final long serialVersionUID = 4735722931634402363L;

    @NoAdd
    private String messageId;

    private String messageContent;

    private String nextSendDate;

    private Integer reSenderCount;

    private Integer status;



}
