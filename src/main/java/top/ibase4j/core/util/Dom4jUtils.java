/**
 * 
 */
package top.ibase4j.core.util;

import java.lang.reflect.Field;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;  
import org.dom4j.DocumentHelper;  
import org.dom4j.Element;  
 
public class Dom4jUtils {
    
    /** 
     * xml字符串转换成bean对象 
     *  
     * @param xmlStr xml字符串 
     * @param clazzMap 待转换的class包括对象属性的class 
     * @return 转换后的对象 
     */  
    public static Object xmlStrToBean(String xmlStr,Map<String,Class>  clazzMap) {  
        Object obj = null;  
        try {  
            // 将xml格式的数据转换成Map对象  
 
        	   Map<String, Object> map = new HashMap<String, Object>();  
               //将xml格式的字符串转换成Document对象  
               Document doc = DocumentHelper.parseText(xmlStr);  
               //获取根节点  
               Element root = doc.getRootElement();  
              
              //将map对象的数据转换成Bean对象  
               obj = mapToBean(root, clazzMap);  
        } catch(Exception e) {  
            e.printStackTrace();  
        }  
        return obj;  
    }  
      
 
   
    
      
    /** 
     * 将Map对象通过反射机制转换成Bean对象 
     *  
     * @param clazzMap 待转换的class包括对象属性的class 
     * @return 转换后的Bean对象 
     * @throws Exception 异常 
     */  
    public static Object mapToBean( Element root,Map<String,Class> clazzMap) throws Exception { 
    	 //获取根节点下的所有元素  
        List children = root.elements();
        List<Attribute> attrs= root.attributes();
      
    	 Map<String, Object> map = new HashMap<String, Object>(); 
    	   if(children != null && children.size() > 0) {  
               for(int i = 0; i < children.size(); i++) {              	
                  Element child = (Element)children.get(i);            
                  if(!child.isTextOnly()){               	  
                	 Object ob= mapToBean(child,clazzMap);
               	     map.put(child.getName(),ob);
                  }else{
               	   map.put(child.getName(), child.getTextTrim());  
                  }
                  
               }  
           }    	   
    	   for(Attribute att:attrs){
    		   map.put(att.getName(),att.getText());  
    	   }
    	   
    	Class clazz=clazzMap.get(root.getName());   
        Object obj = clazz.newInstance();  
        if(map != null && map.size() > 0) {  
            for(Map.Entry<String, Object> entry : map.entrySet()) {  
                String propertyName = entry.getKey();  
                Object value = entry.getValue();  
                String setMethodName = "set"  
                        + propertyName.substring(0, 1).toUpperCase()  
                        + propertyName.substring(1);  
                Field field = getClassField(clazz, propertyName);  
                Class fieldTypeClass = field.getType();  
                value = convertValType(value, fieldTypeClass);  
                clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);  
            }  
        }  
        return obj;  
    }  
      
    /** 
     * 将Object类型的值，转换成bean对象属性里对应的类型值 
     *  
     * @param value Object对象值 
     * @param fieldTypeClass 属性的类型 
     * @return 转换后的值 
     */  
    private static Object convertValType(Object value, Class fieldTypeClass) {  
        Object retVal = null;  
        if(Long.class.getName().equals(fieldTypeClass.getName())  
                || long.class.getName().equals(fieldTypeClass.getName())) {  
            retVal = Long.parseLong(value.toString());  
        } else if(Integer.class.getName().equals(fieldTypeClass.getName())  
                || int.class.getName().equals(fieldTypeClass.getName())) {  
            retVal = Integer.parseInt(value.toString());  
        } else if(Float.class.getName().equals(fieldTypeClass.getName())  
                || float.class.getName().equals(fieldTypeClass.getName())) {  
            retVal = Float.parseFloat(value.toString());  
        } else if(Double.class.getName().equals(fieldTypeClass.getName())  
                || double.class.getName().equals(fieldTypeClass.getName())) {  
            retVal = Double.parseDouble(value.toString());  
        } else {  
            retVal = value;  
        }  
        return retVal;  
    }  
  
    /** 
     * 获取指定字段名称查找在class中的对应的Field对象(包括查找父类) 
     *  
     * @param clazz 指定的class 
     * @param fieldName 字段名称 
     * @return Field对象 
     */  
    private static Field getClassField(Class clazz, String fieldName) {  
        if( Object.class.getName().equals(clazz.getName())) {  
            return null;  
        }  
        Field []declaredFields = clazz.getDeclaredFields();  
        for (Field field : declaredFields) {  
            if (field.getName().equals(fieldName)) {  
                return field;  
            }  
        }  
  
        Class superClass = clazz.getSuperclass();  
        if(superClass != null) {// 简单的递归一下  
            return getClassField(superClass, fieldName);  
        }  
        return null;  
    }   
    
    public static String getXML(){
    	StringBuffer sb=new StringBuffer();
    	sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    	sb.append("<note name=\"xian\">");
    	sb.append("<to>George</to>");
    	sb.append("<Student>");
    	sb.append("<name>xian</name>");
    	sb.append("<classRoom>");
    	sb.append("<name>Ap111111</name>");
    	sb.append("</classRoom>");
    	sb.append("<age>13</age>");
    	sb.append("</Student>");
    	sb.append("<from>John</from>");
    	sb.append("<heading>Reminder</heading>");
    	sb.append("<Body>Don't forget the meeting!</Body>");
    	sb.append("</note>");
    	return sb.toString();
    }
    
    public static void main(String[] args){
    	
     }
 
}
