package cn.why.ioc;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.why.bean.Bean;
import cn.why.bean.Property;

/**
 * 配置文件信息解析管理
 *
 */
public class ConfigManager 
{
    
	/**
	 * 解析配置文件里的bean
	 * @param path 配置文件路径
	 * @return Map<String, Object>
	 */
	public static Map<String, Bean> parseXmlBean(String path){
		
		Map<String, Bean> beanMap = new HashMap<String, Bean>();
		// 创建解析器
		SAXReader reader = new SAXReader();
		InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(path);
		Document document = null;
		try {
			document = reader.read(inputStream);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("请检查配置文件的位置是否正确！");
		}
		String xpath = "//bean";
		
		List<Element> elementList = document.selectNodes(xpath);
		
		if (null != elementList && elementList.size() > 0) {
			for (Element beanElement : elementList) {
				Bean bean = new Bean();
				String name = beanElement.attributeValue("name");
				String className = beanElement.attributeValue("class");
				
				bean.setClassName(className);
				bean.setName(name);
				
				List<Element> propertyList = beanElement.elements();
				if (null != propertyList && propertyList.size() > 0) {
					for (Element propertyElement : propertyList) {
						Property property = new Property();
						String pname = propertyElement.attributeValue("name");
						String pvalue = propertyElement.attributeValue("value");
						String ref = propertyElement.attributeValue("ref");
						property.setName(pname);
						property.setValue(pvalue);
						
						if (null != ref) {
							property.setRef(ref);
						}
						bean.getProperties().add(property);
					}
				}
				
				beanMap.put(name, bean);
			}
		}
		
		return beanMap;
	}
	
}
