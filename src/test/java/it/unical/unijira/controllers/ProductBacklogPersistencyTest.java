package it.unical.unijira.controllers;

import it.unical.unijira.UniJiraTest;
import it.unical.unijira.data.dao.UserRepository;
import it.unical.unijira.data.exceptions.NonValidItemTypeException;
import it.unical.unijira.data.models.*;
import it.unical.unijira.services.common.impl.ItemAssignmentServiceImpl;
import it.unical.unijira.services.common.impl.NoteServiceImpl;
import it.unical.unijira.services.common.impl.ProductBacklogItemServiceImpl;
import it.unical.unijira.services.common.impl.UserServiceImpl;
import it.unical.unijira.utils.ProductBacklogItemType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProductBacklogPersistencyTest extends UniJiraTest {


    @Autowired
    private ProductBacklogItemServiceImpl pbiService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private NoteServiceImpl noteService;

    @Autowired
    private ItemAssignmentServiceImpl itemAssignmentService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void initProductBacklogItem() {

        Assertions.assertNotNull(pbiRepository);
        Assertions.assertNotNull(noteRepository);
        Assertions.assertNotNull(itemAssignmentRepository);
        Assertions.assertNotNull(userRepository);

        ProductBacklogItem father = new ProductBacklogItem();
        father.setDescription("this is an useless epic");
        father.setEvaluation(77);
        father.setMeasureUnit("metri");
        father.setSummary("useless epic");
        try {
            father.setType(ProductBacklogItemType.getInstance().EPIC);
            father.setFather(null);
        } catch (NonValidItemTypeException e) {}

        father.setOwner(userRepository.findAll().stream().findFirst().get());
        father.setTags("#backend#");


        father = pbiRepository.saveAndFlush(father);


        ProductBacklogItem son = new ProductBacklogItem();
        son.setDescription("this is an useless item");
        son.setEvaluation(77);
        son.setMeasureUnit("centimetrimetri");
        son.setSummary("useless item");
        try {
            son.setType(ProductBacklogItemType.getInstance().STORY);
            son.setFather(father);
        } catch (NonValidItemTypeException e) {}

        son.setOwner(userRepository.findAll().stream().findFirst().get());
        son.setTags("#backend#");

        son = pbiRepository.saveAndFlush(son);


        List<User> users = userRepository.findAll();


        for(var user : users) {
            ItemAssignment assignment = new ItemAssignment();
            assignment.setAssignee(user);
            assignment.setItem(son);
            itemAssignmentRepository.save(assignment);
        }

    }

    @Test
    public void dummyTest(){

        Iterable<ItemAssignment> assignments = itemAssignmentRepository.findAll();
        Assertions.assertTrue(assignments.iterator().hasNext());
        List<ProductBacklogItem> items = pbiRepository.findAll();
        Assertions.assertTrue(items.size() >= 2);
        for(var item : items) {
            Assertions.assertNotNull(item.getUpdatedAt());
            Assertions.assertNotNull(item.getId());
            Assertions.assertNotNull(item.getCreatedAt());
            Assertions.assertNotNull(item.getSummary());
            Assertions.assertNotNull(item.getDescription());
            Assertions.assertNotNull(item.getEvaluation());
            Assertions.assertNotNull(item.getMeasureUnit());
            Assertions.assertNotNull(item.getTags());
            Assertions.assertTrue(item.getTags().startsWith("#") && item.getTags().endsWith("#"));
            Assertions.assertNotNull(item.getType());
            Assertions.assertTrue(ProductBacklogItemType.getInstance().isCoherentType(item.getType()));
        }

        var firstId = items.get(0).getId();
        var secondId = items.get(1).getId();

        ProductBacklogItem toUpdate = pbiService.findById(secondId).get();
        System.err.println(toUpdate.getTags());
        Assertions.assertNotNull(toUpdate);
        toUpdate.setTags("#UPDATED#");
        pbiRepository.saveAndFlush(toUpdate);

        ProductBacklogItem updated = pbiService.findById(secondId).get();
        Assertions.assertEquals("#UPDATED#", updated.getTags());

        ProductBacklogItem retrieved = pbiService.findById(firstId).get();
        List<ItemAssignment> assignmentList = retrieved.getAssignees();
        Assertions.assertNotNull(retrieved);

        pbiService.delete(retrieved);

        Assertions.assertFalse(pbiService.findById(firstId).isPresent());
        Assertions.assertFalse(pbiService.findById(secondId).isPresent());



    }




}
