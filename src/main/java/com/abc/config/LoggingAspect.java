package com.abc.config;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggingAspect {
	private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

	public Logger getLogger() {
		return log;
	}

	@Around(value = "execution(* com.ct.*.*.*(..))")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		log.debug("\n# INPUT TO METHOD {} : \t# {} ", joinPoint, Arrays.toString(joinPoint.getArgs()));
		Object obj = null;
		obj = joinPoint.proceed(); // <- try { ... } catch (Throwable e) { }
		log.debug("\n# RETURNED OBJECT: \t# {} #", (obj == null ? "VOID" : obj.toString()));
		return obj;
	}

}
