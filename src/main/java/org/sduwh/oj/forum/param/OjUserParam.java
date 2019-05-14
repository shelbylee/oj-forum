package org.sduwh.oj.forum.param;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OjUserParam extends OjResponseParam {

    private OjData data;

    @Data
    @ToString
    public static class OjData {
        private String token;
        private Integer userId;
        private String avatar;
        private String username;
        private String admin_type;
    }
}
