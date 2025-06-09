package org.example.semiproject.board.serivce;

import lombok.RequiredArgsConstructor;
import org.example.semiproject.board.domain.Board;
import org.example.semiproject.board.domain.dto.ListBoardDTO;
import org.example.semiproject.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardMapper;

    @Override
    public List<ListBoardDTO> readBoard(int cpg) {
        // cpg에 따라 시작위치값 계산
        int stnum = (cpg - 1) * 25;
        return boardMapper.selectBoard(stnum);
    }

    @Override
    public Board readOneBoard(int bno) {
        return boardMapper.selectOneBoard(bno);
    }

}

