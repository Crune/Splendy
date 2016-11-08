package org.kh.splendy.mapper;

import org.apache.ibatis.annotations.*;
import org.kh.splendy.CRUDProvider;

public interface BasicMapper {

	@InsertProvider(method = "create", type = CRUDProvider.class)
	public void create(Object obj) throws Exception;
}
