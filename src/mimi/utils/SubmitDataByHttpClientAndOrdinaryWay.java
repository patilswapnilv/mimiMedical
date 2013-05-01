package mimi.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * 本类封装了Android中向web服务器提交数据的两种方式四种方法
 */
public class SubmitDataByHttpClientAndOrdinaryWay {

	private static final boolean SHOW_LOG = true;
	private static final String TAG = "SubmitDataByHttpClientAndOrdinaryWay";
	public static final String DEVICEID = "deviceID";
	public static final String LONGITUDE = "Longitude";
	public static final String LATITUDE = "Latitude";
	public static final String INCREASEJSONSTRING = "INCREASEJSONSTRING";
	public static final String DELETEJSONSTRING = "DETELEJSONSTRING";
	public static final String JSONSTRING = "JSONSTRING";
	static HttpClient hc = CustomerHttpClient.getHttpClient();
	/**
	 * 使用get请求以普通方式提交数据
	 * @param map 传递进来的数据，以map的形式进行了封装
	 * @param path 要求服务器servlet的地址
	 * @return 返回的boolean类型的参数
	 * @throws Exception
	 */
	public static Boolean submitDataByDoGet(Map<String, String> map, String path) throws Exception {
		// 拼凑出请求地址
		StringBuilder sb = new StringBuilder(path);
		sb.append("?");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue());
			sb.append("&");
		}
		sb.deleteCharAt(sb.length() - 1);
		String str = sb.toString();
		System.out.println(str);
		URL Url = new URL(str);
		HttpURLConnection HttpConn = (HttpURLConnection) Url.openConnection();
		HttpConn.setRequestMethod("GET");
		HttpConn.setReadTimeout(5000);
		// GET方式的请求不用设置什么DoOutPut()之类的吗？
		if (HttpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return true;
		}
		return false;
	}

	/**
	 * 普通方式的DoPost请求提交数据
	 * @param map 传递进来的数据，以map的形式进行了封装
	 * @param path 要求服务器servlet的地址
	 * @return 返回的boolean类型的参数
	 * @throws Exception
	 */
	public static Boolean submitDataByDoPost(Map<String, String> map, String path) throws Exception {
		// 注意Post地址中是不带参数的，所以newURL的时候要注意不能加上后面的参数
		URL Url = new URL(path);
		// Post方式提交的时候参数和URL是分开提交的，参数形式是这样子的：name=y&age=6
		StringBuilder sb = new StringBuilder();
		// sb.append("?");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue());
			sb.append("&");
		}
		sb.deleteCharAt(sb.length() - 1);
		String str = sb.toString();

		HttpURLConnection HttpConn = (HttpURLConnection) Url.openConnection();
		HttpConn.setRequestMethod("POST");
		HttpConn.setReadTimeout(5000);
		HttpConn.setDoOutput(true);
		HttpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		HttpConn.setRequestProperty("Content-Length", String.valueOf(str.getBytes().length));
		OutputStream os = HttpConn.getOutputStream();
		os.write(str.getBytes());
		if (HttpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return true;
		}
		return false;
	}

	/**
	 * 以HttpClient的DoGet方式向服务器发送请数据
	 * @param map 传递进来的数据，以map的形式进行了封装
	 * @param path 要求服务器servlet的地址
	 * @return 返回的boolean类型的参数
	 * @throws Exception
	 */
	public static String submintDataByHttpClientDoGet(Map<String, String> map, String path) throws ClientProtocolException, IOException {
		// 请求路径
		StringBuilder sb = new StringBuilder(path);
		sb.append("?");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue());
			sb.append("&");
		}
		sb.deleteCharAt(sb.length() - 1);
		String str = sb.toString();
		System.out.println(str);
		HttpGet request = new HttpGet(str);

		HttpResponse response = hc.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		}
		return null;
	}
	
	/**
	 * 以HttpClient的DoPost方式提交数据到服务器
	 * @param map 传递进来的数据，以map的形式进行了封装
	 * @param path 要求服务器servlet的地址
	 * @return 返回的boolean类型的参数
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws Exception
	 */
	public static String submintDataByHttpClientDoPost(Map<String, String> map, String path) throws ClientProtocolException, IOException{
//		HttpClient hc = CustomerHttpClient.getHttpClient();
		// DoPost方式请求的时候设置请求，关键是路径
		HttpPost request = new HttpPost(path);
		// 2. 为请求设置请求参数，也即是将要上传到web服务器上的参数
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			NameValuePair nameValuePairs = new BasicNameValuePair(entry.getKey(), entry.getValue());
			parameters.add(nameValuePairs);
		}
		if(SHOW_LOG) Log.i(TAG, "parameters = " + parameters.toString());
		// 请求实体HttpEntity也是一个接口，我们用它的实现类UrlEncodedFormEntity来创建对象，注意后面一个String类型的参数是用来指定编码的
		HttpEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");
		request.setEntity(entity);
		// 3. 执行请求
		HttpResponse response = hc.execute(request);
		// 4. 通过返回码来判断请求成功与否
		if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		}
		return null;
	}
}
