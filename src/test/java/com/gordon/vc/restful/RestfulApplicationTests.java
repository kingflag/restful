package com.gordon.vc.restful;

import com.gordon.vc.restful.request.UserInfoRequestHeader;
import com.gordon.vc.restful.request.UserResourceRequest;
import com.gordon.vc.restful.request.enums.Role;
import com.gordon.vc.restful.response.PackResp;
import com.gordon.vc.restful.util.JsonUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestfulApplicationTests {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void contextLoads() {
    }

    private String baseUrl = "http://127.0.0.1:8080/";

    @Test
    void addUserTestSuccess() {
        //request info
        UserResourceRequest userResourceRequest = new UserResourceRequest();
        userResourceRequest.setUserId(10086);
        List<String> endpoint = new ArrayList<>();
        endpoint.add("resource A");
        endpoint.add("resource B");
        endpoint.add("resource C");
        endpoint.add("resource D");
        userResourceRequest.setEndpoint(endpoint);

        //header
        HttpHeaders headers = new HttpHeaders();
        UserInfoRequestHeader requestHeader = new UserInfoRequestHeader();
        requestHeader.setUserId(userResourceRequest.getUserId());
        requestHeader.setAccountName(userResourceRequest.getUserId() + "AccountName");
        requestHeader.setRole(Role.admin);
        byte[] headerDecode = Base64.getEncoder().encode(JsonUtils.toJson(requestHeader).getBytes(StandardCharsets.UTF_8));
        headers.add("userInfoDecode", new String(headerDecode)); // 添加自定义header
        HttpEntity<String> entity = new HttpEntity(userResourceRequest, headers);

        PackResp resp = testRestTemplate.postForEntity(baseUrl + "admin/addUser", entity, PackResp.class).getBody();
        LOGGER.info(JsonUtils.toJson(resp));
        assertEquals(resp.getRespData(), true);
        assertEquals(resp.isSuccess(), true);
    }

    @Test
    void addUserTestHaveNoHeader() {
        //request info
        UserResourceRequest userResourceRequest = new UserResourceRequest();
        userResourceRequest.setUserId(10087);
        List<String> endpoint = new ArrayList<>();
        endpoint.add("resource A");
        endpoint.add("resource B");
        endpoint.add("resource C");
        endpoint.add("resource D");
        userResourceRequest.setEndpoint(endpoint);

        //header
        HttpHeaders headers = new HttpHeaders();
        UserInfoRequestHeader requestHeader = new UserInfoRequestHeader();
        requestHeader.setUserId(userResourceRequest.getUserId());
        requestHeader.setAccountName(userResourceRequest.getUserId() + "AccountName");
        byte[] headerDecode = Base64.getEncoder().encode(JsonUtils.toJson(requestHeader).getBytes(StandardCharsets.UTF_8));
        //headers.add("userInfoDecode", new String(headerDecode)); // 添加自定义header
        HttpEntity<String> entity = new HttpEntity(userResourceRequest, headers);

        PackResp resp = testRestTemplate.postForEntity(baseUrl + "admin/addUser", entity, PackResp.class).getBody();
        LOGGER.info(JsonUtils.toJson(resp));
        assertEquals(resp.getCode(), 500);
        assertEquals(resp.isSuccess(), false);
        assertEquals(resp.getMsg(), "非法请求");
    }

    @Test
    void addUserTestHaveNoRole() {
        //request info
        UserResourceRequest userResourceRequest = new UserResourceRequest();
        userResourceRequest.setUserId(10088);
        List<String> endpoint = new ArrayList<>();
        //没有使用真正的resource api提供的数据
        endpoint.add("resource A");
        endpoint.add("resource B");
        endpoint.add("resource C");
        endpoint.add("resource D");
        userResourceRequest.setEndpoint(endpoint);

        //header
        HttpHeaders headers = new HttpHeaders();
        UserInfoRequestHeader requestHeader = new UserInfoRequestHeader();
        requestHeader.setUserId(userResourceRequest.getUserId());
        requestHeader.setAccountName(userResourceRequest.getUserId() + "AccountName");
        byte[] headerDecode = Base64.getEncoder().encode(JsonUtils.toJson(requestHeader).getBytes(StandardCharsets.UTF_8));
        headers.add("userInfoDecode", new String(headerDecode)); // 添加自定义header
        HttpEntity<String> entity = new HttpEntity(userResourceRequest, headers);

        PackResp resp = testRestTemplate.postForEntity(baseUrl + "admin/addUser", entity, PackResp.class).getBody();
        LOGGER.info(JsonUtils.toJson(resp));
        assertEquals(resp.getCode(), 500);
        assertEquals(resp.isSuccess(), false);
        assertEquals(resp.getMsg(), "未知权限");
    }

    @Test
    void addResourceTestSuccess() {
        //header
        HttpHeaders headers = new HttpHeaders();
        UserInfoRequestHeader requestHeader = new UserInfoRequestHeader();
        requestHeader.setUserId(10010);
        requestHeader.setAccountName(10010 + "AccountName");
        requestHeader.setRole(Role.user);
        byte[] headerDecode = Base64.getEncoder().encode(JsonUtils.toJson(requestHeader).getBytes(StandardCharsets.UTF_8));
        headers.add("userInfoDecode", new String(headerDecode)); // 添加自定义header
        HttpEntity<String> entity = new HttpEntity(headers);
        String resource = System.currentTimeMillis() + "resource";
        PackResp resp = testRestTemplate.postForEntity(baseUrl + "/user/" + resource, entity, PackResp.class).getBody();
        LOGGER.info(JsonUtils.toJson(resp));
        assertEquals(resp.getCode(), 200);
        assertEquals(resp.isSuccess(), true);
        assertEquals(resp.getMsg(), "success");
    }

    @Test
    void addResourceTestWrongRole() {
        //header
        HttpHeaders headers = new HttpHeaders();
        UserInfoRequestHeader requestHeader = new UserInfoRequestHeader();
        requestHeader.setUserId(10010);
        requestHeader.setAccountName(10010 + "AccountName");
        requestHeader.setRole(Role.admin);
        byte[] headerDecode = Base64.getEncoder().encode(JsonUtils.toJson(requestHeader).getBytes(StandardCharsets.UTF_8));
        headers.add("userInfoDecode", new String(headerDecode)); // 添加自定义header
        HttpEntity<String> entity = new HttpEntity(headers);
        String resource = System.currentTimeMillis() + "resource";
        PackResp resp = testRestTemplate.postForEntity(baseUrl + "/user/" + resource, entity, PackResp.class).getBody();
        LOGGER.info(JsonUtils.toJson(resp));
        assertEquals(resp.getCode(), 500);
        assertEquals(resp.isSuccess(), false);
        assertEquals(resp.getMsg(), "用户没有权限");
    }

}
