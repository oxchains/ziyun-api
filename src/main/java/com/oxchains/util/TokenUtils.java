package com.oxchains.util;

import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.common.ConstantsData;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class TokenUtils {
    @Value("${jwt.key.store}")
    private String keystore;

    @Value("${jwt.key.pass}")
    private String keypass;

    @Value("${jwt.key.alias}")
    private String keyalias;

    @Value("${jwt.cert}")
    private String cert;

    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    @PostConstruct
    private void init() throws Exception {
        char[] pass = keypass.toCharArray();
        KeyStore from = KeyStore.getInstance("JKS", "SUN");
        from.load(new ClassPathResource(keystore).getInputStream(), pass);
        privateKey = (ECPrivateKey) from.getKey(keyalias, pass);

        CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
        X509Certificate x509Cert = (X509Certificate) certificatefactory.generateCertificate(new ClassPathResource(cert).getInputStream());
        publicKey = x509Cert.getPublicKey();
    }

    public static String createToken(String username) {
        return new DefaultJwtBuilder()
                .setSubject(username)
                .setId(username)
                .setExpiration(Date.from(ZonedDateTime
                        .now()
                        .plusWeeks(1)
                        .toInstant()))
                .signWith(SignatureAlgorithm.ES256, privateKey)
                .compact();
    }

    public static JwtToken parseToken(String token) {
        Jws<Claims> jws = new DefaultJwtParser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token);
        Claims claims = jws.getBody();
        JwtToken jwt = new JwtToken();
        jwt.setId(claims.getId());
        jwt.setExpiratioin(claims.getExpiration());
        return jwt;
    }

    public static void main(String[] args) {
        String username = "ziyun";
        String jwt = createToken(username);
        System.out.println("jwt=" + jwt);
        System.out.println(parseToken(jwt).getId());
        System.out.println(parseToken(jwt).getExpiratioin());
    }
}
