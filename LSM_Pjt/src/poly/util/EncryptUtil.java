package poly.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.management.openmbean.InvalidKeyException;

import org.apache.tomcat.util.codec.binary.Base64;

public class EncryptUtil {

	/*
	 * 암호화 알고리즘에 추가시킬 암호화 문구
	 * 
	 * 
	 * 
	 * 일반적인 암호화 알고리즘 SHA-256을 통해서만 암호화 시킬 경우, 암호화 된 값만 보고 일반적인 비밀번호에 대한 값을 쉽게
	 * 예측이 가능함 따라서, 암호화할 때 암호화 되는 값에 추가적인 문자열을 붙여서 함께 암호화를 진행함
	 * 
	 */
	final static String addMessage="PolyDataAnalysis";
	
	/*
	 * AES128-CBC 암호화 알고리즘에 사용되는 초기 벡터와 암호화 키
	 */
	
	//초기벡터
	final static byte[] ivBytes= {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
	
	//AES128-CBC 암호화 알고리즘에 사용되는 키
	final static String key="PolyTechnic12345";
	
	
	/*
	 * 해시 알고리즘(단방향 암호화 알고리즘) SHA-256
	 */
	public static String encHashSHA256(String str) throws Exception {
		String res="";
		String plantText=addMessage+str;
		
		try {
			MessageDigest sh=MessageDigest.getInstance("SHA-256");
			
			sh.update(plantText.getBytes());
			
			byte byteData[]=sh.digest();
			
			StringBuffer sb=new StringBuffer();
			
			for(int i=0;i<byteData.length;i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
				
			}
			res=sb.toString();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
			
			res="";
		}
		return res;
	}
	/*
	 * AES128 CBC 암호화 함수
	 * 
	 */
	public static String encAES128CBC(String str) throws Exception {
		byte[] textBytes=str.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec=new IvParameterSpec(ivBytes);
		SecretKeySpec newKey=new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		Cipher cipher=null;
		cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
		return Base64.encodeBase64String(cipher.doFinal(textBytes));
	}
	
	/*
	 * AES128 CBC 복호화 함수
	 * 
	 */
	public static String decAES128CBC(String str) throws Exception {
		byte[] textBytes=Base64.decodeBase64(str);
		
		AlgorithmParameterSpec ivSpec=new IvParameterSpec(ivBytes);
		SecretKeySpec newKey=new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
		return new String(cipher.doFinal(textBytes), "UTF-8");
	}
}
