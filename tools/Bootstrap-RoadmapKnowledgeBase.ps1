param(
    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"

$repoRoot = Split-Path -Parent $PSScriptRoot
$jarPath = Join-Path $repoRoot "target\AdapriveLearningNavigator-0.0.1-SNAPSHOT.jar"

function Resolve-CommandPath {
    param(
        [string]$CommandName
    )

    $command = Get-Command $CommandName -ErrorAction SilentlyContinue
    if ($null -eq $command) {
        throw "Команда '$CommandName' не найдена в PATH."
    }

    return $command.Source
}

$mvn = Resolve-CommandPath "mvn"
$java = Resolve-CommandPath "java"

if (-not $SkipBuild) {
    Write-Host "Собираем проект..."
    & $mvn package -DskipTests
}

if (-not (Test-Path $jarPath)) {
    throw "Не найден jar-файл: $jarPath"
}

Write-Host "Запускаем одноразовый bootstrap KB из roadmap.sh..."
& $java -jar $jarPath --spring.profiles.active=roadmap-bootstrap
