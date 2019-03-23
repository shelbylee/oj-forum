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
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    // 查询话题
    @GetMapping("/{id}")
    public ResultMessage select(@PathVariable Integer id) {
        Topic topic = topicService.getTopicById(id);
        return ResultMessage.success(topic);
    }

    // 创建话题
    @PostMapping("/create")
    public ResultMessage create(TopicParam param) {
        Topic topic = topicService.saveTopic(param);
        return ResultMessage.success(topic);
    }

    // 编辑话题
    @PutMapping("/edit")
    public ResultMessage edit(TopicParam param) {
        Topic topic = topicService.editTopicById(param);
        return ResultMessage.success(topic);
    }

    // 删除话题
    @DeleteMapping("/delete")
    public ResultMessage delete(TopicParam param) {
        topicService.deleteTopicById(param);
        return ResultMessage.success();
    }
}
