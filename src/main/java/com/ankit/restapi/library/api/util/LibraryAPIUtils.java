package com.ankit.restapi.library.api.util;

public class LibraryAPIUtils {

	public static boolean doesStrValExist(String str) {
		if(str != null && str.trim().length() > 0) 
			return true;
		else
		return false;
	}

}
