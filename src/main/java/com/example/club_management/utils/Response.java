package com.example.club_management.utils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一生成的响应类
 *
 * @author allen
 * @date 2023/10/01
 *///生成getter and setter
@Data
//生成equals and hashcode
@EqualsAndHashCode(callSuper = false)
//允许生成的getter and setter链式调用
@Accessors(chain = true)
public class Response {
    private boolean success;
    //状态码
    private Integer code;
    private String message;
    private Map<String,Object> data = new HashMap<String,Object>();

    //把构造方法私有
    private Response(){};
    public Response data(String string, Object object){
        this.data.put(string,object);
        return this;
    }
    public Response data(Map<String,Object> map){
        this.setData(map);
        return this;
    }


    /**
     * 成功响应
     * 默认使用20000状态码
     * */
    public static Response ok(){
        Response r = new Response();
        return r.setSuccess(true)
                .setCode(ResponseCode.LOGIN_SUCCESS)
                .setMessage("成功");
    }

    /**
     * 成功响应
     * @param message 自定义提示信息
     * */
    public static Response ok(String message){//自定义
        Response r = new Response();
        return r.setSuccess(true)
                .setCode(ResponseCode.OK)
                .setMessage(message);
    }

    /**
     * 成功响应
     * @param code 自定义返回状态码
     * */
    public static Response ok(int code){
        Response r = new Response();
        return r.setSuccess(true)
                .setCode(code)
                .setMessage("成功");
    }

    /**
     * 成功响应
     * @param code 自定义返回状态码
     * @param message 自定义返回消息
     * */
    public static Response ok(int code,String message){
        Response r = new Response();
        return r.setSuccess(true)
                .setCode(code)
                .setMessage(message);
    }
    /**
     * 失败响应
     * 默认返回20004
     * */
    public static Response failure() {
        return new Response()
                .setSuccess(false)
                .setCode(ResponseCode.LOGIN_ERROR)
                .setMessage("失败");
    }

    // 自定义消息的失败响应
    public static Response failure(String message) {
        return new Response()
                .setSuccess(false)
                .setCode(ResponseCode.LOGIN_ERROR)
                .setMessage(message);
    }

    // 自定义状态码和消息的失败响应
    public static Response failure(int code, String message) {
        return new Response()
                .setSuccess(false)
                .setCode(code)
                .setMessage(message);
    }


}
