package org.sduwh.oj.forum.param;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OjContestParam {

    private OjData data;

    @Data
    @ToString
    public static class OjData {
        private Integer id;
        private CreatorData created_by;
        private Integer status;
        private String contest_type;
        private String title;
        private String description;

        @Data
        @ToString
        public static class CreatorData {
            private Integer id;
            private String username;
            private String real_name;
        }
    }
}
