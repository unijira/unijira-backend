package it.unical.unijira.data.dto.user;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SprintInsertionDTO {
    private Long id;
    private SprintDTO sprint;
    private ItemDTO item;
}
