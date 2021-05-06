package com.greenart.MyHome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenart.MyHome.model.Board;
import com.greenart.MyHome.model.User;
import com.greenart.MyHome.repository.BoardRepository;
import com.greenart.MyHome.repository.UserRepository;


@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    public Board save(String username, Board board) {
        User user = userRepository.findByUsername(username);
        board.setUser(user);
        return boardRepository.save(board);
    }

}
