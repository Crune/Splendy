package org.kh.splendy.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 
 * @author 진규
 *
 */
@Controller
public class SampleController {

	private static final Logger log = LoggerFactory.getLogger(SampleController.class);

    
	@RequestMapping("/s/")
	public String index() {
		return "sample";
	}
	
}
