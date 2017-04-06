package com.oxchains.util;

import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

/**
 * CryptoUtils
 *
 * @author liuruichao
 * Created on 2017/3/30 14:06
 */
public final class CryptoUtils {
    public static CryptoSuite createCryptoSuite() throws CryptoException, InvalidArgumentException {
        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
        cryptoSuite.init();
        return cryptoSuite;
    }
}
