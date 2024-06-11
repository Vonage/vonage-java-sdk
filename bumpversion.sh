if [ "$1" = "" ]
then
  echo "Usage: $0 <new version>"
  exit 1
fi

mvn versions:set -DnewVersion=$1
mvn validate
mvn versions:display-plugin-updates
mvn versions:display-dependency-updates
rm pom.xml.releaseBackup pom.xml.versionsBackup