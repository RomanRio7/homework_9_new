package cinescope.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MovieResponse {

    private Long id;
    private String name;
    private Double price;
    private String description;
    private String imageUrl;
    private String location;
    private Boolean published;
    private Integer genreId;

    private GenreResponse genre;

    private String createdAt;
    private Double rating;
}