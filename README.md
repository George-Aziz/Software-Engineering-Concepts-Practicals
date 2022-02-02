# Software Engineering Concepts Practicals
**Date created:** 04/08/2021

**Date last modified:** 13/10/2021

## Purpose:
Multiple programs to illustrate different software engineering concepts such as multi threading, build engineering, language integration, domain-specific languages and internationalisation

## Programs:
1. **Prac_2** - Highly simplified version of cron in Java, using multithreading

2. **Prac_3** - Java GUI application that searches for files by name using blocking queues (Q4) or thread pools (Q5)

3. **Prac_4** - Basic “Diff” program created in Java that contains a CLI and GUI version that can be built through gradle subprojects

4. **Prac_5** - IO Class created in Java Native Interface (JNI)

5. **Prac_6** - Simple program to illustrate the use of the Java reflection API

6. **Prac_7** - Creating Domain-Specific Language (DSL) by making a parser in JavaCC to specify bus times and routes

7. **Prac_8** - Bus timetable app that has been internationalised to support Arabic and English

## Functionality:

* For the Prac_2, use `javac *.java` then `java cron`

<br>

* Prac_4 has multiple build files 
    * For a CLI build: `./gradlew :cli:build`
    * For a GUI build: `./gradlew :gui:build` or `./gradlew build`

<br>

* Prac_8 supports 2 locales - English (en / en-Au) and Arabic (ar)
    * For the default (English) locale: `./gradlew run`
    * For a specific locale: `./gradlew run --args='--locale=[value]'`

<br>

* The rest of the projects get compiled and run by using `./gradlew clean build` first and then `./gradlew run` to run
