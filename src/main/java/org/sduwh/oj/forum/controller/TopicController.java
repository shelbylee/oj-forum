package org.sduwh.oj.forum.controller;

import lombok.extern.slf4j.Slf4j;
import org.sduwh.oj.forum.common.ResultMessage;
import org.sduwh.oj.forum.model.Topic;
import org.sduwh.oj.forum.param.TopicParam;
import org.sduwh.oj.forum.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/vote")
    public ResultMessage vote(@RequestParam Integer id) {
        Integer likeCount = topicService.vote(id);
        return ResultMessage.success(likeCount);
    }

}
