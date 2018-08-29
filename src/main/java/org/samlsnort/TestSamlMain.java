package org.samlsnort;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.AuthnRequestImpl;
import org.samlsnort.util.SamlTool;
import org.samlsnort.vo.SamlResponseData;
import org.samlsnort.vo.TestCase;
import org.opensaml.saml2.core.Response;

import javax.xml.crypto.dsig.XMLObject;

public class TestSamlMain {

    public static void main(String[] args) {
       try {
           TestCase testCase = new TestCase("Test");
           testCase.setSamlResponseData(new SamlResponseData());
           String resp = SamlTool.generateResponse(new MyAuthRequest("test", "test", "test"), testCase);
           Response objReponse = (Response) SamlTool.parseSamlObject(resp);
           Assertion assertion = objReponse.getAssertions().get(0);
           assertion.setIssuer(SamlTool.createIssuer("WadeOrg"));
           assertion.setSignature(SamlTool.createSignature());
           String strAssertion = SamlTool.writeXML(assertion);
           System.out.println(strAssertion);
       }
       catch(Exception e)
       {
            System.out.println("bad: " + e.getMessage());
       }
    }
}
