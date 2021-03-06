package com.clouway.persistent.adapter.jdbc;

import com.clouway.core.HistoryRepository;
import com.clouway.core.Transaction;
import com.clouway.persistent.datastore.DataStore;
import com.google.inject.Inject;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class PersistentHistoryRepository implements HistoryRepository {
  private final DataStore dataStore;

  @Inject
  public PersistentHistoryRepository(DataStore dataStore) {
    this.dataStore = dataStore;
  }

  @Override
  public List<Transaction> getBalanceHistory(String username, int offset, int limit) {
    String query = "select * from transaction_history where account_Name='" + username + "' order by transaction_Date limit " + offset + "," + limit + ";";
    return dataStore.fetchRows(query, set -> {
      try {
        return new Transaction(set.getTimestamp(1), set.getString(2), set.getString(3), set.getDouble(4));
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return null;
    });
  }
}

