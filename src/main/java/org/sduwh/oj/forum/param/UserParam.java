package org.sduwh.oj.forum.param;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserParam {

    private Integer userId;

    private String userName;

    private String userType;

}
