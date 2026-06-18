#!/bin/bash

set -euo pipefail

TMP_ZIP="$(mktemp /tmp/quill.XXXXXX.zip)"

cleanup() {
    rm -f "$TMP_ZIP"
    rm -f "$0"
}
trap cleanup EXIT

if ! curl -fL -o "$TMP_ZIP" https://github.com/muscaa/quill/releases/latest/download/quill-bundle.zip; then
    exit 1
fi

python3 "$TMP_ZIP"
