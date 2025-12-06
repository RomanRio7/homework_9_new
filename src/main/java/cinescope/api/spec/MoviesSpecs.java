package cinescope.api.spec;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class MoviesSpecs {

    private static final String BASE_URI = "https://api.cinescope.t-qa.ru";

    public static RequestSpecification authRequestSpec(String token) {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + token)
                .log(LogDetail.ALL)
                .build();
    }
}