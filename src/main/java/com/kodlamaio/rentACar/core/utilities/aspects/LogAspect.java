package com.kodlamaio.rentACar.core.utilities.aspects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONException;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component // classdan nesne üretme işini spring e bırakıyoruz
public class LogAspect {
	//List<StringBuilder> builderList = new ArrayList<StringBuilder>();

	// @Before("execution(*
	// com.kodlamaio.rentACar.business.concretes.BrandManager.*(..))")
	@Before("execution(* com.kodlamaio.rentACar.business.concretes.*.*(..))")
	public void beforeLog(JoinPoint joinPoint) throws JSONException, IOException {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		StringBuilder builder = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();
		
		builder.append("\n{");
		builder.append(("\n" + "\"date\":") + mapper.writeValueAsString(LocalDate.now().getYear() + "-"+LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth()));
		
		builder.append("\n" + "\"className\":" + mapper.writeValueAsString(joinPoint.getTarget().getClass().getSimpleName()));
		builder.append("\n" +  "\"methodName\":"  + mapper.writeValueAsString(signature.getMethod().getName()));

		if (signature.getMethod().getName() != "getAll") {
			builder.append("\n" + "\"parameters:\":" + mapper.writeValueAsString(joinPoint.getArgs())); // java reflection
		
		} else {
			builder.append("\n" + "\"parameter:\":" + "null");
			
		}
		builder.append("\n" +"}");
		
		File file = new File("C:\\logs\\operations.json");
				
		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true)) ) {
			bufferedWriter.write(builder.toString());
		} catch (Exception e) {
			
		}




//	
//	@After("pointcut()")
//	public void afterLog() {
//		System.out.println("After brand manager delete");
//	}

//	@Before("execution(* com.kodlamaio.rentACar.business.concretes.BrandManager.getById(int))")
//	public void beforeLog(JoinPoint joinPoint) {
//		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
//		System.out.println("Before brand manager getbyid");
//		System.out.println(joinPoint.getArgs()[0]);
//		System.out.println(joinPoint.getSignature().getName());
//		System.out.println(signature.getParameterNames()[0]);
//	}

//	@Around("execution(* com.kodlamaio.rentACar.business.concretes.*.*(..))")
//	public void beforeLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//	
//		try {
//			System.out.println("before");
//			proceedingJoinPoint.proceed(); //method delagasyonu burası getbyid oldu
//			System.out.println("after returning");
//		} catch (Exception e) {
//			System.out.println("after throwing");
//			e.printStackTrace();
//		}
//		
//		System.out.println("after finally");
//	}

//	@Pointcut("execution(* com.kodlamaio.rentACar.business.concretes.BrandManger.getById(int)) ") //.*.* hepsi için
//	public void pointcut() {} //kesişim noktası //dummy
	}
}
