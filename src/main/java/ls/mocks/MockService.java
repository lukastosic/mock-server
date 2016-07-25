package ls.mocks;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.SingleRootFileSource;
import com.github.tomakehurst.wiremock.standalone.JsonFileMappingsSource;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import java.util.ArrayList;
import java.util.Properties;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MockService {
	
	public WireMockServer wireMockServer;
	
	private MappingOptions mappingOptions;
	
	public MockService() {
		wireMockServer = new  WireMockServer(wireMockConfig().dynamicPort().dynamicHttpsPort());
	}
	
	public MockService(int httpPort){
		wireMockServer = new  WireMockServer(wireMockConfig().port(httpPort));
	}
	
	public MockService(int httpPort, int httpsPort){
		wireMockServer = new WireMockServer(wireMockConfig().port(httpPort).httpsPort(httpsPort));
	}
	
	public MockService(String address, int port) {
		wireMockServer = new WireMockServer(wireMockConfig().bindAddress(address).port(port).dynamicHttpsPort());		
	}
	
	public MockService(String address, int port, int httpsPort) {
		wireMockServer = new WireMockServer(wireMockConfig().port(port).httpsPort(httpsPort));		
	}
	
	public WireMockServer GetWireMockInstance() {
		return wireMockServer;		
	}
	
	public int GetHttpPort() {
		try {
			return wireMockServer.port();
		}
		catch (Exception ex) {
			return -1;
		}
	}
	
	public int GetHttpsPort() {
		try {
			return wireMockServer.httpsPort();
		}
		catch (Exception ex) {
			return -1;
		}
	}
	
	public void StartMockServer() {
		wireMockServer.start();
	}
	
	public void StopMockServer() {
		wireMockServer.stop();
	}
	
	public void LoadMapppingOptions() {
		mappingOptions = new MappingOptions();		
	}
	
	public void LoadMappingOptions(String pathToPropertiesFile, String pathPrefixOfMappings) {
		mappingOptions = new MappingOptions(pathToPropertiesFile, pathPrefixOfMappings);
	}
	
	public ArrayList<String> ShowMappingOptions() {
		ArrayList<String> options = new ArrayList<String>();
		for(int i=0; i<mappingOptions.mappings.size();i++) {
			options.add(mappingOptions.mappings.get(i).getOption());
		}
		return options;
	}
	
	public void SetMapping(String option) {
		
		String path = "";
		
		for(int i=0; i<mappingOptions.mappings.size();i++) {	
			if(mappingOptions.mappings.get(i).getOption().equals(option)) {
				path = mappingOptions.mappings.get(i).getPath();
			}
		}
		
		if(path != ""){
			wireMockServer.resetMappings();
			wireMockServer.loadMappingsUsing(new JsonFileMappingsSource(new SingleRootFileSource(path)));	
		}
	}
}
