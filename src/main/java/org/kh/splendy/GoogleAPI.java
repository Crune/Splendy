package org.kh.splendy;

import org.springframework.social.connect.Connection;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;

public class GoogleAPI {

    private GoogleConnectionFactory GplusConnectionFactory;
    private Google google;
    private static final String REDIRECT_URI = "http://localhost/login/facebook";

    public Google getGoogle() {
        return google;
    }

    public GoogleAPI(GoogleConnectionFactory GplusConnectionFactory) {
        this.GplusConnectionFactory = GplusConnectionFactory;
    }

    public String getRedirectURI() {

        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri(REDIRECT_URI);
        params.setScope("profile");
        params.setScope("openid");
        params.setScope("email");
        String authorizeUrl = GplusConnectionFactory.getOAuthOperations().buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, params);
        return authorizeUrl;

    }

    public Google establishFacebookConnection(String accessToken) {
        AccessGrant accessGrant = GplusConnectionFactory.getOAuthOperations().exchangeForAccess(accessToken,REDIRECT_URI, null);
        Connection<Google> connection = GplusConnectionFactory.createConnection(accessGrant);
        google =  connection.getApi();
        return google;
    }

    public boolean isAuthorized() {
        if(google != null){
            return google.isAuthorized();
        }
        return false;
    }
}