
package br.jus.cnj.servico_intercomunicacao_2_2;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "servico-intercomunicacao-2.2.2", targetNamespace = "http://www.cnj.jus.br/servico-intercomunicacao-2.2.2/", wsdlLocation = "http://vmwebmnih2012/servico-intercomunicacao-2.2.2-mtom/jfrj/?wsdl")
public class ServicoIntercomunicacao222_Service
    extends Service
{

    private final static URL SERVICOINTERCOMUNICACAO222_WSDL_LOCATION;
    private final static WebServiceException SERVICOINTERCOMUNICACAO222_EXCEPTION;
    private final static QName SERVICOINTERCOMUNICACAO222_QNAME = new QName("http://www.cnj.jus.br/servico-intercomunicacao-2.2.2/", "servico-intercomunicacao-2.2.2");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://vmwebmnih2012/servico-intercomunicacao-2.2.2-mtom/jfrj/?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SERVICOINTERCOMUNICACAO222_WSDL_LOCATION = url;
        SERVICOINTERCOMUNICACAO222_EXCEPTION = e;
    }

    public ServicoIntercomunicacao222_Service() {
        super(__getWsdlLocation(), SERVICOINTERCOMUNICACAO222_QNAME);
    }

    public ServicoIntercomunicacao222_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), SERVICOINTERCOMUNICACAO222_QNAME, features);
    }

    public ServicoIntercomunicacao222_Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICOINTERCOMUNICACAO222_QNAME);
    }

    public ServicoIntercomunicacao222_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SERVICOINTERCOMUNICACAO222_QNAME, features);
    }

    public ServicoIntercomunicacao222_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ServicoIntercomunicacao222_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ServicoIntercomunicacao222
     */
    @WebEndpoint(name = "servico-intercomunicacao-2.2.2SOAP")
    public ServicoIntercomunicacao222 getServicoIntercomunicacao222SOAP() {
        return super.getPort(new QName("http://www.cnj.jus.br/servico-intercomunicacao-2.2.2/", "servico-intercomunicacao-2.2.2SOAP"), ServicoIntercomunicacao222.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ServicoIntercomunicacao222
     */
    @WebEndpoint(name = "servico-intercomunicacao-2.2.2SOAP")
    public ServicoIntercomunicacao222 getServicoIntercomunicacao222SOAP(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.cnj.jus.br/servico-intercomunicacao-2.2.2/", "servico-intercomunicacao-2.2.2SOAP"), ServicoIntercomunicacao222.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SERVICOINTERCOMUNICACAO222_EXCEPTION!= null) {
            throw SERVICOINTERCOMUNICACAO222_EXCEPTION;
        }
        return SERVICOINTERCOMUNICACAO222_WSDL_LOCATION;
    }

}
