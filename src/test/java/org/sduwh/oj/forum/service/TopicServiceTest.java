package org.sduwh.oj.forum.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sduwh.oj.forum.param.TopicParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class TopicServiceTest {

    @Autowired
    private TopicService topicService;

    private MockHttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    public void getTopicById() {
        Integer topicId = 132;
        TopicParam topic = topicService.getTopicById(topicId);
        System.out.println(topic);
    }
}