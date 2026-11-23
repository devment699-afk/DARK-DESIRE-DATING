#!/bin/sh
##############################################################################
##
##   Gradle start up script for POSIX generated for Dark Desire Dating
##
##############################################################################

APP_BASE_NAME=`basename "$0"`
APP_HOME="`pwd -P`"

if command -v gradle >/dev/null 2>&1; then
    exec gradle "$@"
fi

# Fallback to downloading gradle if not found
GRADLE_VERSION="8.2"
GRADLE_ZIP="gradle-${GRADLE_VERSION}-bin.zip"
GRADLE_DIR="$HOME/.gradle/wrapper/dists/gradle-${GRADLE_VERSION}-bin"

if [ ! -d "$GRADLE_DIR" ]; then
    mkdir -p "$GRADLE_DIR"
    echo "Downloading Gradle $GRADLE_VERSION..."
    curl -sSL "https://services.gradle.org/distributions/${GRADLE_ZIP}" -o "/tmp/${GRADLE_ZIP}"
    unzip -q -o "/tmp/${GRADLE_ZIP}" -d "$GRADLE_DIR"
    rm -f "/tmp/${GRADLE_ZIP}"
fi

GRADLE_BIN=$(find "$GRADLE_DIR" -name gradle -type f -perm -u+x | head -n 1)
if [ -n "$GRADLE_BIN" ]; then
    exec "$GRADLE_BIN" "$@"
else
    echo "Error: Could not locate gradle binary" >&2
    exit 1
fi
