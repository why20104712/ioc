package cn.why.ioc;

/**
 * Bean工厂接口
 * @author why
 *
 */
public interface BeanFactory {

	/**
	 * 根据bean的名称获取bean
	 * @param name
	 * @return
	 */
	Object getBean(String name);
}
