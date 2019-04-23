package org.sduwh.oj.forum.mapper;

import org.apache.ibatis.annotations.*;
import org.sduwh.oj.forum.model.Topic;

import java.util.List;

@Mapper
public interface TopicMapper {

    /** select **/

    @Select("SELECT * FROM topic WHERE id = #{id}")
    @Results(id = "topic", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "content", column = "content"),
            @Result(property = "createdAt", column = "create_time"),
            @Result(property = "updatedAt", column = "modify_time"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "commentCount", column = "comment_count"),
            @Result(property = "upIds", column = "up_ids"),
            @Result(property = "likeCount", column = "like_count"),
            @Result(property = "viewCount", column = "view_count"),
            @Result(property = "contestId", column = "contest_id"),
            @Result(property = "problemId", column = "problem_id"),
            @Result(property = "contestCreatorId", column = "contest_creator_id"),
    })
    Topic selectById(@Param("id") Integer id);

    @Select("SELECT up_ids FROM topic WHERE id = #{id}")
    @Results(
            @Result(property = "up_ids", column = "up_ids")
    )
    String selectUpIds(@Param("id") Integer id);

    @Select("SELECT * FROM topic ")
    @ResultMap("topic")
    List<Topic> selectAll();

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId} and contest_id = #{contestId}")
    @ResultMap("topic")
    List<Topic> selectByProblemContestId(@Param("contestId") Integer contestId, @Param("problemId") Integer problemId);

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId}")
    @ResultMap("topic")
    List<Topic> selectByProblemId(@Param("problemId") Integer problemId);

    @Select("SELECT * FROM topic WHERE problem_id is null and ( title like #{keywords} or content like #{keywords})")
    @ResultMap("topic")
    List<Topic> searchByKeywords(@Param("keywords") String keywords);

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId} and ( title like #{keywords} or content like #{keywords})")
    @ResultMap("topic")
    List<Topic> searchByKeywordsWithProblemId(@Param("keywords") String keywords, @Param("problemId") Integer problemId);

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId} and ( title like #{keywords} or content like #{keywords})")
    @ResultMap("topic")
    List<Topic> searchByKeywordsWithProblemAndContestId(@Param("keywords") String keywords, @Param("problemId") Integer problemId, @Param("contestId") Integer contestId);

    @Select("SELECT view_count FROM topic WHERE id = #{id}")
    @Results(
            @Result(property = "view_count", column = "view_count")
    )
    Integer getViewCount(@Param("id") Integer id);

    /** insert **/

    @Insert("INSERT INTO topic (" +
            "    title, content, create_time, user_id, comment_count, problem_id, contest_id, contest_creator_id" +
            ") VALUES (" +
            "    #{title}, #{content}, #{createdAt}, #{userId}, #{commentCount}, #{problemId}, #{contestId}, #{contestCreatorId}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(Topic topic);

    /** update **/

    @UpdateProvider(type = TopicProvider.class, method = "update")
    int update(Topic topic);

    /** delete **/

    @Delete("DELETE FROM topic" +
            " WHERE" +
            "    id = #{id}")
    void deleteById(@Param("id") Integer id);

    /** contest problem topic sort **/

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId} and contest_id = #{contestId} ORDER BY create_time DESC")
    @ResultMap("topic")
    List<Topic> sortByCreateTimeDESCWithContestAndProblemId(@Param("problemId") Integer problemId, @Param("contestId") Integer contestId);

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId} and contest_id = #{contestId} ORDER BY create_time ASC")
    @ResultMap("topic")
    List<Topic> sortByCreateTimeASCWithContestAndProblemId(@Param("problemId") Integer problemId, @Param("contestId") Integer contestId);

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId} and contest_id = #{contestId} ORDER BY like_count DESC")
    @ResultMap("topic")
    List<Topic> sortByVotesDESCWithContestAndProblemId(@Param("problemId") Integer problemId, @Param("contestId") Integer contestId);

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId} and contest_id = #{contestId} ORDER BY like_count ASC")
    @ResultMap("topic")
    List<Topic> sortByVotesASCWithContestAndProblemId(@Param("problemId") Integer problemId, @Param("contestId") Integer contestId);

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId} and contest_id = #{contestId} ORDER BY comment_count DESC")
    @ResultMap("topic")
    List<Topic> sortByPostsDESCWithContestAndProblemId(@Param("problemId") Integer problemId, @Param("contestId") Integer contestId);

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId} and contest_id = #{contestId} ORDER BY comment_count ASC")
    @ResultMap("topic")
    List<Topic> sortByPostsASCWithContestAndProblemId(@Param("problemId") Integer problemId, @Param("contestId") Integer contestId);

    /** problem topic sort **/

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId} ORDER BY create_time DESC")
    @ResultMap("topic")
    List<Topic> sortByCreateTimeDESCWithProblemId(@Param("problemId") Integer problemId);

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId} ORDER BY create_time ASC")
    @ResultMap("topic")
    List<Topic> sortByCreateTimeASCWithProblemId(@Param("problemId") Integer problemId);

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId} ORDER BY like_count DESC")
    @ResultMap("topic")
    List<Topic> sortByVotesDESCWithProblemId(@Param("problemId") Integer problemId);

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId} ORDER BY like_count ASC")
    @ResultMap("topic")
    List<Topic> sortByVotesASCWithProblemId(@Param("problemId") Integer problemId);

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId} ORDER BY comment_count DESC")
    @ResultMap("topic")
    List<Topic> sortByPostsDESCWithProblemId(@Param("problemId") Integer problemId);

    @Select("SELECT * FROM topic WHERE problem_id = #{problemId} ORDER BY comment_count ASC")
    @ResultMap("topic")
    List<Topic> sortByPostsASCWithProblemId(@Param("problemId") Integer problemId);

    /** topic sort **/

    @Select("SELECT * FROM topic WHERE problem_id is null and contest_id is null ORDER BY create_time DESC")
    @ResultMap("topic")
    List<Topic> sortByCreateTimeDESC();

    @Select("SELECT * FROM topic WHERE problem_id is null and contest_id is null ORDER BY create_time ASC")
    @ResultMap("topic")
    List<Topic> sortByCreateTimeASC();

    @Select("SELECT * FROM topic WHERE problem_id is null and contest_id is null ORDER BY like_count DESC")
    @ResultMap("topic")
    List<Topic> sortByVotesDESC();

    @Select("SELECT * FROM topic WHERE problem_id is null and contest_id is null ORDER BY like_count ASC")
    @ResultMap("topic")
    List<Topic> sortByVotesASC();

    @Select("SELECT * FROM topic WHERE problem_id is null and contest_id is null ORDER BY comment_count DESC")
    @ResultMap("topic")
    List<Topic> sortByPostsDESC();

    @Select("SELECT * FROM topic WHERE problem_id is null and contest_id is null ORDER BY comment_count ASC")
    @ResultMap("topic")
    List<Topic> sortByPostsASC();

}
