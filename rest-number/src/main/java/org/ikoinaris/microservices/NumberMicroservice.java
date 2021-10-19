package org.ikoinaris.microservices;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
@OpenAPIDefinition(
        info = @Info(
                title = "Number Microservice",
                description = "This microservice generates random book numbers",
                version = "1.0",
                contact = @Contact(name = "@ikoinaris", url = "https://twitter.com/ikoinaris")
        )
)
public class NumberMicroservice extends Application {
}
