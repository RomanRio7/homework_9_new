package cinescope.db.model;

import lombok.Data;

@Data
public class MovieDbModel {

    private Long id;
    private String name;
    private Double price;
    private String description;
    private String imageUrl;
    private String location;
    private Boolean published;
    private Integer genreId;
    private String createdAt;
    private Double rating;
}