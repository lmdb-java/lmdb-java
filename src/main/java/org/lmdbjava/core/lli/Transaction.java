package org.lmdbjava.core.lli;

import java.util.Set;

import static java.util.Objects.requireNonNull;
import static jnr.ffi.Memory.allocateDirect;
import static jnr.ffi.NativeType.ADDRESS;
import jnr.ffi.Pointer;
import static org.lmdbjava.core.lli.Library.lib;
import static org.lmdbjava.core.lli.Library.runtime;
import static org.lmdbjava.core.lli.ReturnCodes.checkRc;
import static org.lmdbjava.core.lli.TransactionFlags.MDB_RDONLY;
import static org.lmdbjava.core.lli.Utils.mask;

/**
 * LMDB transaction.
 */
public final class Transaction {

  private boolean committed;
  private final Env env;
  private final boolean readOnly;
  final Pointer ptr;

  Transaction(final Env env, final Transaction parent,
              final Set<TransactionFlags> flags) throws NotOpenException,
                                                        LmdbNativeException {
    requireNonNull(env);
    requireNonNull(flags);
    if (!env.isOpen()) {
      throw new NotOpenException(Transaction.class.getSimpleName());
    }
    this.env = env;
    this.readOnly = flags.contains(MDB_RDONLY);
    final int flagsMask = mask(flags);
    final Pointer txnPtr = allocateDirect(runtime, ADDRESS);
    final Pointer txnParentPtr = parent == null ? null : parent.ptr;
    checkRc(lib.mdb_txn_begin(env.ptr, txnParentPtr, flagsMask, txnPtr));
    ptr = txnPtr.getPointer(0);
  }

  /**
   * Commits this transaction.
   *
   * @throws AlreadyCommittedException if already committed
   * @throws LmdbNativeException       if a native C error occurred
   */
  public void commit() throws AlreadyCommittedException, LmdbNativeException {
    if (committed) {
      throw new AlreadyCommittedException();
    }
    checkRc(lib.mdb_txn_commit(ptr));
    this.committed = true;
  }

  /**
   * Opens a new database
   *
   * @param name  the name of the database (required)
   * @param flags applicable flags (required)
   * @return the database (never null)
   * @throws AlreadyCommittedException if already committed
   * @throws LmdbNativeException if a native C error occurred
   */
  public Database databaseOpen(final String name, final Set<DatabaseFlags> flags)
      throws AlreadyCommittedException, LmdbNativeException {
    return new Database(this, name, flags);
  }

  /**
   * Whether this transaction has been committed.
   *
   * @return true if committed
   */
  public boolean isCommitted() {
    return committed;
  }

  /**
   * Whether this transaction is read-only.
   *
   * @return if read-only
   */
  public boolean isReadOnly() {
    return readOnly;
  }

}
