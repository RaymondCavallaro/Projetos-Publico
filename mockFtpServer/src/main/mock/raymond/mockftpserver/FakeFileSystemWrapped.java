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

import java.util.ArrayList;
import java.util.List;

import org.mockftpserver.fake.filesystem.FileSystemEntry;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

public class FakeFileSystemWrapped extends UnixFakeFileSystem {

	// private static final Character SEPARATOR_CHAR = '/';

//	@Override
//	protected boolean isValidName(String path) {
//		return true;
//	}

	// @Override
	// protected char getSeparatorChar() {
	// return SEPARATOR_CHAR;
	// }
	//
	// @Override
	// protected boolean isSeparator(char c) {
	// return SEPARATOR_CHAR.equals(c);
	// }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List listNames(String path) {
		List retorno = new ArrayList();
		List<FileSystemEntry> entries = listFiles(path);
		for (FileSystemEntry entry : entries) {
			retorno.add(entry.getName());
		}
		return retorno;
	}
}
