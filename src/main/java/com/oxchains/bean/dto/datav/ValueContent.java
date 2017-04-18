package com.oxchains.bean.dto.datav;

import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * ValueContent
 *
 * @author liuruichao
 * Created on 2017/4/18 19:24
 */
@Data
public class ValueContent extends BaseEntity {
    private String value;

    private String content;

    public ValueContent() {
    }

    public ValueContent(String value, String content) {
        this.value = value;
        this.content = content;
    }
}
