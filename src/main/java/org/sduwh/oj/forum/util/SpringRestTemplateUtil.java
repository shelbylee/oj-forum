package org.sduwh.oj.forum.util;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.sduwh.oj.forum.exception.ParamException;
import org.sduwh.oj.forum.param.OjContestParam;
import org.sduwh.oj.forum.param.OjUserParam;
import org.sduwh.oj.forum.param.UserParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class SpringRestTemplateUtil {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${oj.ssoUrl}")
    private String ssoUrl;

    @Value("${oj.contestUrl}")
    private String contestUrl;

    /**
     * 调用oj的sso接口进行第三方登录
     *
     * @param request
     * @return 返回登录用户的信息
     */
    public synchronized UserParam getUserInfo(HttpServletRequest request) {

        Gson gson = new Gson();

        HttpHeaders getHeaders = new HttpHeaders();

        Cookie[] cookies = request.getCookies();
        List<String> cookieList = new ArrayList<>();

        Preconditions.checkNotNull(cookies, "Cookie is null!");

        for (Cookie cookie : cookies) {
            cookieList.add(cookie.getName() + "=" + cookie.getValue());
            getHeaders.put(HttpHeaders.COOKIE, cookieList);
        }

        HttpEntity getRequest = new HttpEntity(getHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(ssoUrl, HttpMethod.GET, getRequest, String.class);

        JsonObject respBody = new JsonParser().parse(Objects.requireNonNull(responseEntity.getBody())).getAsJsonObject();

        // {"error":null,"data":{"token":"6a49bb3eb46264010277cd96c49953a9"}}
        OjUserParam ojUserParam = gson.fromJson(respBody, OjUserParam.class);

        List<String> tokenList = new ArrayList<>();
        tokenList.add(ojUserParam.getData().getToken());
        HttpHeaders postHeaders = new HttpHeaders();

        postHeaders.put("token", tokenList);

        HttpEntity postRequest = new HttpEntity(ojUserParam.getData(), null);
        ResponseEntity<String> result = restTemplate.postForEntity(ssoUrl, postRequest, String.class);

        JsonObject userBody = new JsonParser().parse(Objects.requireNonNull(result.getBody())).getAsJsonObject();

        if (!String.valueOf(userBody.get("error")).equals("null")) {
            log.error("Post request to qduoj /api/sso failed!");
            throw new ParamException("User does not exist");
        }

        OjUserParam userInfo = gson.fromJson(userBody, OjUserParam.class);

        UserParam user = new UserParam();
        user.setUserId(userInfo.getData().getUserId());
        user.setUserName(userInfo.getData().getUserName());
        user.setUserType(userInfo.getData().getUserType());

        return user;

    }

    /**
     * 根据contest id获取contest信息
     *
     * @param contestId
     * @return 返回比赛创建者的信息
     */
    public OjContestParam.OjData.CreatorData getContestCreatorData(Integer contestId) {

        Gson gson = new Gson();

        HttpEntity getRequest = new HttpEntity(new HttpHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(contestUrl + "?id=" + contestId, HttpMethod.GET, getRequest, String.class);
        JsonObject respBody = new JsonParser().parse(Objects.requireNonNull(responseEntity.getBody())).getAsJsonObject();

        Type contestType = new TypeToken<OjContestParam>() {
        }.getType();
        // {"error":null,"data":{"token":"6a49bb3eb46264010277cd96c49953a9"}}
        OjContestParam ojContestParam = gson.fromJson(respBody, contestType);

        OjContestParam.OjData.CreatorData creatorData = ojContestParam.getData().getCreated_by();

        return creatorData;
    }

}
