package ls.mocks;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ls.mocks.MockService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.ContentEncodingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Unit test for simple App.
 */
public class MockServiceTest 
    extends TestCase
{
    public MockServiceTest( String testName )
    {
        super( testName );
    }
  
    public static Test suite()
    {
        return new TestSuite( MockServiceTest.class );
    }
    
    private MockService mockService = new MockService();
    
    public void prepareMockServer() {    	
    	mockService.StartMockServer();
    	mockService.LoadMappingOptions("src/test/resources/mappings/test-mappings.properties", "src/test/resources/mappings/");
    }
    
    @SuppressWarnings("deprecation")
	public void testLoadingOptions() throws ClientProtocolException, IOException
    {
    	prepareMockServer();
    	ArrayList<String> options = mockService.ShowMappingOptions();
    	mockService.StartMockServer();
    	
    	assertTrue("Should be one option in test file",options.size() == 1);
    }
    
    public void testExecutingMcok() throws ClientProtocolException, IOException {
    	
    	prepareMockServer();
    	mockService.SetMapping("TEST_MAPPINGS");
    	
    	HttpClient client = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://localhost:"+mockService.GetHttpPort()+"/verify");
    	HttpResponse response = client.execute(post);
    	mockService.StopMockServer();
    	assertTrue("Received status 200",response.getStatusLine().getStatusCode()==200);
    }
    
    public void testMainFile() throws ClientProtocolException, IOException {
    	mockService.StartMockServer();
    	mockService.LoadMapppingOptions();
    	
    	//prepareMockServer();
    	mockService.SetMapping("AUTHENTICATION_SERVICE_STANDARD_SETUP");
    	
    	HttpClient client = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://localhost:"+mockService.GetHttpPort()+"/verify");
    	HttpResponse response = client.execute(post);
    	assertTrue("Received status 401",response.getStatusLine().getStatusCode()==401);
    }
}
