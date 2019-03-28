package org.sduwh.oj.forum.param;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OjData {
    private String token;
    private Integer userId;
    private String userName;
    private String userType;
}
