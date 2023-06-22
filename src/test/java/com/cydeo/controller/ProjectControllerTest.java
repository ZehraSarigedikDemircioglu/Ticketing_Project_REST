package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Gender;
import com.cydeo.enums.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mvc;
    static String token;
    static UserDTO manager;
    static ProjectDTO project;
    @BeforeAll
    static void setUp() {

        token = "Bearer " + "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJNQk90MlJoZUhaQi1TaGhnNGxPYVZ4MzdsWUhYU3NIYVlaVDdvLUxQN3NRIn0.eyJleHAiOjE2ODc0NTg2NjksImlhdCI6MTY4NzQ0MDY2OSwianRpIjoiYmViY2ZlNmUtYjFhZC00NTRhLTg3NzUtMDQzMGViOTMwZWIzIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL2N5ZGVvLWRldiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI0YWZmMjM0Ny05ZDc2LTQ3OTUtYWZmZi00YjY3OTU4NTE0OTMiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ0aWNrZXRpbmctYXBwIiwic2Vzc2lvbl9zdGF0ZSI6ImNhNzk1NDI4LTFjYTUtNDlkYS04M2QwLWVhZTRkMWQ4Yjc4NSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo4MDgxIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtY3lkZW8tZGV2IiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJ0aWNrZXRpbmctYXBwIjp7InJvbGVzIjpbIk1hbmFnZXIiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIGVtYWlsIHByb2ZpbGUiLCJzaWQiOiJjYTc5NTQyOC0xY2E1LTQ5ZGEtODNkMC1lYWU0ZDFkOGI3ODUiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicHJlZmVycmVkX3VzZXJuYW1lIjoib3p6eSJ9.B53zj3OBMZwQrGaoi2-S4YjG87zw1oTTCE4HDuw0k0zfQV-xm3OTGMiELNlvsYJGIVKFc-l5l6GDUhNCeuudfwahbNZJxf0XC1X8Ld3bDf9yAPs-EyXexB7IeaEZUdqh7kUB3ttbvVZCfMm-eh7q2ca3KZ7aSpidVeQkOu_l9cH5s0qiHZLf1SLQzy88P5QViu4dj8AZIEHmakwj_Sfyy3ylYFD0KCNX3HlUawshVG_mY_olgQ1dTZGsOP0kUbIcTWg7MYg53GiWMZCAwudt2cN-Q0Rx86jVZM5qGdK7zRS5zwiDDR87xJT9L9BvbdmoRaLrzGGkY4GpqqgKkIZf0A";

        manager = new UserDTO(2L,
                "",
                "",
                "ozzy",
                "abc1",
                "",
                true,
                "",
                new RoleDTO(2L, "Manager"),
                Gender.MALE);

        project = new ProjectDTO(
                "API Project",
                "PR001",
                manager,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                "Some details",
                Status.OPEN
        );
    }
    @Test
    void givenNoToken_getProjects() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/project"))
                .andExpect(status().is4xxClientError());
    }
    @Test
    void givenToken_getProjects() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/project")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].projectCode").exists())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").exists())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").isNotEmpty())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").isString())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").value("ozzy"));
    }

}