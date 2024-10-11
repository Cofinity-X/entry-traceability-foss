package org.eclipse.tractusx.traceability.common.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Data
@ConfigurationProperties("provisioning.submodel")
public class SubmodelProperties {
    private String baseInternal;
    private String baseExternal;
    private String path;
    private String clientId;
    private String clientSecret;
    private String tokenUrl;
    private String oauthProviderRegistrationId;
}
