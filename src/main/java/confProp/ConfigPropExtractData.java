package confProp;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class ConfigPropExtractData {
	static Properties prop = new Properties();
	
	public static String getPropValueByKey(String key) throws Exception {
		String path = System.getProperty("user.dir")+"\\src\\test\\resources\\dataProviders\\config.properties";
		FileInputStream fis = new FileInputStream(path);
		prop.load(fis);
		String value = prop.get(key).toString();
		
		if(StringUtils.isEmpty(value)) {
			throw new Exception("Value is not specified for key: "+key+" in config.properties file");
		}
		return value;		
	}

}