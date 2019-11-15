package com.qiyuesuo.proxy.http;

import com.qiyuesuo.proxy.json.JSONUtils;
import com.qiyuesuo.proxy.lang.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;



/**
 * 参数对象封装,包含文本参数和文件参数
 */
public class HttpParamers {
	
	private Map<String,String> params = new HashMap<String, String>();
	private Map<String, FileItem> files = new HashMap<String, FileItem>();
	private Map<String, List<FileItem>> listFiles = new HashMap<String, List<FileItem>>();
	private HttpMethod httpMethod;
	
	
	public HttpParamers(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}
	
	/**
	 * 创建POST请求参数
	 * @return
	 */
	public static HttpParamers httpPostParamers(){
		return new HttpParamers(HttpMethod.POST);
	}
	
	/**
	 * 创建GET请求参数
	 * @return
	 */
	public static HttpParamers httpGetParamers(){
		return new HttpParamers(HttpMethod.GET);
	}
	
	/**
	 * 添加文本参数
	 * @param name 参数名
	 * @param value 参数值
	 * @return
	 */
	public HttpParamers addParam(String name,String value){
		this.params.put(name, value);
		return this;
	}
	
	public void addParams(Map<String, String> datas) {
		for(Map.Entry<String,String> data:datas.entrySet()) {
			this.params.put(data.getKey(),data.getValue());
		}
	}
	
	/**
	 * 添加文件参数
	 * @param name 参数名
	 * @param file 文件对象
	 * @return
	 */
	public HttpParamers addFile(String name,FileItem file){
		this.files.put(name, file);
		return this;
	}
	
	public HttpParamers addListFiles(String name,List<FileItem> fileItems){
		this.listFiles.put(name, fileItems);
		return this;
	}
	
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	/**
	 * 将参数拼接为a=1&b=2...格式
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public String getQueryString(String charset) throws IOException {
		if (params == null || params.isEmpty()) {
			return null;
		}
		StringBuilder query = new StringBuilder();
		Set<Entry<String, String>> entries = params.entrySet();

		for (Entry<String, String> entry : entries) {
			String name = entry.getKey();
			String value = entry.getValue();
			// 忽略参数名或参数值为空的参数
			if (StringUtils.isNoneEmpty(name, value)) {
				query.append("&");
				query.append(name).append("=").append(URLEncoder.encode(value, charset));
			}
		}
		return query.substring(1);
	}
	
	/**
	 * 参数是否是包含附件
	 * @return
	 */
	public boolean isMultipart() {
		return !files.isEmpty()||!listFiles.isEmpty();
	}
	
	public Map<String, String> getParams() {
		return params;
	}

	public Map<String, FileItem> getFiles() {
		return files;
	}
	
	public Map<String, List<FileItem>> getListFiles(){
		return listFiles;
	}

	@Override
	public String toString() {
		return "HttpParamers "+ JSONUtils.toJson(this);
	}

}
