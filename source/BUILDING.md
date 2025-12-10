# Building the application

Local builds use Maven. The project includes a `.mvn/settings.xml` that defines alternate mirrors (Spring and Aliyun) to work around environments where Maven Central returns HTTP 403 or is unreachable. Run Maven with the bundled settings file to take advantage of those mirrors:

```bash
mvn -s .mvn/settings.xml clean test
```

If your network still blocks outbound Maven traffic, use the fully-offline workflow (mirrors are ignored and Maven never attempts the network):

1. On a machine with internet access, run `mvn dependency:go-offline` so Maven caches every dependency into `~/.m2/repository`.
2. Copy that `~/.m2/repository` folder into this project as `.m2/repository`.
3. From the project root, run `./scripts/verify-offline-cache.sh` to confirm the cache contains the Spring Boot parent/BOM POMs **and the required jars**.
4. Back in the offline environment, build against the bundled cache only:

```bash
mvn -s .mvn/local-repo-settings.xml -o clean test
```

5. If Maven still complains about a missing parent POM, manually download `spring-boot-starter-parent-3.3.5.pom` into `.m2/repository/org/springframework/boot/spring-boot-starter-parent/3.3.5/`. For broader cache gaps, copy the full `~/.m2/repository` from a networked machine or drop a `offline-repo.tar.gz` export into `.mvn/offline-cache/` and extract it there before rerunning the verifier.

The `local-repo-settings.xml` file forces Maven to use the project-local `.m2/repository` folder and enables offline mode so no external downloads are attempted.
