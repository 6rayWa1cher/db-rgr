package com.a6raywa1cher.db_rgr.unit;

import com.a6raywa1cher.db_rgr.lib.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilsTest {
	@Test
	void capitalizeFirstLetter() {
		assertEquals("A", StringUtils.capitalizeFirstLetter("a"));
		assertEquals("Cat", StringUtils.capitalizeFirstLetter("cat"));
		assertEquals("CatSaysMeow", StringUtils.capitalizeFirstLetter("catSaysMeow"));
	}

	@Test
	void camelCaseToUnderscore() {
		assertEquals("meow", StringUtils.camelCaseToUnderscore("meow"));
		assertEquals("cat_says_meow", StringUtils.camelCaseToUnderscore("catSaysMeow"));
	}
}
