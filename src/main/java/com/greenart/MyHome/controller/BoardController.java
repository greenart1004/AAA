package com.greenart.MyHome.controller;



import java.util.List;

import javax.validation.Valid;

//import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.greenart.MyHome.model.Board;
import com.greenart.MyHome.repository.BoardRepository;
import com.greenart.MyHome.validator.BoardValidator;

//import com.godcoder.myhome.model.Board;
//import com.godcoder.myhome.repository.BoardRepository;
//import com.godcoder.myhome.service.BoardService;
//import com.godcoder.myhome.validator.BoardValidator;

@Controller                                       //페이지를 받을때    @RestController  = 데이타를 받을때
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;
//
//    @Autowired
//    private BoardService boardService;
//
    @Autowired                                             //spring의 DI를 이용하기 위한 어노테이션
    private BoardValidator boardValidator;

    @GetMapping("/list")
    public String list(Model model,@PageableDefault(size = 7) Pageable pageable, @RequestParam(required = false, defaultValue = "") String searchText) {
    	//	Page<Board> boards = boardRepository.findAll(pageable);    // PageRequest.of(1, 10)
        // @RequestParam(required = false, defaultValue = "")은 기본적으로 searchText = null값이 들어가므로 @RequestParam으로 값을 넣어줄수 있음            
    	// required = false,은 값이 없어도 상관없다는 옵션임
    	
    	Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
     
    	int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);   //boards.getPageable()은 현재 페이지
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);     // 앞쪽 "boards" 라는 변수이름으로 프런트에 넘김... 변수값은 뒤쪽 boards에 들어있는값
        return "board/list"; 
    }

    @GetMapping("/form")
	public String form(Model model, @RequestParam(required = false) Long id) { /* (required = false)이 속성은 필수인지 아닌지.//필요할때만 id 값을 사용함 */
        if(id == null) {
            model.addAttribute("board", new Board());   // form에서 입력받기전에 입력받을 object를 미리 보내놓음
        } else {
            Board board = boardRepository.findById(id).orElse(null);  // findById()는 optional인데 값을 없을때는 null값을 준다
            model.addAttribute("board", board);
        }
        return "board/form";
    }

    @PostMapping("/form")
    public String greetingSubmit(@Valid Board board, BindingResult bindingResult) {  //bindingResult  Board model에서 입력된값이 오류인지 확인하는 객체?
    	boardValidator.validate(board, bindingResult);
    	if (bindingResult.hasErrors()) {   			 //@Valid를 이용하면, service 단이 아닌 객체 안에서, 들어오는 값에 대해 검증을 할 수 있다.
    	  return "board/form";
      }
    	boardRepository.save(board);
        return "redirect:/board/list";             // redirect:board/list는 컨트롤러 get()매핑으로 board/list로 이동함
    }

    
    //
//    @PostMapping("/form")
//    public String postForm(, Authentication authentication) {
//    boardValidator.validate(board, bindingResult);
//        String username = authentication.getName();
//        boardService.save(username, board);
////        boardRepository.save(board);
//        return "redirect:/board/list";
//    }
    
    

}