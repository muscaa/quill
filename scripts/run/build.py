import zipfile

import __about__ as a
from scripts.files import SRC, BUILD, BUILD_GENERATED, BUILD_PREBUNDLE
from scripts import generate, gradlew

def run():
    BUILD.mkdir(parents=True, exist_ok=True)
    BUILD_PREBUNDLE.mkdir(parents=True, exist_ok=True)

    generate()
    gradlew(["preBundle"])

    with zipfile.ZipFile(BUILD / f"{a.ID}-bundle.zip", "w", zipfile.ZIP_DEFLATED) as zip:
        for f in SRC.rglob("*"):
            if f.is_file():
                zip.write(f, f.relative_to(SRC))
        for f in BUILD_GENERATED.rglob("*"):
            if f.is_file():
                zip.write(f, f.relative_to(BUILD_GENERATED))
        for f in BUILD_PREBUNDLE.rglob("*"):
            if f.is_file():
                zip.write(f, f.relative_to(BUILD_PREBUNDLE))
            