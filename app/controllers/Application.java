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

    public static void main(String args[]) throws JsonParseException, JsonMappingException, IOException, JAXBException{
 
    	String xmlIn = "<widgetOutput><dataitems highFreq='0'><dataitem><isSphinxLimit>false</isSphinxLimit></dataitem><dataitem><CLOUDSize>1000</CLOUDSize>" +
    			"</dataitem><dataitem><key><![CDATA['using']]></key><value>51</value></dataitem><dataitem><key><![CDATA['technology']]></key><value>48</value></dataitem>" +
    			"<dataitem><key><![CDATA['buy']]></key><value>38</value></dataitem><dataitem><key><![CDATA['customer']]></key><value>53</value></dataitem>" +
    			"<dataitem><key><![CDATA['twitter']]></key><value>68</value></dataitem><dataitem><key><![CDATA['software']]></key><value>58</value></dataitem>" +
    			"<dataitem><key><![CDATA['time']]></key><value>59</value></dataitem><dataitem><key><![CDATA['thanks']]></key><value>70</value></dataitem>" +
    			"<dataitem><key><![CDATA['tools']]></key><value>44</value></dataitem><dataitem><key><![CDATA['social']]></key><value>215</value></dataitem>" +
    			"<dataitem><key><![CDATA['sales']]></key><value>38</value></dataitem><dataitem><key><![CDATA['facebook']]></key><value>68</value></dataitem>" +
    			"<dataitem><key><![CDATA['industry']]></key><value>59</value></dataitem><dataitem><key><![CDATA['viral']]></key><value>51</value></dataitem>" +
    			"<dataitem><key><![CDATA['platform']]></key><value>67</value></dataitem><dataitem><key><![CDATA['services']]></key><value>57</value></dataitem>" +
    			"<dataitem><key><![CDATA['million']]></key><value>49</value></dataitem><dataitem><key><![CDATA['isn']]></key><value>50</value></dataitem>" +
    			"<dataitem><key><![CDATA['white']]></key><value>53</value></dataitem><dataitem><key><![CDATA['companies']]></key><value>59</value></dataitem>" +
    			"<dataitem><key><![CDATA['enterprise']]></key><value>49</value></dataitem><dataitem><key><![CDATA['london']]></key><value>51</value></dataitem>" +
    			"<dataitem><key><![CDATA['announced']]></key><value>39</value></dataitem><dataitem><key><![CDATA['@radian6']]></key><value>243</value></dataitem>" +
    			"<dataitem><key><![CDATA['company']]></key><value>75</value></dataitem><dataitem><key><![CDATA['salesforce']]></key><value>133</value></dataitem>" +
    			"<dataitem><key><![CDATA['media']]></key><value>192</value></dataitem><dataitem><key><![CDATA['buddy']]></key><value>103</value></dataitem>" +
    			"<dataitem><key><![CDATA['heroku']]></key><value>258</value></dataitem><dataitem><key><![CDATA['queen']]></key><value>48</value></dataitem>" +
    			"<dataitem><key><![CDATA['fans']]></key><value>59</value></dataitem><dataitem><key><![CDATA['oracle']]></key><value>50</value></dataitem>" +
    			"<dataitem><key><![CDATA['business']]></key><value>45</value></dataitem><dataitem><key><![CDATA['online']]></key><value>54</value></dataitem>" +
    			"<dataitem><key><![CDATA['ceo']]></key><value>39</value></dataitem><dataitem><key><![CDATA['looking']]></key><value>37</value></dataitem>" +
    			"<dataitem><key><![CDATA['brands']]></key><value>66</value></dataitem><dataitem><key><![CDATA['acquisition']]></key><value>52</value></dataitem>" +
    			"<dataitem><key><![CDATA['crm']]></key><value>56</value></dataitem><dataitem><key><![CDATA['rain']]></key><value>47</value></dataitem><dataitem>" +
    			"<key><![CDATA['look']]></key><value>37</value></dataitem><dataitem><key><![CDATA['app']]></key><value>39</value></dataitem>" +
    			"<dataitem><key><![CDATA['radian6']]></key><value>279</value></dataitem><dataitem><key><![CDATA['marketing']]></key><value>84</value></dataitem>" +
    			"<dataitem><key><![CDATA['news']]></key><value>52</value></dataitem><dataitem><key><![CDATA['customers']]></key><value>46</value></dataitem>" +
    			"<dataitem><key><![CDATA['@heroku']]></key><value>63</value></dataitem><dataitem><key><![CDATA['great']]></key><value>50</value></dataitem>" +
    			"<dataitem><key><![CDATA['red']]></key><value>48</value></dataitem><dataitem><key><![CDATA['cloud']]></key><value>74</value></dataitem>" +
    			"<status>complete</status></dataitems></widgetOutput>";
    	/*
    	String xmlIn = "<auth><token>415cb01dbad3a986b9f84e0edac56d829e3f199c3214f13c15a12a968b843f7c</token><UserDetails><user><userId>72748</userId>" +
    			"<clientId>6016</clientId><displayName><![CDATA[Anand Narasimhan]]></displayName><emailAddress>anand@heroku.com</emailAddress><timezone>GMT</timezone>" +
    			"<packages></packages><userRoleId>2</userRoleId><createdDate>Mar 06, 2012 09:55 PM</createdDate><enabled>true</enabled><aihUsers><aihUser>" +
    			"<userKey>42914f92e520d63d121174bda6d5cce1</userKey><registerDate>2012</registerDate><type>1</type></aihUser></aihUsers></user><avatar />" +
    			"<Packages></Packages></UserDetails></auth>";
    	*/
    }


}