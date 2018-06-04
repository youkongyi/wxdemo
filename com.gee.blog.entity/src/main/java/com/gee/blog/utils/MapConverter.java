package com.gee.blog.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Map转为具体对象工具类
 * @author "GeHanBiao"
 *
 */
public class MapConverter {
	
	private MapConverter() {
		throw new RuntimeException("禁止初始化");
	}
	
	@SuppressWarnings("rawtypes")
	public static Object packageEntityMapConverter(Map map ,Class clazz){
		Field[] fields = clazz.getDeclaredFields();
		Method[] methods = clazz.getDeclaredMethods();
		Object obj = null;
		String fieldName = null;
		String methodName = null;
		Object val = null;
		
		try {
			obj = clazz.newInstance();
			if(map == null || map.size()==0) {
				return obj;
			}
			while(clazz != Object.class) {
				for( int i = 0 ; i < fields.length; i++ ) {	
					Field field = fields[i];
					fieldName = field.getName();
					val = map.get(fieldName);
					if(val == null || "".equals(val)) {
						continue;
					}
					methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					for (int j = 0 ; j < methods.length; j++) {
						Method method = methods[j];
						if (methodName.equalsIgnoreCase(method.getName())) {
							method.invoke( obj, val);
							break;
						}
					}
				}
				clazz = clazz.getSuperclass();
				fields = clazz.getDeclaredFields();
				methods = clazz.getDeclaredMethods();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
}
