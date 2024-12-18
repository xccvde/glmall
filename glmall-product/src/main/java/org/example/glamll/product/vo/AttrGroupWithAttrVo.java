package org.example.glamll.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.example.glamll.product.entity.AttrEntity;

import java.util.List;

@Data
public class AttrGroupWithAttrVo {
    /**
     * 分组id
     */
    @TableId
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
