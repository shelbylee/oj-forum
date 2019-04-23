package org.sduwh.oj.forum.mapper;

import org.apache.ibatis.jdbc.SQL;
import org.sduwh.oj.forum.param.ContestParam;
import org.springframework.util.StringUtils;

public class ContestProvider extends SQL {

    private static final String TABLE_NAME = "contest";

    public String update(final ContestParam contest) {

        return new SQL() {{

            UPDATE(TABLE_NAME);

            if (!StringUtils.isEmpty(contest.getDiscussStatus())) {
                SET("discuss_status= #{discussStatus}");
            }
            WHERE("contest_id = #{contestId}");
        }}.toString();
    }
}
