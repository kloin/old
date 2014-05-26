package Retrieval;

import javax.servlet.http.HttpServletRequest;

public class LocalityManager extends IDataManager{

	HttpServletRequest request;
	public LocalityManager(HttpServletRequest request) {
		this.request = request;
	}

}
