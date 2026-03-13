plugins {
    id("java-library")
    id("com.vanniktech.maven.publish")
    id("eclipse")
}

group = "dev.musca.${rootProject.name}"
version = System.getenv("GITHUB_REF_NAME")?.removePrefix("v") ?: "0.0.1-SNAPSHOT"
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

mavenPublishing {
    publishToMavenCentral(/*automaticRelease = true*/)
    signAllPublications()
    coordinates(project.group.toString(), project.name, project.version.toString())

    val developerId = "muscaa"
    val developerName = "musca"
    val projectId = rootProject.name

    pom {
        name.set("Quill")
        description.set("Quill CLI app")
        inceptionYear.set("2024")
        url.set("https://github.com/${developerId}/${projectId}/")
        licenses {
            license {
                name.set("Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set(developerId)
                name.set(developerName)
                url.set("https://github.com/${developerId}/")
            }
        }
        scm {
            url.set("https://github.com/${developerId}/${projectId}/")
            connection.set("scm:git:git://github.com/${developerId}/${projectId}.git")
            developerConnection.set("scm:git:ssh://git@github.com/${developerId}/${projectId}.git")
        }
    }
}
