/**   
 * @title FileUtils.java
 * @package mimi.sound
 * @description 
 * @author SunQingqing  
 * @update 2012-9-18 下午02:25:01
 * @version V1.0   
 */
package mimi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.os.Environment;

/**
 * @description 
 * @version 1.0
 * @author SunQingqing
 * @update 2012-9-18 下午02:25:01 
 */

public final class FileUtils {
	public static String getAudioPath(){
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "/mimi/" + "audio/";
	}
	
	public static File createAudioFile(){
		File localDir = new File(getAudioPath());
		localDir.mkdirs();
		try {
			return File.createTempFile("audio_", ".3gp", localDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getMIMEType(File file)
	{
		return getMIMEType(file.getName());
	}
	
	public static String getMIMEType(String fileName) {
		String type="*/*";
		int dotIndex = fileName.lastIndexOf(".");
		if(dotIndex < 0){
			return type;
		}
		String end=fileName.substring(dotIndex,fileName.length()).toLowerCase();
		if(end=="")return type;
		for(int i=0;i<MIME_MapTable.length;i++){
			if(end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}
	
	private static  final String[][] MIME_MapTable={
			{".3gp",	"video/3gp"},
			{".apk",	"application/vnd.android.package-archive"},
			{".asf",	"video/x-ms-asf"},
			{".avi",	"video/x-msvideo"},
			{".bin",	"application/octet-stream"},
			{".bmp",  	"image/bmp"},
			{".c",		"text/plain"},
			{".class",	"application/octet-stream"},
			{".conf",	"text/plain"},
			{".cpp",	"text/plain"},
			{".doc",	"application/msword"},
			{".exe",	"application/octet-stream"},
			{".gif",	"image/gif"},
			{".gtar",	"application/x-gtar"},
			{".gz",		"application/x-gzip"},
			{".h",		"text/plain"},
			{".htm",	"text/html"},
			{".html",	"text/html"},
			{".jar",	"application/java-archive"},
			{".java",	"text/plain"},
			{".jpeg",	"image/jpeg"},
			{".jpg",	"image/jpeg"},
			{".js",		"application/x-javascript"},
			{".log",	"text/plain"},
			{".m3u",	"audio/x-mpegurl"},
			{".m4a",	"audio/mp4a-latm"},
			{".m4b",	"audio/mp4a-latm"},
			{".m4p",	"audio/mp4a-latm"},
			{".m4u",	"video/vnd.mpegurl"},
			{".m4v",	"video/x-m4v"},	
			{".mov",	"video/quicktime"},
			{".mp2",	"audio/x-mpeg"},
			{".mp3",	"audio/x-mpeg"},
			{".mp4",	"video/mp4"},
			{".mpc",	"application/vnd.mpohun.certificate"},		
			{".mpe",	"video/mpeg"},	
			{".mpeg",	"video/mpeg"},	
			{".mpg",	"video/mpeg"},	
			{".mpg4",	"video/mp4"},	
			{".mpga",	"audio/mpeg"},
			{".msg",	"application/vnd.ms-outlook"},
			{".ogg",	"audio/ogg"},
			{".pdf",	"application/pdf"},
			{".png",	"image/png"},
			{".pps",	"application/vnd.ms-powerpoint"},
			{".ppt",	"application/vnd.ms-powerpoint"},
			{".prop",	"text/plain"},
			{".rar",	"application/x-rar-compressed"},
			{".rc",		"text/plain"},
			{".rmvb",	"audio/x-pn-realaudio"},
			{".rtf",	"application/rtf"},
			{".sh",		"text/plain"},
			{".tar",	"application/x-tar"},	
			{".tgz",	"application/x-compressed"}, 
			{".txt",	"text/plain"},
			{".wav",	"audio/x-wav"},
			{".wma",	"audio/x-ms-wma"},
			{".wmv",	"audio/x-ms-wmv"},
			{".wps",	"application/vnd.ms-works"},
			{".xml",	"text/xml"},
			{".xml",	"text/plain"},
			{".z",		"application/x-compress"},
			{".zip",	"application/zip"},
			{"",		"*/*"}	
		};
	
	/**
	 * 
	 */
	public static boolean uploadFile(String uploadUrl,String srcPath,String filename){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(uploadUrl);
		post.addHeader("charset",HTTP.UTF_8);
		MultipartEntity me = new MultipartEntity();
		try {
			String part = "";
			String type = getMIMEType(filename);
			//目前音频暂时支持3gp
			if(type.equals("video/3gp")){
				part = "audio";
			}else if(type.equals("image/jpeg") ||type.equals("image/gif")
					||type.equals("image/png") ||type.equals("image/bmp") ){
				part = "pic";
			}
			me.addPart(part, new InputStreamBody(new FileInputStream(srcPath), getMIMEType(filename),filename));
			post.setEntity(me);
			HttpResponse resp = client.execute(post);
			if(resp.getStatusLine().getStatusCode()==200){
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
