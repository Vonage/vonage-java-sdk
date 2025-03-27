if [ "$1" = "" ]
then
  echo "Usage: $0 <new version>"
  exit 1
fi

mvn versions:set -DnewVersion="$1"
mvn validate
rm pom.xml.versionsBackup #pom.xml.releaseBackup
#mvn versions:display-plugin-updates
#mvn versions:display-dependency-updates