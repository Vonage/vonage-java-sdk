if [ "$1" = "" ]
then
  echo "Usage: $0 <new version>"
  exit 1
fi

python3 -m pip install --upgrade pip
pip3 install bump2version
bump2version --new-version "$1" patch
