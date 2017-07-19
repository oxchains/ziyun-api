package com.oxchains.util;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.common.ConstantsData;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenUtils {

	public static String createToken(String username) {

		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(ConstantsData.TOKEN_SECRET);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder().setId(username).setIssuedAt(now).setSubject(ConstantsData.TOKEN_SUBJECT).setIssuer(ConstantsData.TOKEN_ISSUER)
				.signWith(signatureAlgorithm, signingKey);
		long expire = ConstantsData.TOKEN_EXPIRES;
		// if it has been specified, let's add the expiration
		if (expire >= 0) {
			long expMillis = nowMillis + expire * 1000;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}

		// Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}

	public static JwtToken parseToken(String token) {
		// This line will throw an exception if it is not a signed JWS (as expected)
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(ConstantsData.TOKEN_SECRET))
				.parseClaimsJws(token).getBody();
		JwtToken jwt = new JwtToken();
		jwt.setId(claims.getId());
		jwt.setIssueAt(claims.getIssuedAt());
		jwt.setIssuer(claims.getIssuer());
		jwt.setSubject(claims.getSubject());
		jwt.setExpiratioin(claims.getExpiration());
		return jwt;
	}
	
	public static void main(String[] args) {
		String username = "tom";
		String jwt = createToken(username);
		log.debug("jwt=" + jwt);
		parseToken(jwt);
		
	}
}
