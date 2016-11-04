package org.kh.splendy.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Admin extends UserCore {
	private String role;
}
