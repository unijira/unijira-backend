package it.unical.unijira;


import it.unical.unijira.data.dao.NotifyRepository;
import it.unical.unijira.data.dao.UserProjectRepository;
import it.unical.unijira.data.dao.UserRepository;
import it.unical.unijira.data.dao.items.ItemAssignmentRepository;
import it.unical.unijira.data.dao.items.ItemRepository;
import it.unical.unijira.data.dao.items.NoteRepository;
import it.unical.unijira.data.dao.projects.DocumentRepository;
import it.unical.unijira.data.dao.projects.ProjectRepository;
import it.unical.unijira.data.models.Notify;
import it.unical.unijira.data.models.User;
import it.unical.unijira.data.models.projects.Document;
import it.unical.unijira.data.models.projects.Membership;
import it.unical.unijira.data.models.projects.MembershipKey;
import it.unical.unijira.data.models.projects.Project;
import it.unical.unijira.utils.Config;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@Getter
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class UniJiraTest {

    protected final static String USERNAME = "unijira20@gmail.com";
    protected final static String PASSWORD = "Unijira.20";

    @Autowired
    protected Config config;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected NotifyRepository notifyRepository;

    @Autowired
    protected ProjectRepository projectRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected UserProjectRepository userProjectRepository;

    @Autowired
    protected ItemRepository pbiRepository;

    @Autowired
    protected ItemAssignmentRepository itemAssignmentRepository;

    @Autowired
    protected NoteRepository noteRepository;

    @Autowired
    protected DocumentRepository documentRepository;

    @Autowired
    protected MockMvc mockMvc;


    @BeforeEach
    public void init() {

        Assertions.assertNotNull(userRepository);
        Assertions.assertNotNull(notifyRepository);

        if(userRepository.findByUsername(USERNAME).isEmpty()) {

            User user = User.builder()
                    .username(USERNAME)
                    .password(passwordEncoder.encode(PASSWORD))
                    .status(User.Status.ACTIVE)
                    .ownedProjects(Collections.emptyList())
                    .memberships(Collections.emptyList())
                    .firstName("John")
                    .lastName("Doe")
                    .build();

            userRepository.saveAndFlush(user);


            Notify notify = Notify.builder()
                    .user(user)
                    .title("Test")
                    .message("Test")
                    .build();

            notifyRepository.saveAndFlush(notify);


            Project project = Project.builder()
                    .owner(user)
                    .name("Test")
                    .key("TST")
                    .memberships(Collections.emptyList())
                    .build();

            projectRepository.saveAndFlush(project);


            Membership membership = Membership.builder()
                    .status(Membership.Status.ENABLED)
                    .role(Membership.Role.SCRUM_MASTER)
                    .key(new MembershipKey(user, project))
                    .permissions(new HashSet<>(Arrays.asList(Membership.Permission.ADMIN, Membership.Permission.DETAILS,
                            Membership.Permission.INVITATIONS, Membership.Permission.ROLES)))
                    .build();

            userProjectRepository.saveAndFlush(membership);

        }

    }


    public String performLogin(String username, String password) throws Exception {

        return mockMvc.perform(post("/auth/authenticate")
                .with(csrf())
                .contentType("application/json")
                .content("""
                        {
                            "username": "${USERNAME}",
                            "password": "${PASSWORD}"
                        }
                        """.replace("${USERNAME}", username).replace("${PASSWORD}", password))
        ).andReturn().getResponse().getContentAsString();

    }

}
