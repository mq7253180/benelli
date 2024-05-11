package com.quincy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

//ImportBeanDefinitionRegistrar
//ImportSelector
public class MyInit {
	static {
		System.out.println("=============MyInit");
	}

	@Value("${server.port}")
	private String port;

	public void test() {
		System.out.println("MyInit-------------port: "+port);
	}

//	@Override
//	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
//		return new String[]{MyInit.class.getName()};
//	}

//	@Override
//	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//		RootBeanDefinition bd = new RootBeanDefinition();
//		bd.setBeanClass(MyInit.class);
//		registry.registerBeanDefinition("myInit", bd);
//	}
}