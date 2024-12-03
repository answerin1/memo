package com.example.memo.Dto;

import com.example.memo.entity.Memo;
import lombok.Getter;

@Getter
public class MemoResponse {
    private Long id;
    private String title;
    private String contents;

    public MemoResponse(Memo memo) {
        this.id = memo.getId();
        this.title = memo.getTitle();
        this.contents = memo.getContents();

    }
}
