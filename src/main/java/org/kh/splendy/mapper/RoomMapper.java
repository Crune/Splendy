package org.kh.splendy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.kh.splendy.vo.Room;

public interface RoomMapper {

	@Results(id = "roomResult", value = {
		@Result(property = "id", column = "RM_ID"),
		@Result(property = "title", column = "RM_TITLE"),
		@Result(property = "password", column = "RM_PW"),
		@Result(property = "info", column = "RM_INFO"),
		@Result(property = "playerLimits", column = "RM_PL_LIMITS"),
		@Result(property = "state", column = "RM_STATE"),
	})
	@Select("select * from KH_ROOM where RM_STATE=1")
	public List<Room> getCurrentRooms();

}
