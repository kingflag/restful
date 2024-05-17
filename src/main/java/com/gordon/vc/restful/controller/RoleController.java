package com.gordon.vc.restful.controller;

import com.gordon.vc.restful.advice.PreAuthorize;
import com.gordon.vc.restful.request.UserResourceRequest;

import com.gordon.vc.restful.response.PackResp;
import com.gordon.vc.restful.service.FileService;
import com.gordon.vc.restful.util.JsonUtils;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
public class RoleController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("classpath:templates/resource.txt")
    private Resource resourceFile;

    @Value("classpath:templates/assignResource.txt")
    private Resource assignResourceFile;

    @Autowired
    private FileService fileService;

    @PreAuthorize("admin")
    @PostMapping("/admin/addUser")
    public PackResp addUser(@RequestBody @Valid UserResourceRequest request) {
        LOGGER.debug("userId:{} request body :{}", request.getUserId(), JsonUtils.toJson(request));
        try {
            Map<Integer, UserResourceRequest> assignRes = null;
            String assignResourceContent = fileService.readFile(assignResourceFile);
            if (assignResourceContent.isEmpty()) {
                assignRes = new HashMap<>();
                assignRes.put(request.getUserId(), request);
            } else {
                assignRes = JsonUtils.parse(assignResourceContent, Map.class);
                assignRes.put(request.getUserId(), request);
            }
            fileService.write(assignResourceFile, JsonUtils.toJson(assignRes));
        } catch (Exception e) {
            LOGGER.error("userId:{} Exception :{}", request.getUserId(), e.getMessage());
            return PackResp.fail(e.getMessage());
        }
        return PackResp.success(true);
    }

    @PreAuthorize("user")
    @PostMapping("/user/{resource}")
    public PackResp addResource(@PathVariable("resource") String resource) {
        try {
            List<String> resourceList = null;
            String resourceContent = fileService.readFile(resourceFile);
            if (resourceContent.isEmpty()) {
                resourceList = new ArrayList<>();
                resourceList.add(resource);
            } else {
                resourceList = JsonUtils.parse(resourceContent, List.class);
                resourceList.add(resource);
            }
            fileService.write(resourceFile, JsonUtils.toJson(resourceList));
        } catch (Exception e) {
            LOGGER.error("resource:{} Exception :{}", resource, e.getMessage());
            return PackResp.fail(e.getMessage());
        }
        return PackResp.success(true);
    }
}
