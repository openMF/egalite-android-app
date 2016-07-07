package com.bfsi.egalite.dao;

import java.util.List;

import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.entity.HistoryDetails;
import com.bfsi.egalite.exceptions.DataAccessException;

public interface HistoryDao {
	List<HistoryDetails>  readHistoryData()throws SQLiteException, DataAccessException;
	List<HistoryDetails>  readHistoryEnrollment() throws SQLiteException ,DataAccessException;

}
