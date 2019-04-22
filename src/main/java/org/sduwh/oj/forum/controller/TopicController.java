package org.sduwh.oj.forum.controller;

import lombok.extern.slf4j.Slf4j;
import org.sduwh.oj.forum.common.ResultMessage;
import org.sduwh.oj.forum.model.Topic;
import org.sduwh.oj.forum.param.TopicParam;
import org.sduwh.oj.forum.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    // 查询话题
    @GetMapping("/{id}")
    public ResultMessage select(@PathVariable("id") Integer id) {
        TopicParam topic = topicService.getTopicById(id);
        return ResultMessage.success(topic);
    }

    // 创建话题
    @PostMapping()
    public ResultMessage create(@RequestBody TopicParam param) {
        Topic topic = topicService.saveTopic(param);
        return ResultMessage.success(topic);
    }

    // 编辑话题
    @PutMapping()
    public ResultMessage edit(@RequestBody TopicParam param) {
        Topic topic = topicService.editTopicById(param);
        return ResultMessage.success(topic);
    }

    // 删除话题
    @DeleteMapping()
    public ResultMessage delete(@RequestParam Integer id) {
        topicService.deleteTopicById(id);
        return ResultMessage.success();
    }

    // 点赞话题
    @GetMapping("/vote")
    public ResultMessage vote(@RequestParam Integer id) {
        Integer likeCount = topicService.vote(id);
        return ResultMessage.success(likeCount);
    }

    // 判断该用户是否点赞过该话题
    @GetMapping("/judge-vote")
    public ResultMessage judgeVote(@RequestParam Integer id) {
        Boolean result = topicService.ifUserHadVoted(id);
        return ResultMessage.success(result);
    }

    // 浏览量
    @GetMapping("/view-count")
    public ResultMessage view(@RequestParam Integer id) {
        Integer viewCount = topicService.getViewCount(id);
        return ResultMessage.success(viewCount);
    }

    // 评论数
    @GetMapping("/post-count")
    public ResultMessage post(@RequestParam Integer id) {
        Integer viewCount = topicService.getPostCount(id);
        return ResultMessage.success(viewCount);
    }

    // 排序话题
    @GetMapping("/sort/{sortType}")
    public ResultMessage sort(@PathVariable("sortType") Integer sortType) {
        List<TopicParam> topics = topicService.sort(sortType);
        return ResultMessage.success(topics);
    }

}
