package com.packtpub.a3ws.ch13.samples.cmis.ws.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.cxf.binding.soap.saaj.SAAJOutInterceptor;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;

public class CmisUtils {

	private static final String SEP = " ";
	
	public static <ResultType> ResultType configureWss4jClient(ResultType servicePort, String username, final String password)
    {
        Map<String, Object> outInterceptorProperties = new HashMap<String, Object>();
        outInterceptorProperties.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN + SEP + WSHandlerConstants.TIMESTAMP);
        outInterceptorProperties.put(WSHandlerConstants.USER, username);
        outInterceptorProperties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);

        outInterceptorProperties.put(WSHandlerConstants.PW_CALLBACK_REF, new CallbackHandler() 
        {
            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
            {
                ((WSPasswordCallback) callbacks[0]).setPassword(password);
            }
        });

        WSS4JOutInterceptor outInterceptor = new WSS4JOutInterceptor(outInterceptorProperties);
        Client client = ClientProxy.getClient(servicePort);
        client.getEndpoint().getOutInterceptors().add(new SAAJOutInterceptor());
        client.getEndpoint().getOutInterceptors().add(outInterceptor);

        return servicePort;
    }
	
}
