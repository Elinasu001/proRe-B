package com.kh.even.back.chat.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.chat.model.dto.ChatDetailDTO;

@Mapper
public interface ChatMapper {
    List<ChatDetailDTO> findByRoomNo(Long roomNo);
}
