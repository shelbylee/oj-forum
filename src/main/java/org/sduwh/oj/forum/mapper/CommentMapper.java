package org.sduwh.oj.forum.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.sduwh.oj.forum.model.Comment;

@Mapper
public interface CommentMapper {

    Comment selectById(@Param("id") Integer id);

    @Insert("INSERT INTO comment (" +
            "    content, topic_id, create_time, user_id" +
            ") VALUES (" +
            "    #{content}, #{topicId}, #{createdAt}, #{userId}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(Comment comment);

    void update(Comment comment);

    void deleteById(@Param("id") Integer id);
}
