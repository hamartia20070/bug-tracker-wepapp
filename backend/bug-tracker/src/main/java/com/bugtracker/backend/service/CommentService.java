package com.bugtracker.backend.service;

import com.bugtracker.backend.entity.Comment;
import com.bugtracker.backend.entity.Ticket;
import com.bugtracker.backend.entity.User;
import com.bugtracker.backend.repository.CommentRepository;
import com.bugtracker.backend.util.GlobalUtil;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;


    public Comment getCommentById(@NonNull int commentId){
        return commentRepository.findById(commentId).orElse(null);
    }

    public List<Comment> getCommentsByTicket(@NonNull int ticketId,int pageNo){
        Pageable page = PageRequest.of(pageNo, GlobalUtil.itemsPerPage);
        return commentRepository.getCommentsByTicket(ticketId,page);
    }

    public Comment editComment(@NonNull Comment newComment){
        Comment existingComment= commentRepository.findById(newComment.getId()).orElse(null);
        existingComment.setMessage(newComment.getMessage());
        return commentRepository.save(existingComment);
    }

    public void addAllComment(List<Comment> comments){
        commentRepository.saveAll(comments);
    }

    public void deleteComment(@NonNull Comment comment){
        commentRepository.delete(comment);
    }

    public void deleteCommentById(@NonNull int commentId){
        Comment existingComment= commentRepository.findById(commentId).orElse(null);
        commentRepository.delete(existingComment);
    }

    ///something
    public Optional<Comment> addComment(Comment comment,@NonNull int ticketId,@NonNull int userId){
        Comment newComment = new Comment(ticketService.getTicketById(ticketId), comment.getMessage(), userService.getUserById(userId).orElse(null));
        return Optional.of(commentRepository.save(newComment));//save comment
    }

    public Optional<Comment> addCommentWithTicket(Comment comment){
        //addTicketToComment(ticketId,comment);
        addCommentToTicket(comment);
        ticketService.saveTicket(comment.getTicket());//save ticket with new comment added
        return Optional.of(commentRepository.save(comment));//save comment
    }

    public void addUserToComment(Comment comment,@NonNull int userId){
        User userToAdd= userService.getUserById(userId).get();
        comment.setUser(userToAdd);
    }


    public  void addTicketToComment(@NonNull int ticketId,@NonNull Comment comment){
        //check if ticket exists
        Ticket toAdd= ticketService.getTicketById(ticketId);
        comment.setTicket(toAdd);
    }

    //because we have bi direction many to one mapping between ticket and comment, we need to add the comment to the ticket and vice versa
    public void addCommentToTicket(@NonNull Comment comment){

        Ticket ticket = comment.getTicket();//get ticket of comment

        List<Comment> ticketComments = ticket.getComments();//get the comments of the ticket

        ticketComments.add(comment);//add the new comment to the ticket

        ticket.setComments(ticketComments);//pass the new updated list back with our comment added
    }

}
