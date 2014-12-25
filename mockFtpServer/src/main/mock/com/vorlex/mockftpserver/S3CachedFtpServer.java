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
package com.vorlex.mockftpserver;

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

/**
 * Main class that can download files from an existing FTP server.
 */
public final class S3CachedFtpServer extends FakeFtpServer {

	private CachedFileSystem localFileSystem;
	private UserCommandHandler loginCommand;
	// private CommandHandler size;
	private StorCommandHandler stor;
	private UserAccount login;

	private S3CachedFtpServer() {
		localFileSystem = new CachedFileSystem();
		setFileSystem(localFileSystem);
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

	public static void main(String[] args) throws Exception {
		S3CachedFtpServer s3FtpServer = new S3CachedFtpServer();
		s3FtpServer.start();
	}
}
