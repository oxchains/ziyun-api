package com.oxchains.bean.dto.datav;

import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * XY
 *
 * @author liuruichao
 * Created on 2017/4/18 18:11
 */
@Data
public class XY extends BaseEntity {
    private String x;

    private Long y;

    public XY() {
    }

    public XY(String x, Long y) {
        this.x = x;
        this.y = y;
    }
}
