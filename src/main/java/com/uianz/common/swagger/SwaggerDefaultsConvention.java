package com.uianz.common.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.databind.node.POJONode;
import io.r2dbc.postgresql.codec.Json;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.schema.WildcardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Component
@Profile(("!prod"))
public class SwaggerDefaultsConvention implements AlternateTypeRuleConvention {
    @Autowired
    private TypeResolver typeResolver;

    @Override
    public List<AlternateTypeRule> rules() {
        ArrayList<AlternateTypeRule> rules = new ArrayList<>();
        //有多少个泛形，就有多少个WildcardType.class
        rules.add(
                newRule(
                        typeResolver.resolve(io.vavr.collection.List.class, WildcardType.class),
                        typeResolver.resolve(List.class, WildcardType.class)));
        rules.add(
                newRule(
                        typeResolver.resolve(Set.class, WildcardType.class),
                        typeResolver.resolve(java.util.Set.class, WildcardType.class)));
        rules.add(
                newRule(
                        typeResolver.resolve(Map.class, WildcardType.class, WildcardType.class),
                        typeResolver.resolve(java.util.Map.class, WildcardType.class, WildcardType.class)));
        rules.add(
                newRule(
                        typeResolver.resolve(Option.class, WildcardType.class),
                        typeResolver.resolve(Optional.class, WildcardType.class)));
        rules.add(
                newRule(
                        typeResolver.resolve(Mono.class, WildcardType.class),
                        typeResolver.resolve(WildcardType.class)));
        rules.add(
                newRule(
                        typeResolver.resolve(Flux.class, WildcardType.class),
                        typeResolver.resolve(List.class, WildcardType.class)));
        rules.add(
                newRule(
                        typeResolver.resolve(Json.class),
                        typeResolver.resolve(POJONode.class)));
        return rules;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}