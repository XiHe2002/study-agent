package com.studyagent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.studyagent.entity.QuizAttempt;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuizAttemptMapper
        extends BaseMapper<QuizAttempt> {
}