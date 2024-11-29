package org.example.glamll.member.dao;

import org.example.glamll.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author zxc
 * @email z2485861264@gmail.com
 * @date 2024-08-04 18:30:52
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
