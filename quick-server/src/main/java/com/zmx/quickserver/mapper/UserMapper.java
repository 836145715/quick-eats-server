package com.zmx.quickserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmx.quickpojo.entity.User;
import com.zmx.quickpojo.vo.UserStatisticsRspVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select date(create_time) `date`,count(id) `inc_count` " +
            "from user " +
            "where create_time >= #{begin} and create_time < #{end} " +
            "group by date(create_time) ")
    List<Map<String, Object>> userStatistics(LocalDateTime begin, LocalDateTime end);
}