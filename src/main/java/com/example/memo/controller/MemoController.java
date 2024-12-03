package com.example.memo.controller;

import com.example.memo.MemoRequestDto;
import com.example.memo.entity.Memo;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/memos") // prefix
public class MemoController {

    // 자료구조가 DB 역할 수행
    private final Map<Long, Memo> memoList = new HashMap<>();

    @PostMapping
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        // 식별자가 1씩 증가 하도록 만듦
        Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;

        // 요청받은 데이터로 Memo 객체 생성
        Memo memo = new Memo(memoId, requestDto.getTitle(), requestDto.getContents());

        // Inmemory DB에 Memo 저장
        memoList.put(memoId, memo);

        return new MemoResponseDto(memo);
    }
    @Getter
    public class MemoResponseDto {

        private Long id;
        private String title;
        private String contents;

        // Memo class를 인자로 가지는 생성자
        public MemoResponseDto(Memo memo) {
            this.id = memo.getId();
            this.title = memo.getTitle();
            this.contents = memo.getContents();
        }

        @PutMapping("/{id}")
        public MemoResponseDto updateMemoById(
                @PathVariable Long id,
                @RequestBody MemoRequestDto requestDto
        ) {

            Memo memo = memoList.get(id);

            memo.update(requestDto);

            return new MemoResponseDto(memo);
        }

        @DeleteMapping("/{id}")
        public void deleteMemo(
                @PathVariable Long id
        ) {
            memoList.remove(id);
        }
    }
}
