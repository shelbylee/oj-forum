package org.sduwh.oj.forum.mapper;

import org.apache.ibatis.annotations.*;
import org.sduwh.oj.forum.model.Contest;
import org.sduwh.oj.forum.param.ContestParam;

@Mapper
public interface ContestMapper {

    /** insert **/

    @Insert("INSERT INTO contest (" +
            "    contest_id, discuss_status" +
            ") VALUES (" +
            "    #{contestId}, #{discussStatus}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(Contest contest);

    /** select **/

    @Select("SELECT contest_id, discuss_status FROM contest WHERE contest_id = #{contestId}")
    @Results(id = "contestDiscussStatus", value = {
            @Result(property = "contestId", column = "contest_id"),
            @Result(property = "discussStatus", column = "discuss_status")
    })
    ContestParam select(@Param("contestId") Integer contestId);

    @Select("SELECT discuss_status FROM contest WHERE id = #{id}")
    @Results(
            @Result(property = "discussStatus", column = "discuss_status")
    )
    Integer selectDiscussStatusById(@Param("id") Integer id);

    /** update **/

    @UpdateProvider(type = ContestProvider.class, method = "update")
    int update(ContestParam contest);
}
