package br.jus.trf2.balcaojus.util;

import com.crivano.swaggerservlet.IPresentableException;
@SuppressWarnings("serial")
public class PresentablePartialLoggedException extends Exception  implements IPresentableException{


	public PresentablePartialLoggedException(String message, Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace){
		super(message,cause,enableSuppression,writableStackTrace);
	}


}
