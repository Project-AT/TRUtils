#From https://github.com/ProjectHDS/HerodotusUtils/blob/main/build.sh

BUILD=atutils
VERSION=1.1.5

FILE_NAME="$BUILD-$VERSION.jar"
FILE_NAME_DEV="$BUILD-$VERSION-build${GITHUB_RUN_NUMBER}.jar"

mv "$GITHUB_WORKSPACE/build/libs/$FILE_NAME" "$GITHUB_WORKSPACE/artifacts/$FILE_NAME_DEV"