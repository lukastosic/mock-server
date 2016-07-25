package lukastosic.mocks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class MappingOptions {	
		
	private String fileProperties = "mappings/mappings.properties";
	
	private String pathPrefix = "mappings/";
	
	public ArrayList<MappingOption> mappings = new ArrayList<MappingOption>();
	
	public MappingOptions(boolean defaultPluginUsage) {
		if(defaultPluginUsage) {
			fileProperties = "target/maven-shared-archive-resources/mappings/mappings.properties";
			pathPrefix = "target/maven-shared-archive-resources/mappings/";
		}
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
	
	public void LoadMappingsFromPropertiesFile() {	
		try {			
			
			File file = new File(fileProperties);
			System.out.println(file.getAbsolutePath());
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
