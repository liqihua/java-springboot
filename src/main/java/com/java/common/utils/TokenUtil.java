package com.java.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.java.common.constance.MyConstance;
import com.java.sys.common.cache.CacheUtil;
import com.java.sys.common.utils.Tool;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class TokenUtil {
    public static final String USER_ID_SUFFIX_APP = "-APP";
    public static final String USER_ID_SUFFIX_WEB = "-WEB";

    public static String makeTokenAPP(String userId,Date expireTime){
        return makeToken(userId+USER_ID_SUFFIX_APP,expireTime);
    }

    public static String makeTokenWEB(String userId,Date expireTime){
        return makeToken(userId+ USER_ID_SUFFIX_WEB,expireTime);
    }

    /**
     * 生成jwt token
     * @param userId
     * @param expireTime
     * @return
     */
    public static String makeToken(String userId,Date expireTime){
        try {
            Algorithm algorithm = Algorithm.HMAC256(MyConstance.JWT_SECRET);
            return JWT.create().withExpiresAt(expireTime).withIssuer(userId).sign(algorithm);
        } catch (UnsupportedEncodingException exception){
            exception.printStackTrace();
        } catch (JWTCreationException exception){
            exception.printStackTrace();
        }
        return null;
    }



    /**
     * 校验toekn是否有效
     * @param userId
     * @param token
     * @return
     */
    public static boolean verifyToken(String userId,String token){
        boolean active = true;
        try {
            Algorithm algorithm = Algorithm.HMAC256(MyConstance.JWT_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(userId).build();
            verifier.verify(token);
        } catch (TokenExpiredException exception){
            //System.out.println("--- token 过期");
            active = false;
        } catch (JWTDecodeException exception){
            //System.out.println("--- token 无效");
            active = false;
        } catch (UnsupportedEncodingException exception){
            //System.out.println("--- token 无效");
            active = false;
        } catch (JWTVerificationException exception){
            //System.out.println("--- token 错误");
            active = false;
        }
        return active;
    }



    /**
     * 判断用户是否登录
     * @param userId
     * @param token
     * @return
     */
    public static boolean isLogin(String userId,String token){
        if(Tool.isNotBlank(userId)) {
            if(Tool.isNotBlank(token)) {
                //System.out.println("--- isLogin userId:"+userId+",token:"+token);
                String existValue = (String) CacheUtil.hget(MyConstance.KEY_TOKEN_MAP, userId);
                //System.out.println("existValue:"+existValue);
                if(Tool.isNotBlank(existValue) && existValue.equals(token)) {
                    boolean isLogin = verifyToken(userId,token);
                    //System.out.println("isLogin:"+isLogin);
                    if(!isLogin){
                        CacheUtil.hdel(MyConstance.KEY_TOKEN_MAP,userId);
                    }
                    return isLogin;
                }
            }
        }
        return false;
    }

    /**
     * 判断用户是否登录-APP
     * @param userId
     * @param token
     * @return
     */
    public static boolean isLoginAPP(String userId,String token){
        return isLogin(userId+USER_ID_SUFFIX_APP,token);
    }

    /**
     * 判断用户是否登录-WEB
     * @param userId
     * @param token
     * @return
     */
    public static boolean isLoginWEB(String userId,String token){
        return isLogin(userId+ USER_ID_SUFFIX_WEB,token);
    }

    /**
     * 判断用户是否登录
     * @param userId
     * @param token
     * @return
     */
    public static boolean noLogin(String userId,String token){
        return !isLogin(userId,token);
    }


    /**
     * 清除用户缓存token
     * @param userId
     * @return
     */
    public static String clearToken(String userId){
        String old = (String) CacheUtil.hget(MyConstance.KEY_TOKEN_MAP, userId);
        CacheUtil.hdel(MyConstance.KEY_TOKEN_MAP, userId);
        return old;
    }

    /**
     * 清除用户缓存token-APP
     * @param userId
     * @return
     */
    public static String clearTokenAPP(String userId){
        return clearToken(userId+USER_ID_SUFFIX_APP);
    }


    /**
     * 清除用户缓存token-WEB
     * @param userId
     * @return
     */
    public static String clearTokenWEB(String userId){
        return clearToken(userId+ USER_ID_SUFFIX_WEB);
    }


    /**
     * 更新用户token
     * @param userId
     * @param value
     */
    public static void updateToken(String userId,String value){
        clearToken(userId);
        CacheUtil.hset(MyConstance.KEY_TOKEN_MAP, userId, value);

    }



    /**
     * 更新用户token-APP
     * @param userId
     * @param value
     */
    public static void updateTokenAPP(String userId,String value){
        updateToken(userId+USER_ID_SUFFIX_APP,value);
    }


    /**
     * 更新用户token-WEB
     * @param userId
     * @param value
     */
    public static void updateTokenWEB(String userId,String value){
        updateToken(userId+ USER_ID_SUFFIX_WEB,value);
    }





}
