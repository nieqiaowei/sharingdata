package com.palmgo.com.cn.sharingdata.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName TokenUtils
 * @Description TODO
 * @Author liuxinxin
 * @Date 2020/9/8 下午 04:58
 * @Version 1.0
 */
public class TokenUtils {

    private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);

    private static final long EXPIRE_DATE = 7 * 24 * 60 * 60 * 1000;
    //token秘钥
    private static final String TOKEN_SECRET = "ZCfasfhuaUUHufguGuwu2020BQWE";

    public static String token (String username,String password){

        String token = "";
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis()+EXPIRE_DATE);
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String,Object> header = new HashMap<>();
            header.put("typ","JWT");
            header.put("alg","HS256");
            //携带username，password信息，生成签名
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("username",username)
                    .withClaim("password",password).withExpiresAt(date)
                    .sign(algorithm);
        }catch (Exception e){
            logger.error(e.getLocalizedMessage(),e);
            return  null;
        }
        return token;
    }

    public static DecodedJWT verify(String token){
        /**
         * @desc   验证token，通过返回true
         * @params [token]需要校验的串
         **/
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            Date d2 = jwt.getExpiresAt();
            logger.info("超时时间:" + sdf.format(d2));
            // 可以通过jwt输出载体信息
            logger.info("载体信息用户名:"+jwt.getClaim("username").asString());
            logger.info("载体信息密码:"+jwt.getClaim("password").asString());
            //logger.info("算法:"+jwt.getAlgorithm());
            return jwt;
        }catch (Exception e){
            return null;
        }
    }
    
    /**
     * 获取token中的属性
     * @param request
     * @return
     */
    public static DecodedJWT getToken(HttpServletRequest request) throws Exception{
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        String token = request.getHeader("Authorization");
        return verifier.verify(token);
    }
    
    
    public static void main(String[] args) throws Exception {
    	String token = token("nqw", "123456");
    	System.out.println(token);
    	verify(token);
	}

}
