package com.springboot.dlc.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.client.RestTemplate;

/**
 * @auther: liujiebang
 * @Date: Create in 2018/11/21
 * @Description:
 **/
public class RestTemplateUtil {


    private RestTemplateUtil() {
    }

    private static class SingletonRestTemplate {
        static final RestTemplate INSTANCE = new RestTemplate();
    }

    public static RestTemplate getInstance() {
        return SingletonRestTemplate.INSTANCE;
    }


    /**
     * 设置ContentType=application/json;charset=UTF-8
     *
     * @param token 认证令牌
     * @return
     */
    private static HttpHeaders headersToJson(String token) {
        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.isNotBlank(token)) {
            headers.add(HttpHeaders.AUTHORIZATION, token);
        }
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

    /**
     * 设置ContentType=application/x-www-form-urlencoded
     *
     * @param token 认证令牌
     * @return
     */
    private static HttpHeaders headersToFrom(String token) {
        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.isNotBlank(token)) {
            headers.add(HttpHeaders.AUTHORIZATION, token);
        }
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    /**
     * post请求
     *
     * @param url  请求地址
     * @param json 请求json参数
     * @return
     */
    public static String post(String url, String json) {
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headersToJson(null));
        return RestTemplateUtil.getInstance().postForObject(url, requestEntity, String.class);
    }

    /**
     * post请求
     *
     * @param url   请求地址
     * @param json  请求json参数
     * @param token 授权令牌
     * @return
     */
    public static String post(String url, String json, String token) {
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headersToJson(token));
        return RestTemplateUtil.getInstance().postForObject(url, requestEntity, String.class);
    }

    /**
     * post请求
     *
     * @param url      请求地址
     * @param valueMap key-value参数
     * @return
     */
    public static String post(String url, MultiValueMap valueMap) {
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(valueMap, headersToFrom(null));
        return RestTemplateUtil.getInstance().postForObject(url, httpEntity, String.class);
    }

    /**
     * post请求
     *
     * @param url      请求地址
     * @param valueMap key-value参数
     * @param token    授权参数
     * @return
     */
    public static String post(String url, MultiValueMap valueMap, String token) {
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(valueMap, headersToFrom(token));
        return RestTemplateUtil.getInstance().postForObject(url, httpEntity, String.class);
    }

    /**
     * get请求
     *
     * @param url 请求地址
     * @return
     */
    public static String get(String url) {
        return RestTemplateUtil.getInstance().getForObject(url, String.class, new Object[]{});
    }

    /**
     * get请求
     *
     * @param url      请求地址
     * @param valueMap key-value参数
     * @return
     */
    public static String get(String url, Map<String, Object> valueMap) {
        return RestTemplateUtil.getInstance().getForObject(url + mapToString(valueMap), String.class, new Object[]{});
    }

    /**
     * get请求
     *
     * @param url      请求地址
     * @param valueMap key-value参数
     * @return
     */
    public static String get(String url, Map<String, Object> valueMap, String token) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headersToFrom(token));
        ResponseEntity<String> response = RestTemplateUtil.getInstance().exchange(url + mapToString(valueMap), HttpMethod.GET, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * get请求
     *
     * @param url   请求地址
     * @param token 授权令牌
     * @return
     */
    public static String get(String url, String token) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headersToFrom(token));
        ResponseEntity<String> response = RestTemplateUtil.getInstance().exchange(url, HttpMethod.GET, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * put请求
     *
     * @param url 请求地址
     * @return
     */
    public static String put(String url) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headersToFrom(null));
        ResponseEntity<String> response = RestTemplateUtil.getInstance().exchange(url, HttpMethod.PUT, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * put请求
     *
     * @param url 请求地址
     * @return
     */
    public static String put(String url, String token) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headersToFrom(token));
        ResponseEntity<String> response = RestTemplateUtil.getInstance().exchange(url, HttpMethod.PUT, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * put请求
     *
     * @param url      请求地址
     * @param valueMap 请求参数
     * @return
     */
    public static String put(String url, Map<String, Object> valueMap) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headersToFrom(null));
        ResponseEntity<String> response = RestTemplateUtil.getInstance().exchange(url + mapToString(valueMap), HttpMethod.PUT, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * put请求
     *
     * @param url      请求地址
     * @param valueMap 请求参数
     * @param token    授权令牌
     * @return
     */
    public static String put(String url, Map<String, Object> valueMap, String token) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headersToFrom(token));
        ResponseEntity<String> response = RestTemplateUtil.getInstance().exchange(url + mapToString(valueMap), HttpMethod.PUT, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * delete请求
     *
     * @param url 请求地址
     * @return
     */
    public static String delete(String url) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headersToFrom(null));
        ResponseEntity<String> response = RestTemplateUtil.getInstance().exchange(url, HttpMethod.PUT, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * delete请求
     *
     * @param url   请求地址
     * @param token 授权token
     * @return
     */
    public static String delete(String url, String token) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headersToFrom(token));
        ResponseEntity<String> response = RestTemplateUtil.getInstance().exchange(url, HttpMethod.PUT, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * delete请求
     *
     * @param url      请求地址
     * @param valueMap 请求参数
     * @return
     */
    public static String delete(String url, Map<String, Object> valueMap) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headersToFrom(null));
        ResponseEntity<String> response = RestTemplateUtil.getInstance().exchange(url + mapToString(valueMap), HttpMethod.PUT, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * delete请求
     *
     * @param url      请求地址
     * @param valueMap 请求参数
     * @param token    授权令牌
     * @return
     */
    public static String delete(String url, Map<String, Object> valueMap, String token) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headersToFrom(token));
        ResponseEntity<String> response = RestTemplateUtil.getInstance().exchange(url + mapToString(valueMap), HttpMethod.PUT, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * 将Map转换成?key=value&...
     *
     * @param valueMap 要转换的map
     * @return
     */
    public static String mapToString(Map<String, Object> valueMap) {
        if (Optional.ofNullable(valueMap).isPresent()) {
            StringBuilder sb = new StringBuilder("?");
            for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                sb.append(key + "=" + value + "&");
            }
            int length = sb.lastIndexOf("&");
            return sb.substring(0, length == -1 ? 0 : length);
        }
        return "";
    }
}
