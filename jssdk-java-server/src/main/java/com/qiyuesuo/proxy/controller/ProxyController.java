package com.qiyuesuo.proxy.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.qiyuesuo.proxy.http.HttpClient;
import com.qiyuesuo.proxy.http.HttpHeader;
import com.qiyuesuo.proxy.http.HttpParamers;
import com.qiyuesuo.proxy.http.StreamFile;
import com.qiyuesuo.proxy.lang.MD5;

@Controller
@RequestMapping("/proxy")
public class ProxyController {

	private String forwardingHost;

	private String accessToken;

	private String accessSecret;
	
	
	private static final int CONNECT_TIMEOUT = 15000;
	private static final int READ_TIMEOUT = 100000;

	/**
	 * 初始化转发服务地址
	 * 使用前必须实例化以下三个参数：
	 * forwardingHost：转发地址，测试环境地址：https://openapi.qiyuesuo.cn、生产环境地址：https://openapi.qiyuesuo.com
	 * accessToken：开放平台申请所得的Token
	 * accessSecret：开放平台申请所得的Secret
	 */
	protected void init() {
		this.forwardingHost = "https://openapi.qiyuesuo.cn";
		this.accessToken = "v7Xb9KNogN";
		this.accessSecret = "kAt5C7ibSuoZiM5ixOUP0NbjmSmkvX";
//		this.accessToken = "wcO4jiIt4P";
//		this.accessSecret = "0MDA5ahiZmKsfznVWY1JpYw50EMJCA";
//		this.accessToken = "ocy2nnWLgl";
//		this.accessSecret = "8SrxjhAc9uUfQj2BJ9NehnSmVbRetb";
//		this.accessToken = "6kUVn64VpE";
//		this.accessSecret = "avKo0Or9CZnDlfmAGptaBOzPbXX4Dm";
	}

	ProxyController() {
		this.init();
	}

	/**
	 * Post请求处理逻辑业务，也直接返回生成的流
	 * 
	 * @param file
	 * @param requestData
	 * @param response
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void doPost(MultipartFile file, String requestData, HttpServletResponse response) {
		HttpClient client = new HttpClient(CONNECT_TIMEOUT,READ_TIMEOUT);
		try {
			HttpParamers paramers = HttpParamers.httpPostParamers();
			paramers.addParam("requestData", requestData);
			if (file != null) {
				paramers.addFile("file", new StreamFile(file.getInputStream()));
			}
			String forwardingAddress = new StringBuilder(forwardingHost).append("/uniformapi").toString();
			String responseCharSequence = client.doPost(forwardingAddress, paramers, getHeader());
			response.getWriter().write(responseCharSequence);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get请求处理下载事务，直接将下载流返回
	 * 
	 * @param requestData
	 * @param response
	 */
	@RequestMapping(method = RequestMethod.GET)
	public void doDownload(String requestData, HttpServletResponse response) {
		HttpClient client = new HttpClient(CONNECT_TIMEOUT,READ_TIMEOUT);
		try {
			HttpParamers paramers = HttpParamers.httpGetParamers();
			paramers.addParam("requestData", requestData);
			String forwardingAddress = new StringBuilder(forwardingHost).append("/uniformapi").toString();
			client.doDownload(forwardingAddress, paramers, getHeader(), response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成请求头
	 * 所有开放平台的API接口，header中必须添加下面三个参数，用以校验身份。
	 * （1）x-qys-open-accesstoken： 用来确认对接方身份，是申请开放平台后获取的 AppToken。
	 * （2）x-qys-open-timestamp： 时间戳。
	 * （3）x-qys-open-signature： 签名数据，用来校验请求是否有效。生成方式：Md5(AppToken + AppSecret + timestamp)，即AppToken 和 AppSecret 和
	 * 时间戳拼接的字符串的MD5值。
	 * 
	 * @return
	 */
	private HttpHeader getHeader() {
		String timeStamp = String.valueOf(System.currentTimeMillis());
		String signature = MD5
				.toMD5(new StringBuilder(this.accessToken).append(this.accessSecret).append(timeStamp).toString());
		HttpHeader httpHeader = new HttpHeader();
		httpHeader.addProperty("x-qys-open-timestamp", timeStamp);
		httpHeader.addProperty("x-qys-open-signature", signature);
		httpHeader.addProperty("x-qys-open-accesstoken", this.accessToken);
		return httpHeader;
	}

}
