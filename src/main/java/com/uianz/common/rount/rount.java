//package com.uianz.common.rount;
//
//import com.uianz.modules.person.bean.Person;
//import com.uianz.modules.person.repository.PersonRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Mono;
//
//import static org.springframework.web.reactive.function.server.RequestPredicates.*;
//import static org.springframework.web.reactive.function.server.RouterFunctions.route;
//import static org.springframework.web.reactive.function.server.ServerResponse.ok;
//import static reactor.core.publisher.Mono.just;
//
///**
// * @author uianz
// * @date 2021/5/14
// */
//@Configuration
//public class Rount {
//
//    @Bean
//    public RouterFunction<?> monoRouterFunciton(PersonHandler personHandler) {
//        return route(GET("/ping"), reqeust -> ok().body(just("pong!"), String.class))
//                .andRoute(GET("/bye"), request -> ok().body(just("bye"), String.class))
//                .andRoute(POST("person").and(accept(MediaType.APPLICATION_JSON)), personHandler::add)
//                .andRoute(GET("/person"), personHandler::list)
//                ;
//    }
//
//    @Component
//    public static class PersonHandler {
//        @Autowired
//        PersonRepository personRepo;
//
//        public Mono<ServerResponse> add(ServerRequest request) {
//            var personMono = request.bodyToMono(Person.class);
//            personRepo.saveAll(personMono);
//            return ServerResponse.ok().body("success!", String.class);
//        }
//
//        public Mono<Person> getById(@PathVariable String id) {
//            return personRepo.findById(id);
//        }
//
//        public Mono<Void> delete(@PathVariable("id") String id) {
//            return personRepo.deleteById(id);
//        }
//
//        public Mono<ServerResponse> list(ServerRequest request) {
//            return ServerResponse.ok().body(personRepo.findAll().take(3), Person.class);
//        }
//    }
//
//}
