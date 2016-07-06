package com.bfsi.egalite.dao.sqlite;

import java.util.List;

import com.bfsi.egalite.dao.RequestsDao;
import com.bfsi.egalite.entity.Requests;
import com.bfsi.egalite.exceptions.DataAccessException;

public class RequestsDataAccess implements RequestsDao
{
	@Override
	public List<Requests> readRequestHistory(String selected_date)
			throws DataAccessException {
		return null;
	}
	
}
