package org.kh.splendy.assist;

import org.kh.splendy.service.StreamService;

import org.springframework.beans.factory.annotation.Autowired;

@WSController
public abstract class ProtocolHelper {
	
	@Autowired private StreamService stream;
	
}
