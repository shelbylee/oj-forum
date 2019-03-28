package org.sduwh.oj.forum.interceptor;

import org.sduwh.oj.forum.param.UserParam;
import org.sduwh.oj.forum.util.SpringRestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://yiiu.co
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

  @Autowired
  SpringRestTemplateUtil restTemplateUtil;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    HttpSession session = request.getSession();
    UserParam user = (UserParam) session.getAttribute("_user");

    if (StringUtils.isEmpty(user)) {
      user = restTemplateUtil.getUserInfo(request);
      session.setAttribute("_user", user);
    }

    return true;
  }
}
