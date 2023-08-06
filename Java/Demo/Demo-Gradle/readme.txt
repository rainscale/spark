查看所有任务：
./gradlew tasks --all

命令可以简写：
./gradlew assembleRelease 简写为：./gradlew aR

查看app项目的依赖：
./gradlew :app:dependencies

清除build文件夹：
./gradlew clean

检查依赖并编译打包：
./gradlew build

编译并打印日志：
./gradlew build --info

强制更新最新依赖，清除构建并构建：
./gradlew clean --refresh-dependencies build
注意build命令把debug、release环境的包都打出来，如构建指定的包使用：
构建并打Debug包：
./gradlew assembleDebug
简写：
./gradlew aD
构建并打Release包：
./gradlew assembleRelease
简写：
./gradlew aR
全渠道打包：
./gradlew assemble

构建并安装调试命令：
编译app module并打Debug包：
./gradlew install app:assembleDebug

Release模式打包并安装：
./gradlew installRelease

卸载Release模式包：
./gradlew uninstallRelease

安装/查看包依赖：
./gradlew dependencies

编译时的依赖库：
./gradlew app:dependencies --configuration compile

运行时的依赖库：
./gradlew app:dependencies --configuration runtime

使用离线模式：
./gradlew aDR --offline

守护进程：
./gradlew build --daemon

并行编译模式：
./gradlew build --parallel --parallel-threads=N

按需编译模式：
./gradlew build --configure-on-demand

spark@Spark:~/Works/tmp$ mkdir Demo
spark@Spark:~/Works/tmp$ cd Demo/
spark@Spark:~/Works/tmp/Demo$ gradle init

Select type of project to generate:
  1: basic
  2: application
  3: library
  4: Gradle plugin
Enter selection (default: basic) [1..4] 2

Select implementation language:
  1: C++
  2: Groovy
  3: Java
  4: Kotlin
  5: Swift
Enter selection (default: Java) [1..5] 3

Select build script DSL:
  1: Groovy
  2: Kotlin
Enter selection (default: Groovy) [1..2] 1

Select test framework:
  1: JUnit 4
  2: TestNG
  3: Spock
  4: JUnit Jupiter
Enter selection (default: JUnit 4) [1..4] 1

Project name (default: Demo):
Source package (default: Demo): ale.rains.demo

> Task :init
Get more help with your project: https://docs.gradle.org/6.5/userguide/tutorial_java_projects.html

BUILD SUCCESSFUL in 53s
2 actionable tasks: 2 executed

spark@Spark:~/Works/tmp/Demo$ ls
build.gradle  gradle  gradlew  gradlew.bat  settings.gradle  src

spark@Spark:~/Works/tmp/Demo$ tree
.
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── ale
    │   │       └── rains
    │   │           └── demo
    │   │               └── App.java
    │   └── resources
    └── test
        ├── java
        │   └── ale
        │       └── rains
        │           └── demo
        │               └── AppTest.java
        └── resources

15 directories, 8 files

编译：
spark@Spark:~/Works/tmp/Demo$ ./gradlew build

BUILD SUCCESSFUL in 3s
7 actionable tasks: 7 executed

运行：
spark@Spark:~/Works/tmp/Demo$ ./gradlew run

> Task :run
Hello world.

BUILD SUCCESSFUL in 429ms
2 actionable tasks: 1 executed, 1 up-to-date