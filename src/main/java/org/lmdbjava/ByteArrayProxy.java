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

import jnr.ffi.Pointer;
import jnr.ffi.provider.MemoryManager;
import static org.lmdbjava.Library.RUNTIME;

/**
 * Byte array proxy.
 *
 * {@link Env#create(org.lmdbjava.BufferProxy)}.
 */
public class ByteArrayProxy extends BufferProxy<byte[]> {

  /**
   * The byte array proxy. Guaranteed to never be null.
   */
  public static final BufferProxy<byte[]> PROXY_BA = new ByteArrayProxy();

  private static final MemoryManager MEM_MGR = RUNTIME.getMemoryManager();

  @Override
  protected final byte[] allocate() {
    return new byte[0];
  }

  @Override
  protected final void deallocate(final byte[] buff) {
    // byte arrays cannot be allocated
  }

  @Override
  protected final void in(final byte[] buffer, final Pointer ptr,
                          final long ptrAddr) {
    final Pointer pointer = MEM_MGR.allocateDirect(buffer.length);
    pointer.put(0, buffer, 0, buffer.length);
    ptr.putLong(STRUCT_FIELD_OFFSET_SIZE, buffer.length);
    ptr.putLong(STRUCT_FIELD_OFFSET_DATA, pointer.address());
  }

  @Override
  protected final void in(final byte[] buffer, final int size, final Pointer ptr,
                          final long ptrAddr) {
    // cannot reserve for byte arrays
  }

  @Override
  protected final byte[] out(final byte[] buffer, final Pointer ptr,
                             final long ptrAddr) {
    final long addr = ptr.getLong(STRUCT_FIELD_OFFSET_DATA);
    final int size = (int) ptr.getLong(STRUCT_FIELD_OFFSET_SIZE);
    final Pointer pointer = MEM_MGR.newPointer(addr, size);
    final byte[] bytes = new byte[size];
    pointer.get(0, bytes, 0, size);
    return bytes;
  }
}
