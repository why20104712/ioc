package cn.why.ioc;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.why.bean.Bean;
import cn.why.bean.Property;

/**
 * bean容器实现类
 * @author why
 *
 */
public class ClassPathApplicationContext implements BeanFactory{

	Map<String, Object> ioc = new HashMap<String, Object>();
	
	/**
	 * 创建该类的时候初始化ioc容器
	 */
	public ClassPathApplicationContext() {
		//初始化ioc容器
		//读取配置文件
		Map<String, Bean> beans = ConfigManager.parseXmlBean("applicationContext.xml");
		Set<Entry<String, Bean>> entrySet = beans.entrySet();
		for (Entry<String, Bean> entry : entrySet) {
			String beanName = entry.getKey();
			Bean bean = entry.getValue();
			Object beanObj = createBean(bean);
			ioc.put(beanName, beanObj);
		}
	}
	
	/**
	 * 根据bean的信息创建对应的实例对象
	 * @param bean bean的信息
	 * @return Object 实例对象
	 */
	private Object createBean(Bean bean) {
		String className = bean.getClassName();
		Class clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("找不到类名对应的类,请检查类名是否正确");
		}
		Object beanObj = null;
		if (null != clazz) {
			try {
				//创建bean对象
				beanObj = clazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("检查类是否有空参数的构造方法");
			}
		}
		
		//设置bean对应的属性
		List<Property> properties = bean.getProperties();
		if (null != properties && properties.size() > 0) {
			for (Property property : properties) {
				String pvalue = property.getValue();
				String ref = property.getRef();
						
				//普通属性注入
				if (null != pvalue) {
					String pname = property.getName();
					Method method = getWriteMethod(beanObj, pname);
					try {
						method.invoke(beanObj, pvalue);
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException("请检查bean对应的属性是否有set方法");
					}
				}
				
				//依赖关系注入
				if (null != ref) {
					
				}
			}
		}
		return beanObj;
	}

	/**
	 * 根据对象和属性名称获取对应的set方法
	 * @param beanObj bean对象
	 * @param pname 属性名称
	 * @return Method set方法
	 */
	private Method getWriteMethod(Object beanObj, String pname) {
		Method writeMethod = null;
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(beanObj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			if (null != propertyDescriptors) {
				for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
					String name = propertyDescriptor.getName();
					if (pname.equals(name)) {
						writeMethod = propertyDescriptor.getWriteMethod();
					}
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		if (null == writeMethod) {
			throw new RuntimeException("请检查该属性是否有set方法");
		}
		return writeMethod;
	}

	public Object getBean(String name) {
		return ioc.get(name);
	}

}
