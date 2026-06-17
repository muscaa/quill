#!/bin/bash

TMP_ZIP="$(mktemp /tmp/quill.XXXXXX.zip)"
trap 'rm -f "$TMP_ZIP"' EXIT

curl -fL -o "$TMP_ZIP" https://github.com/muscaa/quill/releases/latest/download/quill-bundle.zip

python3 $TMP_ZIP
