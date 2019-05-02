package org.sduwh.oj.forum.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sduwh.oj.forum.param.UserParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TopicControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;
    private MockHttpSession session;

    @Before
    public void setupMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
        session = new MockHttpSession();
        UserParam user =new UserParam();
        user.setUserId(1);
        user.setUserName("root");
        user.setUserType("Super Admin");
        session.setAttribute("userParam",user); //拦截器那边会判断用户是否登录，所以这里注入一个用户
    }

    @Test
    public void createTopic() throws Exception {
        String json = "{\n" +
                "    \"title\": \"201904301107title\",\n" +
                "    \"content\": \"201904301107content\"\n" +
                "}";
        mvc.perform(MockMvcRequestBuilders.post("/api/topics")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(json.getBytes()) //传json参数
                .session(session)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}
