package org.sduwh.oj.forum.mapper;

import org.apache.ibatis.annotations.*;
import org.sduwh.oj.forum.model.Topic;

@Mapper
public interface TopicMapper {

    @Select("SELECT * FROM topic WHERE id = #{id}")
    @Results(id = "topic", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "content", column = "title"),
            @Result(property = "createdAt", column = "create_time"),
            @Result(property = "updatedAt", column = "modify_time"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "commentCount", column = "comment_count"),
            @Result(property = "upIds", column = "up_ids"),
            @Result(property = "likeCount", column = "like_count"),
            @Result(property = "viewCount", column = "view_count"),
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Topic selectById(@Param("id") Integer id);

    @Insert("INSERT INTO topic (" +
            "    title, content, create_time, user_id, comment_count, problem_id, contest_id" +
            ") VALUES (" +
            "    #{title}, #{content}, #{createdAt}, #{userId}, #{commentCount}, #{problemId}, #{contestId}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(Topic topic);

    @UpdateProvider(type = TopicProvider.class, method = "update")
    int update(Topic topic);

    @Delete("DELETE FROM topic" +
            " WHERE" +
            "    id = #{id}")
    void deleteById(@Param("id") Integer id);
}
