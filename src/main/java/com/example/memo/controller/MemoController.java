package com.example.memo.controller;

import com.example.memo.MemoRequestDto;
import com.example.memo.entity.Memo;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/memos") // prefix
public class MemoController {

    // 자료구조가 DB 역할 수행
    private final Map<Long, Memo> memoList = new HashMap<>();

    @PostMapping
    public ResponseEntity<MemoResponseDto> createMemo(@RequestBody MemoRequestDto requestDto) {
        // 식별자가 1씩 증가 하도록 만듦
        Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;

        // 요청받은 데이터로 Memo 객체 생성
        Memo memo = new Memo(memoId, requestDto.getTitle(), requestDto.getContents());

        // Inmemory DB에 Memo 저장
        memoList.put(memoId, memo);

        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.CREATED);
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

        @GetMapping
        public List<MemoResponseDto> findAllMemos() {

            // init List 리스트 초기화
            List<MemoResponseDto> responseList = new ArrayList<>();

            // HashMap<Memo> -> List<MemoResponseDto> 변경 (두 가지 방법)
            for (Memo memo : memoList.values()) {
                MemoResponseDto responseDto = new MemoResponseDto(memo);
                responseList.add(responseDto);
            }

            // Map To List
            // responseList = memoList.values().stream().map(MemoResponseDto::new).toList();
            return responseList;
        }

        @PutMapping("/{id}")
        public ResponseEntity<MemoResponseDto> updateMemo(
                @PathVariable Long id,
                @RequestBody MemoRequestDto requestDto
        ) {

            Memo memo = memoList.get(id);

            // NPE 방지
            if (memo == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // 필수값 검증
            if (requestDto.getTitle() == null || requestDto.getContents() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // memo 수정
            memo.update(requestDto);

            // 응답
            return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
        }

        @PatchMapping("/{id}")
        public ResponseEntity<MemoResponseDto> updateTitle(
                @PathVariable Long id,
                @RequestBody MemoRequestDto requestDto
        ) {
            Memo memo = memoList.get(id);

            // NPE 방지
            if (memo == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // 필수값 검증
            if (requestDto.getTitle() == null || requestDto.getContents() != null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            memo.updateTitle(requestDto);

            return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);

        }

        @DeleteMapping("/{id}")
        public void deleteMemo(
                @PathVariable Long id
        ) {
            memoList.remove(id);
        }
    }
}
