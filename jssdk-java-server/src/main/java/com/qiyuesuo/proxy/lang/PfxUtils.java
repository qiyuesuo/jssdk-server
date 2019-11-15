package com.qiyuesuo.proxy.lang;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.security.cert.X509Certificate;

public class PfxUtils {

	public static Map<String, Object> readPfx(byte [] pfxBytes, String password) throws Exception {
		ByteArrayInputStream fis = new ByteArrayInputStream(pfxBytes);
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(fis, password.toCharArray());

		Enumeration enumas = ks.aliases();
		String aliases = null;
		if (enumas.hasMoreElements()) {
			aliases = (String) enumas.nextElement();
		}
		Certificate certificate = ks.getCertificate(aliases);

		Map result = new HashMap();
		PrivateKey privateKey = (PrivateKey) ks.getKey(aliases,password.toCharArray());
		
		result.put("privateKey", privateKey.getEncoded());
		result.put("publicKey", certificate.getEncoded());
		
		X509Certificate cert = X509Certificate.getInstance(certificate.getEncoded());
		Date notBefore = cert.getNotBefore();
		Date notAfter = cert.getNotAfter();

		Principal subjectDN = cert.getSubjectDN();
		result.put("startTime", notBefore);
		result.put("endTime", notAfter);
		result.put("name", getCn(subjectDN.getName()));
		result.put("serialNo", cert.getSerialNumber().toString(16));
		
		
		String issuer = cert.getIssuerDN().getName();
		result.put("issuer", getCn(issuer));
		
		return result;
	}
	
	private static String getCn(String subject) {
		if(StringUtils.isBlank(subject)) {
			return "";
		}
		String [] subjects = subject.split(",");
		Map<String, String> map = new HashMap();
		for (String str : subjects) {
			String [] strs = str.split("=");
			if(strs.length > 1) {
				map.put(strs[0].trim(), strs[1].trim());
			}
		}
		return map.get("CN");
	}
	
}
