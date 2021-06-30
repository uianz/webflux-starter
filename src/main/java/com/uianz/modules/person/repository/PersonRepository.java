package com.uianz.modules.person.repository;

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;
import com.uianz.modules.person.bean.Person;

public interface PersonRepository extends QuerydslR2dbcRepository<Person, Integer> {
}
