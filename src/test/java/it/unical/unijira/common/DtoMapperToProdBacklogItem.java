package it.unical.unijira.common;

import it.unical.unijira.UniJiraTest;
import it.unical.unijira.data.dto.user.ItemAssignmentDTO;
import it.unical.unijira.data.dto.user.ProductBacklogItemDTO;
import it.unical.unijira.data.dto.user.UserInfoDTO;
import it.unical.unijira.data.models.ProductBacklogItem;
import it.unical.unijira.data.models.User;
import it.unical.unijira.services.common.impl.UserServiceImpl;
import it.unical.unijira.utils.ProductBacklogItemType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class DtoMapperToProdBacklogItem extends UniJiraTest {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserServiceImpl userService;

    private List<Long> availableIdS = new ArrayList<>();

    @BeforeEach
    void addUser(){
        User userCiccio = new User();
        userCiccio.setUsername("ciccio");
        userCiccio.setPassword(passwordEncoder.encode(PASSWORD));
        userCiccio.setMembers(Collections.emptyList());

        userRepository.saveAndFlush(userCiccio);
        List<User> users = userRepository.findAll();
        for (var user : users){
            availableIdS.add(user.getId());

        }
    }


    @Test
    void DTOToBacklogItemTest() {


        List<User> users = userRepository.findAll();


        var item = new ProductBacklogItemDTO() {{
            setId(1L);
            setSummary("item inutile");
            setDescription("questo item non serve assolutamente a nulla");
            setEvaluation(8);
            setType(ProductBacklogItemType.getInstance().EPIC);
            setMeasureUnit("metri");

        }};





        List<ItemAssignmentDTO> assignments = new ArrayList<>();
        if (availableIdS.size() > 1) {
            ItemAssignmentDTO firstAssignmentDTO = new ItemAssignmentDTO() {{
                this.setId(1L);
                this.setItem(item);
            }};
            UserInfoDTO firstAssignee = new UserInfoDTO() {{
                setId(availableIdS.get(1));
            }};
            firstAssignmentDTO.setAssignee(firstAssignee);
            assignments.add(firstAssignmentDTO);
        }
        if (availableIdS.size() > 0){
            ItemAssignmentDTO secondAssignmentDTO = new ItemAssignmentDTO() {{
                this.setId(2L);
                this.setItem(item);
            }};
            UserInfoDTO secondAssignee = new UserInfoDTO() {{
                setId(availableIdS.get(0));
            }};
            secondAssignmentDTO.setAssignee(secondAssignee);
            assignments.add(secondAssignmentDTO);
        }

        item.setAssignees(assignments);


        ProductBacklogItem itemAssignment = modelMapper.map(item, ProductBacklogItem.class);

        Assertions.assertNotNull(itemAssignment.getId());
        itemAssignment.getType();
        itemAssignment.getDescription();
        itemAssignment.getEvaluation();
        itemAssignment.getOwner();


    }
}