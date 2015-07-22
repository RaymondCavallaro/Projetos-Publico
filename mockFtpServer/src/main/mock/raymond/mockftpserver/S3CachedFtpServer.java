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

import org.mockftpserver.core.command.Command;
import org.mockftpserver.core.command.CommandNames;
import org.mockftpserver.core.session.Session;
import org.mockftpserver.core.session.SessionKeys;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.command.StorCommandHandler;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystemEntry;
import org.mockftpserver.stub.command.UserCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import raymond.mockftpserver.Encryptor.EncryptionException;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

/**
 * Main class that can download files from an existing FTP server.
 */
public final class S3CachedFtpServer extends FakeFtpServer {

	private File _accessFile;

	private String charset;
	private String keyBase;
	private String apiKey;
	private String apiKeySecret;
	private String chave;
	private String bucket;
	private Region region = Region.getRegion(Regions.SA_EAST_1);

	private CachedFileSystem localFileSystem;
	private UserCommandHandler loginCommand;
	// private CommandHandler size;
	private StorCommandHandler stor;
	private UserAccount login;

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	public S3CachedFtpServer() {

	}

	public void init(File _accessFile, String chave, String charset,
			String keyBase, String bucket, Region region)
			throws UnsupportedEncodingException {

		this._accessFile = _accessFile;
		this.chave = chave;
		this.charset = charset;
		this.keyBase = keyBase;
		this.bucket = bucket;
		this.region = region;

		login = new UserAccount() {
			@Override
			public boolean canExecute(FileSystemEntry entry) {
				return true;
			}

			@Override
			public boolean canRead(FileSystemEntry entry) {
				return true;
			}

			@Override
			public boolean canWrite(FileSystemEntry entry) {
				return true;
			}
		};
		loginCommand = new UserCommandHandler() {
			public void handleCommand(
					org.mockftpserver.core.command.Command command,
					org.mockftpserver.core.session.Session session,
					org.mockftpserver.core.command.InvocationRecord invocationRecord) {
				super.handleCommand(command, session, invocationRecord);
				String username = command.getRequiredParameter(0);
				session.setAttribute(SessionKeys.USERNAME, username);
				session.setAttribute(SessionKeys.USER_ACCOUNT, login);
				session.setAttribute(SessionKeys.CURRENT_DIRECTORY, "/");
			};
		};
		loginCommand.setPasswordRequired(false);
		setCommandHandler(CommandNames.USER, loginCommand);
		// size = new AbstractFakeCommandHandler() {
		//
		// @Override
		// protected void handle(Command command, Session session) {
		// verifyLoggedIn(session);
		// this.replyCodeForFileSystemException = ReplyCodes.READ_FILE_ERROR;
		//
		// String path = getRealPath(session, command.getRequiredParameter(0));
		// String size;
		// if (localFileSystem.isRoot(path)) {
		// size = quotes("0");
		// } else {
		// FileSystemEntry entry = getFileSystem().getEntry(path);
		// verifyFileSystemCondition(entry != null, path,
		// "filesystem.doesNotExist");
		// if (entry.isDirectory()) {
		// size = quotes("0");
		// } else {
		// size = quotes(Long.toString(entry.getSize()));
		// }
		// }
		// sendReply(session, ReplyCodes.TRANSFER_DATA_FINAL_OK, size);
		// }
		// };
		// setCommandHandler("SIZE", size);
		stor = new StorCommandHandler() {
			@Override
			protected void handle(Command command, Session session) {
				super.handle(command, session);
				String filename = getOutputFile(command);
				String path = getRealPath(session, filename);
				FileEntry file = (FileEntry) getFileSystem().getEntry(path);
				((CachedFileSystem) getFileSystem()).s3.add(file);
			}
		};
		setCommandHandler(CommandNames.STOR, stor);
	}

	public void go() throws EncryptionException, IOException {
		readKeyFile(_accessFile);

		logger.info("inicializando");
		// S3BucketFileSystem s3 = new S3BucketFileSystem();
		// s3.init(apiKey, apiKeySecret, bucket, region);
		// setFileSystem(s3);
		localFileSystem = new CachedFileSystem();

//		S3BucketFileSystem s3 = new S3BucketFileSystem();
//		s3.init(apiKey, apiKeySecret, bucket, region);

		GCSStorageBucketFileSystem s3 = new GCSStorageBucketFileSystem();
		s3.init(apiKey, apiKeySecret, bucket);

		setServerControlPort(22);

		localFileSystem.init(s3);
		setFileSystem(localFileSystem);
		logger.info("fim inicializacao");

		start();
	}

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		String identityPath = (String) ctx.getBean("identityPath");
		String charset = (String) ctx.getBean("charset");
		String keyBase = (String) ctx.getBean("keyBase");
		// String host = (String) ctx.getBean("s3Host");
		String bucket = (String) ctx.getBean("s3Bucket");
		String chave = (String) ctx.getBean("chave");
		Region region = (Region) ctx.getBean("s3Region");

		ctx.close();

		// check if user wants to create credentials
		File _accessFile = new File(identityPath);

		S3CachedFtpServer s3FtpServer = new S3CachedFtpServer();
		s3FtpServer.init(_accessFile, chave, charset, keyBase, bucket, region);

		if (!_accessFile.exists() && args.length != 2) {
			throw new RuntimeException("sem credenciais");
		} else if (!_accessFile.exists()) {
			s3FtpServer.writeKeyFile(args[0], args[1].toCharArray());
		}

		s3FtpServer.go();
	}

	private void writeKeyFile(String accessKey, char[] secretKey)
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

		readKeyFile(_accessFile);
	}

	private void readKeyFile(File _accessFile) throws IOException,
			EncryptionException {
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
}
