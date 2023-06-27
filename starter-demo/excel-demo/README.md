
- 分页
- ResultHandler
- Cursor  MyBatisCursorItemReader(mybatis-spring)

```java
try (SqlSession session = sqlSessionFactory.openSession()) {
    Cursor<TeamEntity> cursor = session.selectCursor("com.github.mjeanroy.mybatisissue.TeamEntity.findAll");
    return StreamSupport.stream(cursor.spliterator(), false)
        .map(team -> new TeamDto(team))
        .collect(Collectors.toList());
}
```