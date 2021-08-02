package com.uianz.modules.person.repository;

import com.uianz.common.r2dbc.repository.PageQuerydslR2dbcRepository;
import com.uianz.modules.person.bean.Person;

public interface PersonRepository extends PageQuerydslR2dbcRepository<Person, Integer> {
}
