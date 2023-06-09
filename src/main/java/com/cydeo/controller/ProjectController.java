package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/v1/project")
@Tag(name = "ProjectController",description = "Project API")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Get projects")
    public ResponseEntity<ResponseWrapper> getProjects() {
        return ResponseEntity.ok(new ResponseWrapper("Projects are successfully retrieved", projectService.listAllProjects(), HttpStatus.OK));
    }

    @GetMapping("/{projectCode}")
    @RolesAllowed("Manager")
    @Operation(summary = "Get project by code")
    public ResponseEntity<ResponseWrapper> getProjectByCode(@PathVariable("projectCode") String code) {
        return ResponseEntity.ok(new ResponseWrapper("Project is retrieved", projectService.getByProjectCode(code), HttpStatus.OK));
    }

    @PostMapping
    @RolesAllowed({"Admin","Manager"})
    @Operation(summary = "Create project")
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO) {
        projectService.save(projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("Project is successfully created", HttpStatus.CREATED));
    }

    @PutMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Update project")
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO) {
        projectService.update(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Project is successfully created",HttpStatus.CREATED));
    }

    @DeleteMapping("/{code}")
    @RolesAllowed("Manager")
    @Operation(summary = "Delete project")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("code") String code) {
        projectService.delete(code);
        return ResponseEntity.ok(new ResponseWrapper("Project is successfully deleted", HttpStatus.OK));
    }

    @GetMapping("/manager/project-status")
    @RolesAllowed("Manager")
    @Operation(summary = "Get project by manager")
    public ResponseEntity<ResponseWrapper> getProjectByManager() {
        return ResponseEntity.ok(new ResponseWrapper("Projects are successfully retrieved", projectService.listAllProjectDetails(), HttpStatus.OK));
    }

    @PutMapping("/manager/complete/{projectCode}")
    @RolesAllowed("Manager")
    @Operation(summary = "Complete project")
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable("projectCode") String code) {
        projectService.complete(code);
        return ResponseEntity.ok(new ResponseWrapper("Project is successfully completed", HttpStatus.OK));
    }
}
