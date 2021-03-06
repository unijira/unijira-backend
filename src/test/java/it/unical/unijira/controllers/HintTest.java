package it.unical.unijira.controllers;

import it.unical.unijira.UniJiraTest;
import it.unical.unijira.data.dao.UserRepository;
import it.unical.unijira.data.dao.projects.ProjectRepository;
import it.unical.unijira.data.models.*;
import it.unical.unijira.data.models.items.*;
import it.unical.unijira.data.models.projects.Project;
import it.unical.unijira.services.common.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
public class HintTest extends UniJiraTest {

    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    @Autowired
    ProductBacklogService productBacklogService;

    @Autowired
    SprintService sprintService;

    @Autowired
    RoadmapService roadmapService;

    @Autowired
    SprintInsertionService sprintInsertionService;

    @Autowired
    ProductBacklogInsertionService productBacklogInsertionService;

    private final int defaultPage = 0;
    private final int defaultSize = 10000;

    private Long projectId;
    private Long backlogId;
    private Long sprintId;
    private Long userId;

    @Autowired
    private ProjectRepository projectRepository;

    @BeforeEach
    void fillData() {

        userService.save(User.builder()
                .username("p.theninja@gmail.com")
                .password("Unijira2.0!")
                .status(User.Status.ACTIVE)
                .build());
        userService.save(User.builder()
                .username("fakemail1@unijira.com")
                .password("Unijira2.0!")
                .status(User.Status.ACTIVE)
                .build());
        userService.save(User.builder()
                .username("fakemail2@unijira.com")
                .password("Unijira2.0!")
                .status(User.Status.ACTIVE)
                .build());
        userService.save(User.builder()
                .username("fakemail3@unijira.com")
                .password("Unijira2.0!")
                .status(User.Status.ACTIVE)
                .build());
        userService.save(User.builder()
                .username("fakemail4@unijira.com")
                .password("Unijira2.0!")
                .status(User.Status.ACTIVE)
                .build());
        userService.save(User.builder()
                .username("fakemail5@unijira.com")
                .password("Unijira2.0!")
                .status(User.Status.ACTIVE)
                .build());
        log.info("Initializing Project...");
            User p = userService.findByUsername(UniJiraTest.USERNAME).orElseThrow();
            Project project = projectService
                    .create(Project.builder()
                            .name("Project 1")
                            .key("Project 1 key")
                            .owner(p).build()).orElse(null);
            List<ProductBacklog> backlogs = productBacklogService.findAllByProject(project,defaultPage,defaultSize);
            ProductBacklog backlog = backlogs.get(0);

            if (project!= null) {
                this.projectId = project.getId();
            }
            this.userId = p.getId();
            this.backlogId = backlog.getId();
            List<Roadmap> roadmaps = roadmapService.findByBacklog(backlog,defaultPage,defaultSize);
            Roadmap roadmap = roadmaps.get(0);
            Sprint closedOneMonthAgo = Sprint.builder()
                    .backlog(backlog)
                    .startingDate(LocalDate.now().minusMonths(2))
                    .endingDate(LocalDate.now().minusMonths(1))
                    .status(SprintStatus.INACTIVE)
                    .build();
            Sprint closedToday = Sprint.builder()
                    .backlog(backlog)
                    .startingDate(LocalDate.now().minusMonths(1))
                    .endingDate(LocalDate.now())
                    .status(SprintStatus.INACTIVE)
                    .build();

            Sprint open = Sprint.builder()
                    .backlog(backlog)
                    .startingDate(LocalDate.now())
                    .endingDate(LocalDate.now().plusMonths(1))
                    .status(SprintStatus.ACTIVE)
                    .build();
            closedOneMonthAgo = sprintService.save(closedOneMonthAgo).orElse(closedOneMonthAgo);
            closedToday = sprintService.save(closedToday).orElse(closedToday);
            open = sprintService.save(open).orElse(open);
            this.sprintId = open.getId();
            List<Sprint> sprints = new ArrayList<>();
            sprints.add(closedOneMonthAgo);
            sprints.add(closedToday);
            sprints.add(open);
            Item epic = Item.builder()
                    .description("FIRST EPIC")
                    .summary("1ST EPIC")
                    .measureUnit(MeasureUnit.STORY_POINTS)
                    .status(ItemStatus.DONE)
                    .type(ItemType.EPIC)
                    .owner(p)
                    .project(project)
                    .build();
            itemService.save(epic);
            productBacklogInsertionService.save(ProductBacklogInsertion.builder()
                    .backlog(backlog)
                    .item(epic)
                    .priority(0)
                    .build());
            SprintInsertion s = new SprintInsertion();
            s.setItem(epic);
            s.setSprint(closedOneMonthAgo);
            sprintInsertionService.save(s);
            Item story = Item.builder()
                    .description("FIRST STORY")
                    .summary("1ST STORY")
                    .measureUnit(MeasureUnit.STORY_POINTS)
                    .type(ItemType.STORY)
                    .status(ItemStatus.DONE)
                    .father(epic)
                    .owner(p)
                    .project(project)
                    .build();
            itemService.save(story);
            productBacklogInsertionService.save(ProductBacklogInsertion.builder()
                    .backlog(backlog)
                    .item(story)
                    .priority(0)
                    .build());
            s = new SprintInsertion();
            s.setItem(story);
            s.setSprint(closedOneMonthAgo);
            sprintInsertionService.save(s);
            Random r = new Random();
            List<User> users = userService.findAll();
            for (Sprint sprintz : sprints) {
                int factor = 1;
                int itemsOfThisStory = r.nextInt(25);
                for (int i = 0; i < itemsOfThisStory; i++) {
                    List<ItemAssignment> list = new ArrayList<>();
                    ItemAssignment ia = ItemAssignment.builder()
                            .assignee(users.get(r.nextInt(users.size()))).build();
                    list.add(ia);
                    Item task;
                    if (sprintz.equals(open)) {
                        task = Item.builder()
                                .description(i * factor + " TASK")
                                .summary(i * factor + " task")
                                .measureUnit(MeasureUnit.STORY_POINTS)
                                .evaluation(r.nextInt(8))
                                .type(ItemType.TASK)
                                .status(ItemStatus.OPEN)
                                .father(story)
                                .assignees(list)
                                .owner(p)
                                .project(project)
                                .build();
                        itemService.save(task);
                    } else {
                        task = Item.builder()
                                .description(i * factor + " TASK")
                                .summary(i * factor + " task")
                                .measureUnit(MeasureUnit.STORY_POINTS)
                                .evaluation(r.nextInt(8))
                                .type(ItemType.TASK)
                                .status(ItemStatus.DONE)
                                .father(story)
                                .assignees(list)
                                .owner(p)
                                .project(project)
                                .build();
                        itemService.save(task);

                    }
                    productBacklogInsertionService.save(ProductBacklogInsertion.builder()
                            .backlog(backlog)
                            .item(task)
                            .priority(i)
                            .build());


                    s = new SprintInsertion();
                    s.setItem(task);
                    s.setSprint(sprintz);

                    sprintInsertionService.save(s);
                }
                ++factor;
                if(!sprintz.equals(open)) {
                    sprintService.update(sprintz.getId() , Sprint.builder()
                            .backlog(sprintz.getBacklog())
                            .endingDate(sprintz.getEndingDate().minusDays(1))
                            .startingDate(sprintz.getStartingDate())
                            .status(SprintStatus.INACTIVE)
                            .build());
                }
            }
        log.info("Project initialized");

    }


    @Test
    void receiveHint() throws Exception{

        ResultActions call = mockMvc.perform(get("/projects/"+projectId+
                "/backlogs/"+backlogId+"/" +
                "sprints/"+sprintId+"/user/"+userId+"/hint")
                .header("Authorization", "Bearer " +
                        this.performLogin(UniJiraTest.USERNAME, UniJiraTest.PASSWORD))) ;


        MvcResult returnValue = call.andReturn();

        call.andExpect(status().isOk());

        System.out.println(returnValue.getResponse().getContentAsString());

        Sprint s = sprintService.findById(sprintId).orElse(null);
        if (s != null) {
            Long sprintId = s.getId();
            s.setStatus(SprintStatus.INACTIVE);
            this.sprintService.update(sprintId,s);
        }

    }

}
