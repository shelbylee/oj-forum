package org.sduwh.oj.forum.util;

import org.sduwh.oj.forum.filter.WordFilter;
import org.sduwh.oj.forum.model.Comment;
import org.sduwh.oj.forum.model.Topic;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("sensitiveWordsFilterUtil")
public class SensitiveWordsFilterUtil {

    @Resource
    private WordFilter wordFilter;

    public Topic filterTopic(Topic topic) {
        String title = topic.getTitle();
        String content = topic.getContent();
        if (wordFilter.isContains(title)) {
            title = wordFilter.doFilter(title);
        } else if (wordFilter.isContains(content)) {
            content = wordFilter.doFilter(content);
        }
        topic.setTitle(title);
        topic.setContent(content);
        return topic;
    }

    public Comment filterComment(Comment comment) {
        String content = comment.getContent();
        if (wordFilter.isContains(content)) {
            content = wordFilter.doFilter(content);
        }
        comment.setContent(content);
        return comment;
    }

}
