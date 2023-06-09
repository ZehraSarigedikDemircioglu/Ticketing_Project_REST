package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/v1/task")
@Tag(name = "TaskController",description = "Task API")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Get tasks")
    public ResponseEntity<ResponseWrapper> getTasks() {
        return ResponseEntity.ok(new ResponseWrapper("Tasks are successfully retrieved", taskService.listAllTasks(), HttpStatus.OK));
    }

    @GetMapping("/{id}")
    @RolesAllowed("Manager")
    @Operation(summary = "Get task by id")
    public ResponseEntity<ResponseWrapper> getTaskById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ResponseWrapper("Task is retrieved", taskService.findById(id), HttpStatus.OK));
    }

    @PostMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Create task")
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO taskDTO) {
        taskService.save(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Task is successfully created",HttpStatus.CREATED));
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("Manager")
    @Operation(summary = "Delete task")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("id") Long id) {
        taskService.delete(id);
        return ResponseEntity.ok(new ResponseWrapper("Task is successfully deleted", HttpStatus.OK));
    }

    @PutMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Update task")
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO taskDTO) {
        taskService.update(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Task is successfully updated", HttpStatus.OK));
    }

    @GetMapping("/employee/pending-tasks")
    @RolesAllowed("Employee")
    @Operation(summary = "Get pending tasks")
    public ResponseEntity<ResponseWrapper> employeePendingTasks() {
        return ResponseEntity.ok(new ResponseWrapper("Tasks are successfully retrieved", taskService.listAllTasksByStatusIsNot(Status.COMPLETE), HttpStatus.OK));
    }

    @PutMapping("/employee/update")
    @RolesAllowed("Employee")
    @Operation(summary = "Update employee tasks")
    public ResponseEntity<ResponseWrapper> employeeUpdateTasks(@RequestBody TaskDTO taskDTO) {
        taskService.update(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Task is successfully updated", HttpStatus.OK));
    }

    @GetMapping("/employee/archive")
    @RolesAllowed("Employee")
    @Operation(summary = "Get archived tasks")
    public ResponseEntity<ResponseWrapper> employeeArchivedTasks() {
        return ResponseEntity.ok(new ResponseWrapper("Task is successfully completed", taskService.listAllTasksByStatus(Status.COMPLETE), HttpStatus.OK));
    }

}
