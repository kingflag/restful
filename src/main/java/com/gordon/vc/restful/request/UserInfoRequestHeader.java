package com.gordon.vc.restful.request;

import com.gordon.vc.restful.request.enums.Role;
import lombok.Data;

@Data
public class UserInfoRequestHeader {
    private Integer userId;
    private String accountName;
    private Role role;
}
