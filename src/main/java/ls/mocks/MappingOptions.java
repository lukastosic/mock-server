package ls.mocks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class MappingOptions {

	private String fileProperties = "src/main/resources/mappings/mappings.properties";
	
	private String pathPrefix = "src/main/resources/mappings/";
	
	public ArrayList<MappingOption> mappings = new ArrayList<MappingOption>();
	
	public MappingOptions() {
		LoadMappingsFromPropertiesFile();
	}
	
	public MappingOptions(String pf, String pp) {
		fileProperties = pf;
		pathPrefix = pp;
		LoadMappingsFromPropertiesFile();
	}
	
	public void LoadMappingsFromPropertiesFile(String pf, String pp) {
		fileProperties = pf;
		pathPrefix = pp;
		LoadMappingsFromPropertiesFile();
	}
	
	private void LoadMappingsFromPropertiesFile() {	
		try {
			File file = new File(fileProperties);
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();
			
			mappings.clear();
			
			Enumeration enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String option = (String) enuKeys.nextElement();
				String path = pathPrefix + properties.getProperty(option);
				mappings.add(new MappingOption(option, path));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();			
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}
	
	
	
	
}
