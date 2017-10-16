package com.oxchains.bean.dto;

import com.oxchains.bean.model.ziyun.SyzlExchangeRecord;
import com.oxchains.common.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * Created by root on 17-10-10.
 */
@Data
public class SyzlExchangeRecordDTO extends BaseEntity {
    private List<SyzlExchangeRecord> list;
}
