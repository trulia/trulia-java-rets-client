package org.realtors.rets.client;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class ChangePasswordRequest extends VersionInsensitiveRequest {
	public ChangePasswordRequest(String username, String oldPassword, String newPassword) throws RetsException {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(username.toUpperCase().getBytes());
			md5.update(oldPassword.toUpperCase().getBytes());
			byte[] digest = md5.digest();
			DESKeySpec keyspec = new DESKeySpec(digest);
			SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keyspec);
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipher.update(newPassword.getBytes());
			cipher.update(":".getBytes());
			cipher.update(username.getBytes());
			md5.reset();
			md5.update(cipher.doFinal());
			byte[] output = md5.digest();
			byte[] param = Base64.encodeBase64(output);
			setQueryParameter("PWD", new String(param));
		} catch (GeneralSecurityException e) {
			throw new RetsException(e);
		}
	}

	@Override
	public void setUrl(CapabilityUrls urls) {
		this.setUrl(urls.getChangePasswordUrl());
	}
}
