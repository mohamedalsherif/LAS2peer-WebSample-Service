package i5.las2peer.services.webSampleService;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import i5.las2peer.httpConnector.HttpConnector;
import i5.las2peer.httpConnector.client.Client;
import i5.las2peer.p2p.LocalNode;
import i5.las2peer.security.ServiceAgent;
import i5.las2peer.testing.MockAgentFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class webSampleServiceTest 
{
	private static final String HTTP_ADDRESS = "localhost";
	private static final int HTTP_PORT = HttpConnector.DEFAULT_HTTP_CONNECTOR_PORT;

	private LocalNode node;
	private HttpConnector connector;
	private ByteArrayOutputStream logStream;

	private static final String testPass = "adamspass";

	private static final String testServiceClass = "i5.las2peer.services.webSampleService.WebSampleService";

	@Before
	public void startServer() throws Exception {
		// start Node
		node = LocalNode.newNode();
		node.storeAgent(MockAgentFactory.getAdam());
		node.launch();

		ServiceAgent testService = ServiceAgent.generateNewAgent(
				testServiceClass, "password");
		testService.unlockPrivateKey("password");

		node.registerReceiver(testService);

		// start connector
		logStream = new ByteArrayOutputStream();
		connector = new HttpConnector();
		connector.setSocketTimeout(1000);
		connector.setLogStream(new PrintStream(logStream));
		connector.start(node);
	}
	
	@After
	public void shutDownServer() throws Exception {
		System.out.println("After");
		connector.stop();
		System.out.println("Connecter stopped");
		node.shutDown();
		System.out.println("Node shut Down");
		connector = null;
		node = null;

		LocalNode.reset();

		System.out.println("Connector-Log:");
		System.out.println("--------------");

		System.out.println(logStream.toString());
	}
	
	@Test
	public void testMethodCall() {
		Client c = new Client(HTTP_ADDRESS, HTTP_PORT, "adam", testPass);

		try {
			c.connect();
			Object result = c.invoke(testServiceClass, "boolTestMethod");
			assertEquals(true, result);	
			result = c.invoke(testServiceClass, "stringIntTestMethod", "Message", 2);
			assertEquals("Message adam", result);
			
			
			
			result = c.invoke(testServiceClass, "getRandomNumber", 3, 3, 3);
			assertEquals(9, result);
			c.invoke(testServiceClass, "changeRandomNumber", "x");
			result = c.invoke(testServiceClass, "getRandomNumber", 3,4,2);
			assertEquals(10, result);
			
			
			System.out.println("tests ran sucessfully");
			c.disconnect();
			System.out.println("disconnected");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception: " + e);
		}
	}
	
}