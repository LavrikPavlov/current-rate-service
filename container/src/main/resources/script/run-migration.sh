#!/bin/bash
# Запуск миграции на БД

OS=$(uname)

if [[ "$OS" == "Darwin" ]]; then
  # macOS
  readonly MAVEN_HOME="/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin"
elif [[ "$OS" == "MINGW"* || "$OS" == "CYGWIN"* || "$OS" == "MSYS"* ]]; then
  # Windows
  readonly MAVEN_HOME="C:/Program Files/JetBrains/IntelliJ IDEA/plugins/maven/lib/maven3/bin"
else
  echo "Операционная система не поддерживается."
  exit 1
fi

echo "Операционная система: $OS"

cd "$(dirname "$0")/../../../.." || exit

"$MAVEN_HOME/mvn" liquibase:update