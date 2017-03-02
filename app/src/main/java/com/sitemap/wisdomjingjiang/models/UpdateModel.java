/**
 * 
 */
package com.sitemap.wisdomjingjiang.models;

/**
 * com.sitemap.na2ne.models.UpdateModel
 * @author zhang
 * 更新
 * create at 2015年12月30日 上午10:41:14
 */
public class UpdateModel {
	private String version;//  版本号（X.X.X）
	private String content;//  更新内容介绍（分点叙述，已“；”进行隔开。）
	private String downloadUrl;//   app下载路径
	 private String isopen;//     该接口是否开启（1：开启，2：关闭）
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getIsopen() {
		return isopen;
	}
	public void setIsopen(String isopen) {
		this.isopen = isopen;
	}
	
}
