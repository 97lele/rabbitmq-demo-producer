package com.gdut.xg.product.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lulu
 * @Date 2019/6/16 22:00
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name="message")
public class SelfMessage  {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private String content;
    private String messageId;
    private Date createDate;

}
