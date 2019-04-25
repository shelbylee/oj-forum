package org.sduwh.oj.forum.controller;

import lombok.extern.slf4j.Slf4j;
import org.sduwh.oj.forum.common.ResultMessage;
import org.sduwh.oj.forum.param.OjContestParam;
import org.sduwh.oj.forum.param.UserParam;
import org.sduwh.oj.forum.service.UserService;
import org.sduwh.oj.forum.util.SpringRestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private SpringRestTemplateUtil restTemplateUtil;

    // 返回当前登录用户的信息
    @GetMapping()
    public ResultMessage getCurrentUserInfo() {
        UserParam user = userService.getUserInfo();
        return ResultMessage.success(user);
    }

    // 返回比赛创建者的信息
    @GetMapping("/contest-creator/{contestId}")
    public ResultMessage getContestCreatorInfo(@PathVariable("contestId") Integer contestId) {
        OjContestParam.OjData.CreatorData creator = restTemplateUtil.getContestCreatorData(contestId);
        return ResultMessage.success(creator);
    }
}
