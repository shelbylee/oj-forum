package org.sduwh.oj.forum.controller;

import lombok.extern.slf4j.Slf4j;
import org.sduwh.oj.forum.common.ResultMessage;
import org.sduwh.oj.forum.param.SearchParam;
import org.sduwh.oj.forum.param.TopicParam;
import org.sduwh.oj.forum.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/topics")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public ResultMessage searchTopic(SearchParam searchParam) {
        List<TopicParam> topicList = searchService.search(searchParam);
        return ResultMessage.success(topicList);
    }
}
