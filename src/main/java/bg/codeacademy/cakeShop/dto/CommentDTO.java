package bg.codeacademy.cakeShop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record CommentDTO(
        @NotEmpty
        @NotNull
        @NotBlank
        String comment,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDateTime date,
        @NotEmpty
        @NotNull
        @NotBlank
        int assessed
) {
}
