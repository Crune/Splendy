package org.kh.splendy.vo;

import lombok.Data;

import java.util.List;

@Data
public class WSCoinRequest {
    List<PLCoin> req = null;
    List<PLCoin> draw = null;
}
