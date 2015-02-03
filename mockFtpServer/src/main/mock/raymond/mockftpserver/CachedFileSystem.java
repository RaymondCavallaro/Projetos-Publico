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

import java.util.List;

import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystemEntry;

import com.amazonaws.regions.Region;

public class CachedFileSystem extends FakeFileSystemWrapper {

	private static final String ROOT_FOLDER = "/";

	protected S3BucketFileSystem s3;

	public CachedFileSystem() {
		super();
	}

	public void init(String apiKey, String apiKeySecret, String bucket,
			Region region) {
		s3 = new S3BucketFileSystem();
		s3.init(apiKey, apiKeySecret, bucket, region);
		List<FileSystemEntry> entries = s3.listFilesRoot();
		if (entries.isEmpty()) {
			super.add(new DirectoryEntry(ROOT_FOLDER));
		} else {
			for (FileSystemEntry entry : entries) {
				if (entry.isDirectory()) {
					super.add(new DirectoryEntry(entry.getPath()));
				} else {
					super.add(new FileEntry(entry.getPath()));
				}
			}
		}
	}

	@Override
	public void add(FileSystemEntry entry) {
		if (isDirectory(entry)) {
			s3.add(entry);
		}
		// se incluir no s3 inclui no cache
		super.add(entry);
	}

	// public FileSystemEntry getEntry(String path) {
	// FileSystemEntry cached = super.getEntry(path);
	// if ((cached == null) || (isFile(path) && cached.getSize() == 0)) {
	// if (isFile(path)) {
	// // busca no s3
	// S3Object obj = s3.getObject(new GetObjectRequest(bucket, path));
	// if (obj != null) {
	// FileEntry file;
	// if (cached == null) {
	// file = new FileEntry(path);
	// } else {
	// file = (FileEntry) cached;
	// }
	// try {
	// try {
	// IOUtils.copy(obj.getObjectContent(),
	// file.createOutputStream(false));
	// } finally {
	// obj.close();
	// }
	// cached = file;
	// super.add(cached);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }
	// return cached;
	// }

	@Override
	public boolean delete(String path) {
		s3.delete(getEntry(path));
		// se deletar no s3 deleta no cache
		return super.delete(path);
	}

	@Override
	public void rename(String fromPath, String toPath) {
		FileSystemEntry entry = super.getEntry(fromPath);
		s3.rename(entry, toPath);
		// se renomear no s3 renomeia no cache
		super.rename(fromPath, toPath);
	}
}