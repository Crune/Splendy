package org.kh.splendy.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WSComp {
    List<PLCard> cards = new ArrayList<>();
    List<PLCoin> coins = new ArrayList<>();
}
