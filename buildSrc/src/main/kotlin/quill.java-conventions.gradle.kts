plugins {
    id("java-library")
    id("eclipse")
}

group = rootProject.group
version = rootProject.version
eclipse.project.name = "${rootProject.name}-${project.name}"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

val shade = configurations.create("shade")
configurations.api.get().extendsFrom(shade)

val bootstrap = configurations.create("bootstrap")
configurations.api.get().extendsFrom(bootstrap)

repositories {
    mavenCentral()
}

dependencies {
}

tasks.withType<Jar> {
    from(rootProject.layout.projectDirectory) {
        include("LICENSE", "NOTICE")
        into("META-INF")
    }
}

afterEvaluate {
    tasks.jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        from(shade.map { if (it.isDirectory) it else zipTree(it) })
    }
}
