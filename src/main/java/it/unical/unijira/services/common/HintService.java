package it.unical.unijira.services.common;

import it.unical.unijira.data.models.Sprint;
import it.unical.unijira.data.models.User;

import java.util.List;

public interface HintService {


    List<Long> sendHint(Sprint sprintObj, User userObj, String type);
}
