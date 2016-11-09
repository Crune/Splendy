package org.kh.splendy;

import org.apache.ibatis.jdbc.SQL;

public class CRUDProvider {

	// @formatter:off
	public static String create(Object obj) {
		return new SQL() {{
			INSERT_INTO("table");
			VALUES("calumn", "#{value}");
		}}.toString();
	}
	public static String read(Object obj) {
		String key = "";
		return new SQL() {{
			SELECT("*");
			FROM("table");
			if (key != null) {
				WHERE("P.ID like #{id}");
			}
		}}.toString();
	}
	public static String update(Object obj) {
		return new SQL() {{
			UPDATE("table");
			SET("condition = #{condition}");
			WHERE("condition");
		}}.toString();
	}
	public static String delete(Object obj) {
		return new SQL() {{
			DELETE_FROM("table");
			WHERE("condition");
		}}.toString();
	}
	// @formatter:on
}
