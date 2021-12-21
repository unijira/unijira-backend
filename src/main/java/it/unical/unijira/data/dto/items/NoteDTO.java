package it.unical.unijira.data.dto.items;

import it.unical.unijira.data.dto.user.UserInfoDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NoteDTO {

    private Long id;
    private LocalDateTime timestamp;
    private String message;
    private NoteDTO replyTo;
    private ItemDTO refersTo;
    private UserInfoDTO author;
}