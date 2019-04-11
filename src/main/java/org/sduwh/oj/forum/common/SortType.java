package org.sduwh.oj.forum.common;

public enum SortType {

    // 最新发表
    CREATE_TIME_DESC(0),
    // 最旧发表
    CREATE_TIME_ASC(1),
    // 投票最多
    VOTES_DESC(2),
    // 投票最少
    VOTES_ASC(3),
    // 评论最多
    POSTS_DESC(4),
    // 评论最少
    POSTS_ASC(5);

    private int index;

    SortType(int idx) {
        this.index = idx;
    }

    public Integer getIdx() {
        return this.index;
    }
}
