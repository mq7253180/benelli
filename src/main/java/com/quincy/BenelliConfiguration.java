package com.quincy;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.quincy.sdk.WebMvcConfiguration;

@PropertySource(value = {"classpath:application-core.properties", "classpath:application-auth.properties"})
@Configuration("sssiiiccc")
public class BenelliConfiguration extends WebMvcConfiguration {
	@PostConstruct
	public void init() {
		
	}

	@PreDestroy
	private void destroy() {
		
	}
}