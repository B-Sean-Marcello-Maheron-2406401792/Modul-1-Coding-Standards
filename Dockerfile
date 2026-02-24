# Stage 1: Builder
FROM docker.io/library/eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /src/advshop
COPY . .

# Menjalankan build dengan verifikasi dependensi
RUN ./gradlew clean bootJar --no-daemon

# Stage 2: Runner
FROM docker.io/library/eclipse-temurin:21-jre-alpine AS runner

ARG USER_NAME=advshop
ARG USER_UID=1000
ARG USER_GID=${USER_UID}

# Membuat grup dan user non-root untuk keamanan
RUN addgroup -g ${USER_GID} ${USER_NAME} \
    && adduser -h /opt/advshop -D -u ${USER_UID} -G ${USER_NAME} ${USER_NAME}

USER ${USER_NAME}
WORKDIR /opt/advshop

# Menyalin file JAR dari stage builder dengan kepemilikan user yang tepat
COPY --from=builder --chown=${USER_UID}:${USER_GID} /src/advshop/build/libs/*.jar app.jar

# Ekspos port sesuai konfigurasi Koyeb kamu (8080)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]