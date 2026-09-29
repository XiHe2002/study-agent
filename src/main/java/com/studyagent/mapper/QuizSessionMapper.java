package com.studyagent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.studyagent.entity.QuizSession;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuizSessionMapper
        extends BaseMapper<QuizSession> {
}