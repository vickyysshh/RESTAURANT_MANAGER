@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup bat script
@REM ----------------------------------------------------------------------------
@IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)
@SET DP0=%~dp0
@SET MAVEN_PROJECTBASEDIR=%DP0%
@SET MVNW_REPOURL=https://repo.maven.apache.org/maven2

@SET WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
@SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@SET DOWNLOAD_URL="%MVNW_REPOURL%/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

@IF EXIST %WRAPPER_JAR% (
    @SET DOWNLOAD_REQUIRED=false
) ELSE (
    @SET DOWNLOAD_REQUIRED=true
)

@IF "%DOWNLOAD_REQUIRED%"=="true" (
    powershell -Command "Invoke-WebRequest -Uri %DOWNLOAD_URL% -OutFile %WRAPPER_JAR%"
)

@REM Auto-free Spring port when launching the app to prevent frequent port-in-use failures.
@IF /I "%~1"=="spring-boot:run" (
    FOR /F %%P IN ('powershell -NoProfile -Command "$c = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue; if ($c) { $proc = Get-Process -Id $c.OwningProcess -ErrorAction SilentlyContinue; if ($proc -and $proc.ProcessName -eq 'java') { $c.OwningProcess } }"') DO (
        taskkill /PID %%P /F >nul 2>&1
    )
)

java "-Dmaven.multiModuleProjectDirectory=%CD%" -cp %WRAPPER_JAR% %WRAPPER_LAUNCHER% %*
