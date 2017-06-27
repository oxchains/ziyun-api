package com.oxchains.repository;

import com.oxchains.bean.model.ziyun.SignData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Luo_xuri on 2017/6/26.
 */
@Repository
public interface VerifySignRepository extends CrudRepository<SignData, Integer> {

    // 根据hash查找
    SignData findByDataHash(String hash);

}
