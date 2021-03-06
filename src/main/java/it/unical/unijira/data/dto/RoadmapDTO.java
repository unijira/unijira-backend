package it.unical.unijira.data.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RoadmapDTO {

    private Long id;
    private Long backlogId;
    private List<RoadmapInsertionDTO> insertions;

}
