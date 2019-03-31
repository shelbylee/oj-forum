package org.sduwh.oj.forum.mapper;

import org.apache.ibatis.jdbc.SQL;
import org.sduwh.oj.forum.model.Topic;
import org.springframework.util.StringUtils;

public class TopicProvider extends SQL {

    private static final String TABLE_NAME = "topic";

    public String update(final Topic topic) {
        return new SQL() {{
            UPDATE(TABLE_NAME);

            if (!StringUtils.isEmpty(topic.getTitle())) {
                SET("title = #{title}");
            }
            if (!StringUtils.isEmpty(topic.getContent())) {
                SET("content= #{content}");
            }
            if (!StringUtils.isEmpty(topic.getUpdatedAt())) {
                SET("modify_time= #{updatedAt}");
            }
            if (!StringUtils.isEmpty(topic.getUpIds())) {
                SET("up_ids= #{upIds}");
            }
            if (!StringUtils.isEmpty(topic.getViewCount())) {
                SET("view_count= #{viewCount}");
            }
            WHERE("id = #{id}");
        }}.toString();
    }

}
