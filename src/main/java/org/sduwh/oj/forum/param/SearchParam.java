package org.sduwh.oj.forum.param;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SearchParam {

    private String keywords;

    private Integer contestId;

    private Integer problemId;

}
