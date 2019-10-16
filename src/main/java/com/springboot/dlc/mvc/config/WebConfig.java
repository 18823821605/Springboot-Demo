package com.springboot.dlc.mvc.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.springboot.dlc.common.utils.DateUtils;
import com.springboot.dlc.mvc.handler.BackAuthenticationInterceptor;
import com.springboot.dlc.mvc.handler.BackAuthorizationInterceptor;
import com.springboot.dlc.mvc.handler.FrontAuthenticationInterceptor;
import com.springboot.dlc.mvc.resources.InterceptorResource;
import com.springboot.dlc.mvc.resources.WebResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: liujiebang
 * @Description: 过滤器与拦截器配置
 * @Date: 2018/6/27 15:05
 **/
@Slf4j
@Configuration
/**会覆盖@EnableAutoConfiguration关于WebMvcAutoConfiguration的配置 */
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private WebResource webResource;
    @Autowired
    private InterceptorResource interceptorResource;


    /**
     * 拦截器注册
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("授权拦截路径加载成功:{}", interceptorResource.getAdminAuthenticationAddPathPatterns());
        registry.addInterceptor(frontAuthenticationInterceptor())
                .addPathPatterns(interceptorResource.getFrontAuthenticationAddPathPatterns())
                .excludePathPatterns(interceptorResource.getFrontAuthenticationExcludePathPatterns());

        registry.addInterceptor(backAuthenticationInterceptor())
                .addPathPatterns(interceptorResource.getAdminAuthenticationAddPathPatterns())
                .excludePathPatterns(interceptorResource.getAdminAuthenticationExcludePathPatterns());

        registry.addInterceptor(backAuthorizationInterceptor())
                .addPathPatterns(interceptorResource.getAdminAuthorizationAddPathPatterns())
                .excludePathPatterns(interceptorResource.getAdminAuthorizationExcludePathPatterns());

    }

    /**
     * 静态资源处理
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/**").addResourceLocations("file:" + webResource.getStaticResourcePath());

    }

    /**
     * 欢迎页请求重定向
     * --可用来处理微信公众平台或者开放平台获取code操作
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        log.info("静态资源加载成功:{}", webResource.getProjectPath());
        registry.addRedirectViewController("/", webResource.getProjectPath() + webResource.getWelcomePath());

    }

    /**
     * 多日期格式转换为Date类型
     *
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<String, Date>() {
            @Nullable
            @Override
            public Date convert(String s) {
                if ("".equals(s) || s == null) {
                    return null;
                }
                if (s.matches("^\\d{4}-\\d{1,2}$")) {
                    return DateUtils.stringToDate(s, DateUtils.formarts.get(0));
                } else if (s.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
                    return DateUtils.stringToDate(s, DateUtils.formarts.get(1));
                } else if (s.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
                    return DateUtils.stringToDate(s, DateUtils.formarts.get(2));
                } else if (s.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                    return DateUtils.stringToDate(s, DateUtils.formarts.get(3));
                } else if (s.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                    return DateUtils.stringToDate(s, DateUtils.formarts.get(4));
                } else if (s.matches("^\\d{4}年\\d{1,2}月\\d{1,2}日 {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                    return DateUtils.stringToDate(s, DateUtils.formarts.get(5));
                } else {
                    throw new IllegalArgumentException("Invalid boolean value '" + s + "'");
                }
            }
        });
    }

    /**
     * 设置HTTP请求报文字符串UTF-8编码格式 消息转换器替换为fastJson
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        // 创建配置类
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty
        );
        //此处是全局处理方式
        config.setDateFormat(DateUtils.DATE_TIME_FORMAT);
        config.setCharset(Charset.forName("UTF-8"));
        fastConverter.setFastJsonConfig(config);
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        fastConverter.setSupportedMediaTypes(supportedMediaTypes);
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converters.add(fastConverter);
        converters.add(stringHttpMessageConverter);
    }

    /**
     * 拦截器级别的跨域配置
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(CorsConfiguration.ALL)
                .allowedMethods(CorsConfiguration.ALL)
                .allowCredentials(true)
                .maxAge(3600);
    }

//    /**
//     * 不覆盖默认配置的情况下:设置HTTP请求报文字符串UTF-8编码格式 消息转换器替换为fastJson
//     *
//     * @return
//     */
//    @Bean
//    public HttpMessageConverters customConverters() {
//        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//        // 创建配置类
//        FastJsonConfig config = new FastJsonConfig();
//        config.setSerializerFeatures(
//                SerializerFeature.WriteDateUseDateFormat,
//                SerializerFeature.WriteNullListAsEmpty,
//                SerializerFeature.WriteMapNullValue,
//                SerializerFeature.WriteNullStringAsEmpty
//        );
//        //全局配置处理方式
//        config.setDateFormat(DateUtils.DATE_TIME_FORMAT);
//        config.setCharset(Charset.forName("UTF-8"));
//        fastConverter.setFastJsonConfig(config);
//        List<MediaType> supportedMediaTypes = new ArrayList<>();
//        supportedMediaTypes.add(MediaType.ALL);
//        fastConverter.setSupportedMediaTypes(supportedMediaTypes);
//        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
//        return new HttpMessageConverters(fastConverter, stringHttpMessageConverter);
//    }


    @Bean
    FrontAuthenticationInterceptor frontAuthenticationInterceptor() {
        return new FrontAuthenticationInterceptor();
    }


    @Bean
    BackAuthenticationInterceptor backAuthenticationInterceptor() {
        return new BackAuthenticationInterceptor();
    }


    @Bean
    BackAuthorizationInterceptor backAuthorizationInterceptor() {
        return new BackAuthorizationInterceptor();
    }


}
