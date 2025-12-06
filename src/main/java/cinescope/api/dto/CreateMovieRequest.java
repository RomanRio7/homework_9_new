package cinescope.api.dto;

import lombok.Data;

@Data
public class CreateMovieRequest {
    private String name;
    private String imageUrl;
    private Double price;
    private String description;
    private String location;
    private Boolean published;
    private Integer genreId;
}