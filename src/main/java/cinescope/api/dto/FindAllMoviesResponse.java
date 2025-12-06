package cinescope.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class FindAllMoviesResponse {
    private List<MovieResponse> movies;
    private Integer count;
    private Integer page;
    private Integer pageSize;
    private Integer pageCount;
}