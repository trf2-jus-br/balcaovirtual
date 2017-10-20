package br.jus.trf2.sistemadocumental;

import com.crivano.swaggerservlet.ISwaggerMethod;
import com.crivano.swaggerservlet.ISwaggerModel;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;

public interface ISistemaDocumental {
	public class PDF implements ISwaggerModel {
	}

	public class Pages implements ISwaggerModel {
	}

	public class Size implements ISwaggerModel {
	}

	public class Error implements ISwaggerModel {
		public String error;
	}

	public class HtmlPdfPostRequest implements ISwaggerRequest {
		public String html;
		public String conv;
	}

	public class HtmlPdfPostResponse implements ISwaggerResponse {
		public byte[] pdf;
		public String pages;
		public String size;
	}

	public interface IHtmlPdfPost extends ISwaggerMethod {
		public void run(HtmlPdfPostRequest req, HtmlPdfPostResponse resp) throws Exception;
	}

}
