package com.eline.anagrams.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartupListener implements InitializingBean {

	@Autowired
	AnagramUtil util;

	@Override
	public void afterPropertiesSet() throws Exception {
		util.loadDictionaryMap();

	}

}
