import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    id("com.github.ben-manes.versions") version "0.44.0"
    id("org.sonarqube") version "3.5.0.2730"
    jacoco
    distribution
}

group = "com.ryzingtitan"
version = "1.6.0"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("org.junit.platform:junit-platform-suite-api:1.9.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("io.cucumber:cucumber-java:7.10.1")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.10.1")
    testImplementation("io.cucumber:cucumber-spring:7.10.1")
    testImplementation("io.projectreactor:reactor-test:3.5.1")
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:4.3.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.register<Copy>("installGitHooks") {
    dependsOn("processResources")
    dependsOn("processTestResources")
    dependsOn("ktlintMainSourceSetCheck")
    dependsOn("ktlintTestSourceSetCheck")
    dependsOn("ktlintKotlinScriptCheck")
    from(rootProject.rootDir) {
        include("**/pre-commit")
    }
    into(".git/hooks")
}

tasks.getByName("compileKotlin") {
    dependsOn("installGitHooks")
}

ktlint {
    version.set("0.45.2")
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    reporters {
        reporter(ReporterType.JSON)
    }
}

detekt {
    source = objects.fileCollection().from(
        io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_SRC_DIR_KOTLIN,
        io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_TEST_SRC_DIR_KOTLIN
    )
    buildUponDefaultConfig = true
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "17"

    reports {
        html.required.set(true)
        html.outputLocation.set(file("${rootProject.rootDir}/${rootProject.name}/detektHtmlReport/detekt.html"))
    }
}

jacoco {
    toolVersion = "0.8.8"
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.outputLocation.set(file("${rootProject.rootDir}/${rootProject.name}/jacocoHtmlReport"))
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.84".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn("jacocoTestCoverageVerification")
}

tasks.withType<DependencyUpdatesTask> {
    checkForGradleUpdate = true
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"

    resolutionStrategy {
        componentSelection {
            all {
                if (candidate.version.isNonStable() && !currentVersion.isNonStable()) {
                    reject("Release candidate")
                }
            }
        }
    }
}

fun String.isNonStable(): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(this)
    return isStable.not()
}

distributions {
    main {
        contents {
            into("/") {
                from("src/main/resources")
                include("application.yaml")
            }
            into("/") {
                from("build/libs")
                include("${rootProject.name}-${rootProject.version}.jar")
            }
        }
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "ryzingTitan_datalog-parser")
        property("sonar.organization", "ryzingtitan")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

tasks.getByName("distTar") {
    enabled = false
}

tasks.getByName("distZip") {
    dependsOn("bootJar")
}
