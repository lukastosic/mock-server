package ls.mocks;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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
   
}
