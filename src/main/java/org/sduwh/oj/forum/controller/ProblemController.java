package org.sduwh.oj.forum.controller;

import lombok.extern.slf4j.Slf4j;
import org.sduwh.oj.forum.common.ResultMessage;
import org.sduwh.oj.forum.param.TopicParam;
import org.sduwh.oj.forum.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    // 查询某个problem下的所有话题
    @GetMapping("/{id}/topics")
    public ResultMessage selectByProblemId(@PathVariable("id") Integer id) {
        List<TopicParam> topicList = problemService.getTopicsByProblemId(id);
        return ResultMessage.success(topicList);
    }

    // 排序话题
    @GetMapping("/{problemId}/sort/{sortType}")
    public ResultMessage sort(@PathVariable("problemId") Integer problemId, @PathVariable("sortType") Integer sortType) {
        List<TopicParam> topics = problemService.sort(problemId, sortType);
        return ResultMessage.success(topics);
    }
}
