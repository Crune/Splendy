package org.kh.splendy.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserWithRole extends UserCore {
	private List<String> roles;
}
