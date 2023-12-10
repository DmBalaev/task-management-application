package com.dm.taskapp.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "Comment management")
@RequestMapping("/api/v1/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/add/{idTask}")
    @Operation(
            summary = "Add Comment",
            description = "Endpoint to add a comment to a task",
            parameters = {
                    @Parameter(name = "idTask", description = "ID of the task to add the comment to", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body for adding a comment",
                    content = @Content(schema = @Schema(implementation = Comment.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Comment added successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Comment.class))),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<Comment> addComment(
            @PathVariable Long idTask,
            @RequestParam String text,
            @AuthenticationPrincipal UserDetails userDetails) {
        Comment comment = commentService.add(idTask, text, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @GetMapping("/task/{taskId}")
    @Operation(
            summary = "Get Comments by Task",
            description = "Endpoint to retrieve comments for a specific task",
            parameters = {
                    @Parameter(name = "taskId", description = "ID of the task to get comments for", required = true),
                    @Parameter(name = "page", description = "Optional. Page number (zero-based) to retrieve."),
                    @Parameter(name = "size", description = "Optional. Number of items per page.")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comments retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Comment.class))),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<Page<Comment>> getCommentsByTask(
            @PathVariable Long taskId,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size
    ){
        Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(10));
        Page<Comment> comments = commentService.getByTask(taskId, pageable);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/author/{accountId}")
    @Operation(
            summary = "Get Comments by Author",
            description = "Endpoint to retrieve comments for a specific author",
            parameters = {
                    @Parameter(name = "accountId", description = "ID of the author to get comments for", required = true),
                    @Parameter(name = "page", description = "Optional. Page number (zero-based) to retrieve."),
                    @Parameter(name = "size", description = "Optional. Number of items per page.")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comments retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Comment.class))),
                    @ApiResponse(responseCode = "404", description = "Author not found")
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<Page<Comment>> getCommentsByAuthor(
            @PathVariable Long accountId,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size
    ){
        Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(10));
        Page<Comment> comments = commentService.getByAuthor(accountId, pageable);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{commentId}")
    @Operation(
            tags = "Comment management",
            summary = "Delete Comment",
            description = "Endpoint to delete a comment",
            parameters = {
                    @Parameter(name = "commentId", description = "ID of the comment to delete", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Comment not found")
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        commentService.delete(commentId, userDetails);
        return ResponseEntity.noContent().build();
    }
}