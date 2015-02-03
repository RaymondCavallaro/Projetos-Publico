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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystemEntry;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.StorageClass;

public class S3BucketFileSystem extends FakeFileSystemWrapper {

	private static final String FOLDER_SUFFIX = "/";
	private static final String ROOT_FOLDER = "//";

	private AmazonS3 s3;

	private String bucket;

	public S3BucketFileSystem() {
	}

	public void init(String apiKey, String apiKeySecret, String bucket,
			Region region) {
		this.bucket = bucket;
		s3 = new AmazonS3Client(new BasicAWSCredentials(apiKey, apiKeySecret));
		s3.setRegion(region);
	}

	@Override
	public boolean isDirectory(String path) {
		return path.endsWith(FOLDER_SUFFIX);
	}

	@Override
	public boolean isFile(String path) {
		return !isDirectory(path);
	}

	protected boolean isRoot(String pathComponent) {
		return ROOT_FOLDER.equals(pathComponent);
	}

	@Override
	public void add(FileSystemEntry entry) {
		ObjectMetadata metaData = new ObjectMetadata();
		PutObjectRequest request;
		if (isDirectory(entry)) {
			metaData.setContentLength(0);
			InputStream is = new ByteArrayInputStream(new byte[0]);
			request = new PutObjectRequest(bucket, entry.getPath()
					+ FOLDER_SUFFIX, is, metaData);
		} else {
			metaData.setContentLength(entry.getSize());
			request = new PutObjectRequest(bucket, entry.getPath(),
					((FileEntry) entry).createInputStream(), metaData);
		}
		request.setStorageClass(StorageClass.ReducedRedundancy);
		s3.putObject(request);
	}

	@Override
	public FileSystemEntry getEntry(String path) {
		if (isFile(path)) {
			S3Object obj = s3.getObject(new GetObjectRequest(bucket, path));
			if (obj != null) {
				FileEntry file = new FileEntry(path);
				try {
					try {
						IOUtils.copy(obj.getObjectContent(),
								file.createOutputStream(false));
					} finally {
						obj.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return file;
			}
		}
		return null;
	}

	public boolean delete(FileSystemEntry entry) {
		String sufix = entry.isDirectory() ? FOLDER_SUFFIX : "";
		s3.deleteObject(new DeleteObjectRequest(bucket, entry.getPath() + sufix));
		return true;
	}

	public void rename(FileSystemEntry entry, String toPath) {
		String sufix = entry.isDirectory() ? FOLDER_SUFFIX : "";
		s3.copyObject(bucket, entry.getPath() + sufix, bucket, toPath + sufix);
		delete(entry);
	}

	public List<FileSystemEntry> listFilesRoot() {
		List<FileSystemEntry> retorno = new ArrayList<FileSystemEntry>();
		// if (isDirectory(path)) {
		// ObjectListing listing = isRoot(path) ? s3.listObjects(bucket)
		// : s3.listObjects(bucket, path);
		ObjectListing listing = s3.listObjects(bucket);
		for (S3ObjectSummary summary : listing.getObjectSummaries()) {
			String summaryPath = summary.getKey();
			FileSystemEntry entry;
			if (isDirectory(summaryPath)) {
				entry = new DirectoryEntry(summaryPath.substring(0,
						summaryPath.length() - 1));
			} else {
				entry = new FileEntry(summaryPath);
			}
			retorno.add(entry);
		}
		return retorno;
	}

	@Override
	public boolean delete(String path) {
		delete(getEntry(path));
		return true;
	}

	@Override
	public void rename(String fromPath, String toPath) {
		FileSystemEntry entry = super.getEntry(fromPath);
		rename(entry, toPath);
	}

	@Override
	public List listFiles(String path) {
		throw new RuntimeException();
	}

	@Override
	public List listNames(String path) {
		throw new RuntimeException();
	}

	@Override
	public String formatDirectoryListing(FileSystemEntry fileSystemEntry) {
		throw new RuntimeException();
	}

	@Override
	public boolean exists(String path) {
		throw new RuntimeException();
	}

	@Override
	public boolean isAbsolute(String path) {
		throw new RuntimeException();
	}

	@Override
	public String path(String path1, String path2) {
		throw new RuntimeException();
	}

	@Override
	public String getParent(String path) {
		throw new RuntimeException();
	}
}
