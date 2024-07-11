package bg.codeacademy.cakeShop.controller;

import bg.codeacademy.cakeShop.dto.CommentDTO;
import bg.codeacademy.cakeShop.mapper.Mapper;
import bg.codeacademy.cakeShop.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;
    private final Mapper mapper;

    public CommentController(CommentService commentService, Mapper mapper) {
        this.commentService = commentService;
        this.mapper = mapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createComment(@Valid @RequestBody CommentDTO dto) {
        return new ResponseEntity<>("http://localhost:8080/api/v1/legal-entities/comments/?id=" +
                commentService.createComment(dto.uin(), dto.text()
                ).getId(), HttpStatus.CREATED);
    }
}
