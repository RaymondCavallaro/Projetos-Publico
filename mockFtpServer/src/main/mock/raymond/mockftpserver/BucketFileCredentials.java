/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package raymond.mockftpserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import raymond.mockftpserver.Encryptor.EncryptionException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

/**
 * Main class that can download files from an existing FTP server.
 */
public final class BucketFileCredentials {

	private File _accessFile;

	private String credencialsFilePath;
	private String charset;
	private String keyBase;
	private String chave;
	private String accessKey;
	private String secretKey;

	private String apiKey;
	private String apiKeySecret;

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	public BucketFileCredentials() {

	}

	public AWSCredentials createAWSCredentials() {
		return new BasicAWSCredentials(apiKey, apiKeySecret);
	}

	public void init() throws IOException, EncryptionException {
		_accessFile = new File(credencialsFilePath);

		if (_accessFile.exists()) {
			readKeyFile();
		} else {
			writeKeyFile(accessKey, secretKey.toCharArray());
		}
	}

	public void writeKeyFile(String accessKey, char[] secretKey)
			throws UnsupportedEncodingException, IOException,
			EncryptionException {
		byte[] nameRaw = chave.getBytes(charset);
		byte[] accessKeyRaw = accessKey.getBytes(charset);

		if (nameRaw.length > 0xff) {
			throw new RuntimeException("name too long");
		}
		if (accessKeyRaw.length > 0xff) {
			throw new RuntimeException("accessKey too long");
		}

		byte[] cleartext = new byte[2 + nameRaw.length + accessKeyRaw.length
				+ secretKey.length * 2];
		cleartext[0] = (byte) nameRaw.length;
		cleartext[1] = (byte) accessKeyRaw.length;
		for (int i = 0; i < nameRaw.length; i++) {
			cleartext[i + 2] = nameRaw[i];
		}
		int offset = nameRaw.length + 2;
		for (int i = 0; i < accessKeyRaw.length; i++) {
			cleartext[i + offset] = accessKeyRaw[i];
		}
		offset += accessKeyRaw.length;
		for (int i = 0; i < secretKey.length; i++) {
			char ch = secretKey[i];
			int spot = i + i;
			cleartext[offset + spot] = (byte) (ch >> 8); // msb
			cleartext[offset + spot + 1] = (byte) (ch); // lsb
		}

		Encryptor crypt = new ThreeDESEncryptor(chave + keyBase);
		byte[] cipher = crypt.encrypt(cleartext);

		logger.info("@@ writing cypher in:" + _accessFile);
		logger.info("@@ len written is:" + cipher.length);

		_accessFile.createNewFile();
		OutputStream out = new FileOutputStream(_accessFile);
		out.write(cipher);
		out.close();

		readKeyFile();
	}

	public void readKeyFile() throws IOException, EncryptionException {
		logger.info("@@ reading cypher from:" + _accessFile);
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				_accessFile));
		int len = in.available();
		byte[] buffer = new byte[len];
		in.read(buffer);
		in.close();
		logger.info("@@ len read is:" + len);

		byte[] cleartext;

		Encryptor crypt = new ThreeDESEncryptor(chave + keyBase);
		cleartext = crypt.decrypt(buffer);

		int nameLen = cleartext[0];
		int accessKeyLen = cleartext[1];
		int secretKeyLen = cleartext.length - 2 - nameLen - accessKeyLen;
		if (secretKeyLen % 2 != 0) {
			throw new RuntimeException(
					"internal error: secret key has odd bytecount");
		}

		byte[] nameRaw = new byte[nameLen];
		for (int i = 0; i < nameLen; i++) {
			nameRaw[i] = cleartext[i + 2];
		}
		String nameCheck = new String(nameRaw, charset);
		if (!chave.equals(nameCheck)) {
			throw new RuntimeException(
					"internal error: secret key name mismatch");
		}

		// Get the Access Key
		int offset = 2 + nameLen;
		byte[] accessKeyRaw = new byte[accessKeyLen];
		for (int i = 0; i < accessKeyLen; i++) {
			accessKeyRaw[i] = cleartext[i + offset];
		}
		apiKey = new String(accessKeyRaw, charset);

		// Get the Secret Key
		char[] secretKey = new char[secretKeyLen / 2];
		offset += accessKeyLen;
		for (int i = 0; i < secretKeyLen / 2; i++) {
			int spot = i + i + offset;
			char ch = (char) ((cleartext[spot] << 8) + cleartext[spot + 1]);
			secretKey[i] = ch;
		}
		apiKeySecret = new String(secretKey);
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getKeyBase() {
		return keyBase;
	}

	public void setKeyBase(String keyBase) {
		this.keyBase = keyBase;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getCredencialsFilePath() {
		return credencialsFilePath;
	}

	public void setCredencialsFilePath(String credencialsFilePath) {
		this.credencialsFilePath = credencialsFilePath;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

}
