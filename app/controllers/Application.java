package controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.xml.sax.InputSource;

import antlr.collections.List;


import play.libs.WS;
import play.mvc.Controller;
import radian6.Auth;
import radian6.tagcloud.DataItem;
import radian6.tagcloud.WidgetOutput;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import utils.JedisPoolFactory;

public class Application extends Controller {
	static JedisPoolFactory poolFactory = new JedisPoolFactory();
	private static String R6_AUTH_URL="http://api.radian6.com/socialcloud/v1/auth/authenticate";
	private static String R6_TAG_CLOUD_URL = "http://api.radian6.com/socialcloud/v1/data/tagclouddata/162/370934/1,2,4,5,8,10,9,11,12,13,14,16/0:1?extendedMediaTypes=2,3,4";
	private static String AUTH_TOKEN= "AUTH_TOKEN";
	private static String TAG_CLOUD= "TAG_CLOUD";
	private static int EXPIRY_SECS = 10*60;
	private static WidgetOutput ERROR_WIDGET;
	
	static{
		ERROR_WIDGET=new WidgetOutput();
		ERROR_WIDGET.add(new DataItem("#Error",50));
	}
    public static void index() {
        render();
    }
    
    public static void tagCloud(){
		JedisPool pool = poolFactory.getPool();
	    Jedis jedis = pool.getResource();
	    String cachedCloud=null,authToken=null;
	    try{
	    	cachedCloud = jedis.get(TAG_CLOUD);
	    	if(cachedCloud==null){
	    		authToken = jedis.get(AUTH_TOKEN);
	    		if(authToken ==null){
	    			authToken = getAuthToken();
	    			jedis.set(AUTH_TOKEN, authToken);
	    			jedis.expire(AUTH_TOKEN, EXPIRY_SECS);
	    		}
	    		cachedCloud = getTagCloudData(authToken);
    			jedis.set(TAG_CLOUD, cachedCloud);
    			jedis.expire(TAG_CLOUD, EXPIRY_SECS);
	    	}
	    	renderJSON(cachedCloud);
	    } catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			renderJSON(ERROR_WIDGET);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			renderJSON(ERROR_WIDGET);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			renderJSON(ERROR_WIDGET);
		}finally{
	    	pool.returnResource(jedis);
	    }
    }

    private static String getAuthToken() throws JAXBException{
    	String token=null;
    	/*
    	 * curl -v  -H "auth_user:<username>" 
    	 *          -H "auth_pass:<md5 hash of password>" 
    	 *          -H "auth_appkey:r8g45345cv5neqdg8qjvb9fr" 
    	 * http://api.radian6.com/socialcloud/v1/auth/authenticate
    	 * 
    	 */
    	WS.HttpResponse response	 = WS.url(R6_AUTH_URL)
			    						 .setHeader("auth_user",System.getenv("R6_USERNAME"))
			    						 .setHeader("auth_pass", System.getenv("R6_PASSWORD_MD5"))
			    						 .setHeader("auth_appkey", System.getenv("R6_APP_KEY"))
			    						 .get();
    	ByteArrayInputStream byteIn = new ByteArrayInputStream(response.getString().getBytes());
       	JAXBContext context = JAXBContext.newInstance(Auth.class);
        // parse the XML and return an instance of the WidgetOutput class
       	Auth auth = (Auth) context.createUnmarshaller().unmarshal(byteIn);
    	return auth.getToken();
    }
    
    private static String getTagCloudData(String authToken) throws JAXBException, JsonGenerationException, JsonMappingException, IOException{
    	WS.HttpResponse response	 = WS.url(R6_TAG_CLOUD_URL)
    									 .setHeader("auth_appkey", System.getenv("R6_APP_KEY"))
			    						 .setHeader("auth_token",authToken)
			    						 .setHeader("Host", "api.radian6.com")
			    						 .get();
    	System.out.println("-------------- XML RESPONSE START--------------");
    	System.out.println(response.getString());
    	System.out.println("-------------- XML RESPONSE END  --------------");
    	ByteArrayInputStream byteIn = new ByteArrayInputStream(response.getString().getBytes());
       	JAXBContext context = JAXBContext.newInstance(WidgetOutput.class);
        // parse the XML and return an instance of the WidgetOutput class
    	WidgetOutput widgetOut = (WidgetOutput) context.createUnmarshaller().unmarshal(byteIn);
    	ObjectMapper mapper = new ObjectMapper();
    	ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    	mapper.writeValue(byteOut,widgetOut);
    	return byteOut.toString();
    }
}