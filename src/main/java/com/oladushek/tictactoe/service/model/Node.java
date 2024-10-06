package com.oladushek.tictactoe.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class Node {

    private String[] board;

    private Integer index;

    private Mark mark;

    private Integer scope = 0;

    private List<Node> children = new ArrayList<>();

}
