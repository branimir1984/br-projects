package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.UniqueIdentificationNumberExistException;
import bg.codeacademy.cakeShop.model.Address;
import bg.codeacademy.cakeShop.model.Comment;
import bg.codeacademy.cakeShop.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    public final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment addComment(Comment comment) {
        commentRepository.save(comment);
        return comment;
    }
}
