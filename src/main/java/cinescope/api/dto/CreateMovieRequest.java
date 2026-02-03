package cinescope.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMovieRequest {
    private String name;
    private String imageUrl;
    private Double price;
    private String description;
    private String location;
    private Boolean published;
    private Integer genreId;
}