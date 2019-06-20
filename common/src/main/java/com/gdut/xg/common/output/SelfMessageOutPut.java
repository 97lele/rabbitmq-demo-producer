package com.gdut.xg.common.output;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lulu
 * @Date 2019/6/16 22:31
 */
@Data
public class SelfMessageOutPut implements Serializable {
    private static final long serialVersionUID = -3569482018082983802L;
    private Integer id;
    private String content;
    private String messageId;

}
