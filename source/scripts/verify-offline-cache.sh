#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
LOCAL_REPO="${ROOT_DIR}/.m2/repository"
PARENT_POM="${LOCAL_REPO}/org/springframework/boot/spring-boot-starter-parent/3.3.5/spring-boot-starter-parent-3.3.5.pom"
BOM_POM="${LOCAL_REPO}/org/springframework/boot/spring-boot-dependencies/3.3.5/spring-boot-dependencies-3.3.5.pom"

# Core artifacts we expect to be present in the offline cache for this project.
declare -A REQUIRED_ARTIFACTS=(
  ["org/springframework/boot/spring-boot-starter-web/3.3.5/spring-boot-starter-web-3.3.5.jar"]="Spring Boot Web starter"
  ["org/springframework/boot/spring-boot-starter-thymeleaf/3.3.5/spring-boot-starter-thymeleaf-3.3.5.jar"]="Spring Boot Thymeleaf starter"
  ["org/springframework/boot/spring-boot-starter-data-jpa/3.3.5/spring-boot-starter-data-jpa-3.3.5.jar"]="Spring Boot JPA starter"
  ["org/springframework/boot/spring-boot-starter-test/3.3.5/spring-boot-starter-test-3.3.5.jar"]="Spring Boot test starter"
  ["org/springframework/boot/spring-boot-devtools/3.3.5/spring-boot-devtools-3.3.5.jar"]="Spring Boot devtools"
  ["org/xerial/sqlite-jdbc/3.45.2.0/sqlite-jdbc-3.45.2.0.jar"]="SQLite JDBC driver"
  ["com/github/gwenn/sqlite-dialect/0.1.2/sqlite-dialect-0.1.2.jar"]="SQLite Hibernate dialect"
  ["org/projectlombok/lombok/1.18.34/lombok-1.18.34.jar"]="Lombok"
  ["com/h2database/h2/2.2.224/h2-2.2.224.jar"]="H2 in-memory database"
)

missing=()
if [[ ! -d "${LOCAL_REPO}" ]]; then
  missing+=("${LOCAL_REPO} (directory)")
fi

for artifact in "${PARENT_POM}" "${BOM_POM}"; do
  [[ -f "${artifact}" ]] || missing+=("${artifact}")
done

for path in "${!REQUIRED_ARTIFACTS[@]}"; do
  [[ -f "${LOCAL_REPO}/${path}" ]] || missing+=("${LOCAL_REPO}/${path} (${REQUIRED_ARTIFACTS[$path]})")
done

if (( ${#missing[@]} > 0 )); then
  echo "Missing required cached artifacts for offline Maven builds:" >&2
  for entry in "${missing[@]}"; do
    rel_path=${entry#"${ROOT_DIR}/"}
    echo " - ${rel_path}" >&2
  done
  cat >&2 <<'EOT'

To fix:
1) On a machine with internet access, run: mvn dependency:go-offline
2) Copy the resulting ~/.m2/repository into this project as .m2/repository
3) Re-run this script to confirm the cache is present
4) Execute: mvn -s .mvn/local-repo-settings.xml -o clean test

If only the parent POM is missing, manually download spring-boot-starter-parent-3.3.5.pom into the path above.
If other jars are missing, copy them from your populated ~/.m2/repository or place an offline-repo .tar.gz in .mvn/offline-cache and extract it here.
EOT
  exit 1
fi

echo "Local Maven cache looks ready for offline Spring Boot builds."
echo "Next: mvn -s .mvn/local-repo-settings.xml -o clean test"
