package com.oxchains.controller;

import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.TabToken;
import com.oxchains.common.ConstantsData;
import com.oxchains.dao.TabTokenDao;
import com.oxchains.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.hibernate.result.Output;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by root on 17-7-11.
 */
@Configuration
@Slf4j
public class WebSecurityConfig extends WebMvcConfigurerAdapter {

    @Resource
    private TabTokenDao tabTokenDao;

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 排除配置
        addInterceptor.excludePathPatterns("/user/login")
                .excludePathPatterns("/datav/**")
                .excludePathPatterns("/healthz");

        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            String rtn = "";
            String token = request.getParameter("Token" );
            log.debug("===token=="+token);
            if(!StringUtils.isEmpty(token)){
                JwtToken jwt = TokenUtils.parseToken(token);
                Date expire = jwt.getExpiratioin();
                Date now = new Date();
                if (expire.before(now)) {// expired
                    rtn = "{\"status\":\""+ConstantsData.RTN_LOGIN_EXPIRED+"\",\"message\":\"操作失败\"}";
                }
                //unlogin
                TabToken tabToken = tabTokenDao.findByUsername(jwt.getId());
                if(tabToken != null){
                    if(!token.equals(tabToken.getToken())){
                        log.debug("===rtn=="+ConstantsData.RTN_UNLOGIN);
                        rtn = "{\"status\":\""+ConstantsData.RTN_UNLOGIN+"\",\"message\":\"操作失败\"}";
                    }
                }
                else{
                    log.debug("===rtn=="+ConstantsData.RTN_UNLOGIN);
                    rtn = "{\"status\":\""+ConstantsData.RTN_UNLOGIN+"\",\"message\":\"操作失败\"}";
                }
                if(StringUtils.isEmpty(rtn)){
                    rtn = "{\"status\":\"0\",\"message\":\"操作成功\"}";
                    return true;
                }

            }
            if(StringUtils.isEmpty(rtn)){
                rtn = "{\"status\":\""+ConstantsData.RTN_LOGIN_EXPIRED+"\",\"message\":\"操作失败\"}";
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.append(rtn);
                log.debug(rtn);
            } catch (IOException e) {
                log.error(e.getMessage());
            } finally {
                if (out != null) {
                    out.close();
                }
            }
            return false;
        }
    }
}
