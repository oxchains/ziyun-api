package com.oxchains.bean.dto.datav;

import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * NameValue
 *
 * @author liuruichao
 * Created on 2017/4/18 17:48
 */
@Data
public class NameValue<T> extends BaseEntity {
    private String name;

    private T value;

    public NameValue() {
    }

    public NameValue(String name, T value) {
        this.value = value;
    }
}