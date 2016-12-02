package org.kh.splendy.vo;

import lombok.Data;

@Data
public class PLCoin {
	int rm_id=0;
	int u_id=0;
	int cn_id=0;
	int cn_count=0;

    public PLCoin() {

    }

	public PLCoin(int room, int user, int coin, int amount) {
		this.rm_id = room;
		this.u_id = user;
		this.cn_id = coin;
		this.cn_count = amount;
	}
}
