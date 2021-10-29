package com.bugtracker.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

//Util class for decode and generating jwt
public final class JwtUtil {

    public static Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());//Algorithm for encrypting/decrypting jwt
    public static JWTVerifier verifier = JWT.require(algorithm).build();//verifier for decrypting

    public static DecodedJWT getDecodedJWT(String token){//returns decoded jwt
        return verifier.verify(token);
    }

    public static String getUsername(String token){
        return getDecodedJWT(token).getSubject();
    }

    public static String[] getRoles(String token){
        return getDecodedJWT(token).getClaim("roles").asArray(String.class);
    }

    public static Date getExpiresAt(String token){
        return getDecodedJWT(token).getExpiresAt();
    }



    public static String createToken(String subject, String author, Collection<GrantedAuthority> authorities, Date expiresAt ){
        if(authorities!=null){
            return   JWT.create()
                    .withSubject(subject)//something unique about the user (username)
                    .withExpiresAt(expiresAt/*10 minutes*/)
                    .withIssuer(author)//Author of refresh_token (url of api for now)
                    .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))//roles of user
                    .sign(JwtUtil.algorithm);
        }

        return JWT.create()//refresh token doesn't need authorities saved that's why it's split
                .withSubject(subject)//something unique about the user (username)
                .withExpiresAt(expiresAt/*30 days*/)
                .withIssuer(author)//Author of refresh_token (url of api for now)
                .sign(JwtUtil.algorithm);

    }
}
