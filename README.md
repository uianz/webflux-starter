# 这个项目为 webflux+r2dbc 的一个starter项目
- jdk: 11
- db: postgresql
- api doc: knife4j
- 主要框架: springboot, spring-webflux, spring-data-r2dbc,querydsl,spring-data-redis
- 好用的工具: flyway, vavr, hutool, lombok


Todo:
-[ ] gitsubmodule

## 同时引入spring-data-r2dbc和reactive-redis
同时引入spring-data-r2dbc和reactive-redis会报包扫不到的异常，加入@Table解决
https://stackoverflow.com/questions/55495708/is-spring-data-r2dbc-conflicts-with-reactive-redis