if [ "$1" = "" ]
then
  echo "Usage: $0 <new version>"
  exit 1
fi

python -m pip install --upgrade pip
pip install bump2version
bump2version --new-version "$1" patch
