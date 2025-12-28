package com.example.todo.controller.task;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.todo.controller.comment.CommentDTO;
import com.example.todo.controller.comment.CommentForm;
import com.example.todo.service.comment.CommentService;
import com.example.todo.service.task.TaskService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final CommentService commentService;
    private final TaskService taskService;

    @GetMapping
    public String list(TaskSearchForm searchForm, Model model) {
        var taskList = taskService.find(searchForm.toEntity())
                .stream()
                .map(TaskDTO::toDTO)
                .toList();
        model.addAttribute("taskList", taskList);
        model.addAttribute("searchDTO", searchForm.toDTO());
        return "tasks/list";
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable("id") long taskId, CommentForm commentForm, Model model) {
        setupDetailPage(taskId, commentForm, model);
        return "tasks/detail";
    }

    @GetMapping("/creationForm")
    public String showCreationForm(@ModelAttribute TaskForm form, Model model) {
        model.addAttribute("mode", "CREATE");
        return "tasks/form";
    }

    @PostMapping
    public String create(@Validated TaskForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return showCreationForm(form, model);
        }
        taskService.create(form.toEntity());
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/editForm")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        var form = taskService.findById(id)
                .map(TaskForm::fromEntity)
                .orElseThrow(TaskNotFoundException::new);
        model.addAttribute("taskForm", form);
        model.addAttribute("mode", "EDIT");
        return "tasks/form";
    }

    @PutMapping("{id}") // PUT /tasks/{id}
    public String update(
            @PathVariable("id") long id,
            @Validated @ModelAttribute TaskForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "EDIT");
            return "tasks/form";
        }
        var entity = form.toEntity(id);
        taskService.update(entity);
        return "redirect:/tasks/{id}";
    }

    // POST /tasks/1 (hidden: _method: delete)
    // -> DELETE /tasks/1
    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") long id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }

    // PATCH /tasks/1/toggle
    @PatchMapping("{id}/toggle")
    public String toggleStatus(@PathVariable("id") long id) {
        var updated = taskService.toggleStatus(id);
        if (!updated) {
            throw new TaskNotFoundException();
        }
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/comments")
    public String createComment(
            @PathVariable("id") long taskId,
            @Validated CommentForm commentForm,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            setupDetailPage(taskId, commentForm, model);
            return "tasks/detail";
        }
        commentService.create(commentForm.toEntity(taskId));
        return "redirect:/tasks/{id}";
    }

    @DeleteMapping("/{taskId}/comments/{commentId}")
    public String deleteComment(
            @PathVariable("taskId") long taskId,
            @PathVariable("commentId") long commentId
    ) {
        commentService.delete(taskId, commentId);
        return "redirect:/tasks/{taskId}";
    }

    private void setupDetailPage(long taskId, CommentForm commentForm, Model model) {
        var taskDTO = taskService.findById(taskId)
                .map(TaskDTO::toDTO)
                .orElseThrow(TaskNotFoundException::new);
        var commentList = commentService.findByTaskId(taskId)
                .stream()
                .map(CommentDTO::fromEntity)
                .toList();

        model.addAttribute("id", taskId);
        model.addAttribute("task", taskDTO);
        model.addAttribute("commentForm", commentForm);
        model.addAttribute("commentList", commentList);
    }
}
