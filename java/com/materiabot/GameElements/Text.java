package com.materiabot.GameElements;
import com.materiabot.Utils.MessageUtils;

public class Text {
	private String en;
	private String gl;
	private String jp;
	
	public Text() {}
	public Text(String fake) { en = gl = jp = fake; }

	public String getEn() { return en; }
	public void setEn(String en) { this.en = en; }
	public String getGl() { return gl; }
	public void setGl(String gl) { this.gl = gl; }
	public String getJp() { return jp; }
	public void setJp(String jp) { this.jp = jp; }
	
	public String getBest() {
		if(gl != null && gl.length() > 0) return getGl();
		if(en != null && en.length() > 0) return getEn();
		if(jp != null && jp.length() > 0) return getJp();
		return MessageUtils.NOTEXT;
	}
}