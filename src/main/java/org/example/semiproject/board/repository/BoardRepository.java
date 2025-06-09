package org.example.semiproject.board.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.semiproject.board.domain.Board;
import org.example.semiproject.board.domain.dto.ListBoardDTO;

import java.util.List;

@Mapper
public interface BoardRepository {

    @Select("select bno, title, userid, regdate, thumbs, views from boards order by bno desc limit #{stnum}, 25")
    List<ListBoardDTO> selectBoard(int stnum);

    @Select("select * from boards where bno = #{bno}")
    Board selectOneBoard(int bno);

}

