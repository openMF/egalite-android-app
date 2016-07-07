package com.bfsi.egalite.dao;

import java.util.List;

import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.entity.Requests;
import com.bfsi.egalite.exceptions.DataAccessException;


public interface RequestsDao {

	List<Requests> readRequestHistory(String selected_date) throws SQLiteException, DataAccessException;
	
}
