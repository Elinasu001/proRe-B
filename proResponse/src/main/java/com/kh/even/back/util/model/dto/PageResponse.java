package com.kh.even.back.util.model.dto;

import java.util.List;

import com.kh.even.back.util.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {

    private List<T> list;
    private PageInfo pageInfo;
    
}
