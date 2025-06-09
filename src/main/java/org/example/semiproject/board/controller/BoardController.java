package org.example.semiproject.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.semiproject.board.serivce.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public String list(Model m, @RequestParam(defaultValue = "1") int cpg) {

        // RequestParam에 defaultValue를 이용하면
        // cpg 매개변수가 전달되지 않을 경우 cpg의 기본값은 1로 전달됨
        log.info("board/list 호출!!");

        m.addAttribute("bds", boardService.readBoard(cpg));
        m.addAttribute("cpg", cpg);
        m.addAttribute("stblk", ((cpg - 1) / 10) * 10 + 1);


        return "views/board/list";
    }

    @GetMapping("/view")
    public String view(Model m, int bno) {

        m.addAttribute("bd", boardService.readOneBoard(bno));

        return "views/board/view";
    }

}
