package com.materiabot.IO.JSON;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.materiabot.GameElements.Text;

public class JSONParser {
	public static class MyJSONObject{
		private JSONObject json;

		public MyJSONObject(JSONObject o) { json = o; }

		public Boolean getBoolean(String name) { return getInt(name) == 1; }
		public Integer getInt(String name) { try { return json.getInt(name); } catch(Exception e) { return null; }}
		public String getString(String name) { try { return json.getString(name); } catch(Exception e) { return null; }}
		public MyJSONObject getObject(String name) { try { return new MyJSONObject(json.getJSONObject(name)); } catch(Exception e) { return null; }}
		public String[] getStringArray(String name) { 
			try {
				JSONArray arr = name == null ? (JSONArray)(Object)json : json.getJSONArray(name);
				String[] ret = new String[arr.length()];
				for(int i = 0; i < ret.length; i++)
					ret[i] = arr.getString(i);
				return ret;
			} catch(Exception e) {
				return null; 
			}
		}
		public Text getText(MyJSONObject obj) {
			Text t = new Text();
			t.setEn(fix(obj.getString("en")));
			t.setGl(fix(obj.getString("gl")));
			t.setJp(fix(obj.getString("jp")));
			return t;
		}
		private String fix(String c) { //Fix Special Characters
			return c.replace("\\n", System.lineSeparator()).replace("\\bQp", "+");
		}
		
		public Integer[] getIntArray(String name) { 
			try {
				JSONArray arr = name == null ? (JSONArray)(Object)json : json.getJSONArray(name);
				Integer[] ret = new Integer[arr.length()];
				for(int i = 0; i < ret.length; i++)
					ret[i] = arr.getInt(i);
				return ret;
			} catch(Exception e) {
				return null; 
			}
		}
		public MyJSONObject[] getObjectArray(String name) {
			try {
				JSONArray arr = json.getJSONArray(name);
				MyJSONObject[] ret = new MyJSONObject[arr.length()];
				for(int i = 0; i < ret.length; i++)
					ret[i] = new MyJSONObject(arr.getJSONObject(i));
				return ret;
			} catch(Exception e) {
				return null;
			}
		}
		public MyJSONObject[] getObjectArray(String name, int nullValue) {
			try {
				JSONArray arr = json.getJSONArray(name);
				MyJSONObject[] ret = new MyJSONObject[arr.length()];
				for(int i = 0; i < ret.length; i++) {
					try {
						ret[i] = new MyJSONObject(arr.getJSONObject(i));
					}catch(JSONException e) {
						if(arr.getInt(i) == nullValue)
							ret[i] = null;
						else
							throw new RuntimeException("Error parsing an object array \"" + name + "\" with int value different than null value:" + nullValue);
					}
				}
				return ret;
			} catch(Exception e) {
				return null;
			}
		}
		public MyJSONObject[][] getArrayArray(String name) {
			try {
				JSONArray arr = json.getJSONArray(name);
				MyJSONObject[][] ret = new MyJSONObject[arr.length()][];
				for(int i = 0; i < ret.length; i++) {
					ret[i] = new MyJSONObject[arr.getJSONArray(i).length()];
					for(int ii = 0; ii < ret[i].length; ii++)
						ret[i][ii] = new MyJSONObject(arr.getJSONArray(i).getJSONObject(ii));
				}
				return ret;
			} catch(Exception e) {
				return null; 
			}
		}
		
		public JSONObject getJSON() { return json; }
	}

	private static final JSONObject parse(InputStream is) throws IOException{
		BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		StringBuilder sb = new StringBuilder();
		do{
			int cp = rd.read();
			if(cp == -1) break;
			sb.append((char) cp);
			if(cp == '\'')
				sb.append('\'');
		}while(true);
		is.close();
		return new JSONObject(sb.toString());
	}
	private static final JSONObject parseFile(String path) throws IOException {
		try(FileInputStream fis = new FileInputStream(new File(path))) {
			return parse(fis);
		} catch(FileNotFoundException e) {
			throw new IOException(e);
		}
	}
	private static final JSONObject parseURL(String path) throws IOException{
		try {
			return parse(new URL(path).openStream());
		} catch(MalformedURLException e) {
			throw new IOException(e);
		}
	}
	public static final MyJSONObject loadContent(String path, boolean urlTrue_fileFalse) {
		try {
			return new MyJSONObject(urlTrue_fileFalse ? parseURL(path) : parseFile(path));
		} catch (IOException e) {
			return null;
		}
	}
}