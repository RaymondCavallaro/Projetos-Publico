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

import java.util.List;

import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.FileSystemEntry;

public class FakeFileSystemWrapper implements FileSystem {

	protected FakeFileSystemWrapped wrapped;

	public FakeFileSystemWrapper() {
		wrapped = new FakeFileSystemWrapped();
	}

	@Override
	public boolean exists(String path) {
		return wrapped.exists(path);
	}

	@Override
	public boolean isDirectory(String path) {
		return wrapped.isDirectory(path);
	}

	public boolean isDirectory(FileSystemEntry entry) {
		return entry != null && entry.isDirectory();
	}

	@Override
	public boolean isFile(String path) {
		return wrapped.isFile(path);
	}

	@Override
	public void add(FileSystemEntry entry) {
		wrapped.add(entry);
	}

	@Override
	public boolean delete(String path) {
		return wrapped.delete(path);
	}

	@Override
	public void rename(String fromPath, String toPath) {
		wrapped.rename(fromPath, toPath);
	}

	@Override
	public List listFiles(String path) {
		return wrapped.listFiles(path);
	}

	@Override
	public String formatDirectoryListing(FileSystemEntry fileSystemEntry) {
		return wrapped.formatDirectoryListing(fileSystemEntry);
	}

	@Override
	public boolean isAbsolute(String path) {
		return wrapped.isAbsolute(path);
	}

	@Override
	public String path(String path1, String path2) {
		return wrapped.path(path1, path2);
	}

	@Override
	public FileSystemEntry getEntry(String path) {
		return wrapped.getEntry(path);
	}

	@Override
	public String getParent(String path) {
		return wrapped.getParent(path);
	}

	@Override
	public List listNames(String path) {
		return wrapped.listNames(path);
	}
}
