package org.sduwh.oj.forum.mapper;

import org.apache.ibatis.annotations.*;
import org.sduwh.oj.forum.model.Comment;
import org.sduwh.oj.forum.param.CommentParam;

import java.util.List;

@Mapper
public interface CommentMapper {

    Comment selectById(@Param("id") Integer id);

    @Select("SELECT * FROM comment WHERE topic_id = #{topicId}")
    @Results(id = "commentsByTopicId", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "content", column = "content"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "createdAt", column = "create_time"),
            @Result(property = "updatedAt", column = "modify_time"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "upIds", column = "up_ids"),
    })
    List<CommentParam> selectByTopicId(@Param("topicId") Integer topicId);

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
