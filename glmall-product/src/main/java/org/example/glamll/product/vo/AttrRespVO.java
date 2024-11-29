package org.example.glamll.product.vo;

import lombok.Data;

@Data
public class AttrRespVO extends AttrVo{
    private String catelogName;
    private String groupName;
    private Long[] catelogPath;
}
