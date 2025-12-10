# Offline Maven cache drop-in

Place a compressed export of a known-good Maven repository (for example, `offline-repo.tar.gz` created from `~/.m2/repository`) in this directory.

From the project root you can then extract it into `.m2/repository`:

```bash
mkdir -p .m2
 tar -xzf .mvn/offline-cache/offline-repo.tar.gz -C .m2
```

After extracting, rerun `./scripts/verify-offline-cache.sh` to confirm that the Spring Boot parent/BOM POMs and required jars are present.
