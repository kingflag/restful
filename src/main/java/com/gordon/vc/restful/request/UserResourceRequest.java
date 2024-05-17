package com.gordon.vc.restful.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UserResourceRequest {

    @NotNull(message = "userId cannot be null")
    private Integer userId;

    private List<String> endpoint;
}
