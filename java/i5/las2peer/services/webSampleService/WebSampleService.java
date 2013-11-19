package i5.las2peer.services.webSampleService;

import i5.las2peer.api.Service;
import i5.las2peer.security.UserAgent;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class WebSampleService extends Service 
{
	private int randomNumber=0;
	
	/**
	 * used in the JS Class to get all the Methods declared in this class  
	 * @return all this class' declared methods as a list of strings
	 */
	public String[] getMethods()
	{
		List<String> allMethods = new ArrayList<String>();
		for ( Method m : WebSampleService.class.getDeclaredMethods()) 
		{
			if ( Modifier.isPublic ( m.getModifiers()) 
				&& !Modifier.isStatic(m.getModifiers()) 
				) 
			{
				allMethods.add(m.getName());
			}
		}
		
		return allMethods.toArray(new String[allMethods.size()]);
	}
	
	/**
	 * used in the JS Class to show the parameters of the given method 
	 * @param methodName the method name  
	 * @return A list with all parameters of the given input method
	 */
	public String[] getParameterTypesOfMethod(String methodName)
	{	
		for ( Method m : WebSampleService.class.getDeclaredMethods()) 
		{
			if ( Modifier.isPublic ( m.getModifiers()) 
					&& !Modifier.isStatic(m.getModifiers()) 
					&& m.getName().equals(methodName) )
					
			{
					Class[] parameterTypesClasses = m.getParameterTypes();
					String[] parameterTypes=new String[parameterTypesClasses.length];
					for(int i=0;i<parameterTypes.length;i++)
					{
						parameterTypes[i]=parameterTypesClasses[i].getSimpleName();
					}
					
					if(parameterTypesClasses.length!=0)
					{
						return parameterTypes;
					}
					
					return null;
			}
		}
		 return new String[]{"No such method declared in the service " + WebSampleService.class.getName() + "."}; 
	}
	/**
	 * test Method that always returns true
	 * @return true
	 */
	public boolean boolTestMethod() 
	{
		return true;
	}
	
	/**
	 * a test Method that returns a formatted string that contains the given string parameter and the user agents' login name 
	 * @param firstMessage the first part of the string that will be returned  
	 * @param ignoredNumber an unused integer demonstrating a method with two different parameters 
	 * @return a formatted string that contains the given input string and the agents login name
	 */
	public String stringIntTestMethod(String firstMessage, int ignoredNumber) 
	{
		UserAgent sendingAgent = (UserAgent) this.getActiveAgent(); 
		return String.format("%s %s", firstMessage, sendingAgent.getLoginName()); 
		// this is the User Agent that was used to log in LAS2Peer
	}
	
	/**
	 * increments the private randomNumber by 1 
	 * @param doNothing an ignored string
	 */
	public void changeRandomNumber(String doNothing)
	{
		randomNumber++;
	}
	/**
	 * a method that returns the randomNumber added to all the input integers
	 * @param a first number
	 * @param b second number 
	 * @param c third number 
	 * @return the sum of all the input numbers added to the private variable randomNumber
	 */
	public int getRandomNumber(int a,int b,int c)
	{
		return randomNumber+a+b+c;
	}
}