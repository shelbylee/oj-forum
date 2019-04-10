package org.sduwh.oj.forum.mapper;

import org.apache.ibatis.jdbc.SQL;
import org.sduwh.oj.forum.model.Comment;
import org.springframework.util.StringUtils;

public class CommentProvider extends SQL {

    private static final String TABLE_NAME = "comment";

    public String update(final Comment comment) {

        return new SQL() {{

            UPDATE(TABLE_NAME);

            if (!StringUtils.isEmpty(comment.getContent())) {
                SET("content= #{content}");
            }
            if (!StringUtils.isEmpty(comment.getUpdatedAt())) {
                SET("modify_time= #{updatedAt}");
            }
            if (!StringUtils.isEmpty(comment.getUpIds())) {
                SET("up_ids= #{upIds}");
            }
            if (!StringUtils.isEmpty(comment.getCommentId())) {
                SET("comment_id= #{commentId}");
            }
            WHERE("id = #{id}");
        }}.toString();
    }

}
