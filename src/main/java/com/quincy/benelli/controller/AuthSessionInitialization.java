package com.quincy.benelli.controller;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.quincy.benelli.service.XxxService;
import com.quincy.core.web.PublicKeyGetter;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@Configuration
public class AuthSessionInitialization implements HttpSessionListener, BeanDefinitionRegistryPostProcessor {
	@Override
	public void sessionCreated(HttpSessionEvent hse) {
		System.out.println("******************sessionCreated");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent hse) {
		System.out.println("******************sessionDestroyed");
	}

	@Bean
	public PublicKeyGetter publicKeyGetter() {
		return new PublicKeyGetter() {
			@Override
			public String getById(String id) {
//				return null;
				return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMYDMqMFSJL+nUMzF7MQjCYe/Y3P26wjVn90CdrSE8H9Ed4dg0/BteWn5+ZK65DwWev2F79hBIpprPrtVe+wplCTkpyR+mPiNL+WKkvo7miMegRYJFZLvh9QrFuDzMJZ+rAiu4ldxkVB0CMKfYEWbukKGmAinxVAqUr/HcW2mWjwIDAQAB";
			}
		};
	}

	/*@Bean
    public HttpSessionListener httpSessionListener() {
		return new HttpSessionListener() {
			@Override
			public void sessionCreated(HttpSessionEvent hse) {
				System.out.println("-------------sessionCreated");
			}

			@Override
			public void sessionDestroyed(HttpSessionEvent hse) {
				System.out.println("-------------sessionDestroyed");
			}
		};
	}*/

	@Bean
    public ServletListenerRegistrationBean<HttpSessionListener> sessionListenerWithMetrics() {
        ServletListenerRegistrationBean<HttpSessionListener> listenerRegBean = new ServletListenerRegistrationBean<>();
        listenerRegBean.setListener(new HttpSessionListener() {
			@Override
			public void sessionCreated(HttpSessionEvent hse) {
				System.out.println("============sessionCreated");
			}

			@Override
			public void sessionDestroyed(HttpSessionEvent hse) {
				System.out.println("============sessionDestroyed");
			}
		});
        return listenerRegBean;
    }

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (registry.containsBeanDefinition("xxxService1Impl")){
        	registry.removeBeanDefinition("xxxService1Impl");
        }
	}

	@Autowired
	public ApplicationContext applicationContext;

	@PostConstruct
	public void test() {
//		XxxService service = applicationContext.getBean(XxxService.class);
//		System.out.println("XxxService========="+(service!=null));
		Map<String, XxxService> map = applicationContext.getBeansOfType(XxxService.class);
		Set<Entry<String, XxxService>> set = map.entrySet();
		for(Entry<String, XxxService> e:set) {
			System.out.println("BEAN_TEST=============="+e.getKey()+"---------"+e.getValue());
		}
	}
}