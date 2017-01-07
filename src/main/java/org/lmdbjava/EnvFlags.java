/*-
 * #%L
 * LmdbJava
 * %%
 * Copyright (C) 2016 - 2017 The LmdbJava Open Source Project
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package org.lmdbjava;

/**
 * Flags for use when opening the {@link Env}.
 */
public enum EnvFlags implements MaskedFlag {

  /**
   * Mmap at a fixed address (experimental).
   */
  /**
   * Mmap at a fixed address (experimental).
   */
  MDB_FIXEDMAP(0x01),
  /**
   * No environment directory.
   */
  MDB_NOSUBDIR(0x4000),
  /**
   * Don't fsync after commit.
   */
  MDB_NOSYNC(0x1_0000),
  /**
   * Read only.
   */
  MDB_RDONLY_ENV(0x2_0000),
  /**
   * Don't fsync metapage after commit.
   */
  MDB_NOMETASYNC(0x4_0000),
  /**
   * Use writable mmap.
   */
  MDB_WRITEMAP(0x8_0000),
  /**
   * Use asynchronous msync when {@link #MDB_WRITEMAP} is used.
   */
  MDB_MAPASYNC(0x10_0000),
  /**
   * Tie reader locktable slots to {@link Txn} objects instead of to threads.
   */
  MDB_NOTLS(0x20_0000),
  /**
   * Don't do any locking, caller must manage their own locks.
   */
  MDB_NOLOCK(0x40_0000),
  /**
   * Don't do readahead (no effect on Windows).
   */
  MDB_NORDAHEAD(0x80_0000),
  /**
   * Don't initialize malloc'd memory before writing to datafile.
   */
  MDB_NOMEMINIT(0x100_0000);

  private final int mask;

  EnvFlags(final int mask) {
    this.mask = mask;
  }

  @Override
  public int getMask() {
    return mask;
  }

}
