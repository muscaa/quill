from quill.setup import SetupWizard
from quill.setup.versions import V1

def install():
    wizard = SetupWizard.get(__file__)
    if not wizard:
        raise Exception("Unknown setup")
    v1 = V1(wizard)
    v1.copy("wizard.py")
    v1.copy("package.json")

    v1.bins([
        "_/bin/quill",
        "_/bin/quill.cmd",
        "_/bin/quillx",
        "_/bin/quillx.cmd",
    ])

    v1.copy("bin/")
    v1.copy("quill/")
    v1.copy("bootstrap4j/")
    v1.copy("java/")

    v1.owns([
        "@/core/",
        "@/dist/core/",
        "@/main.py",
    ])
    v1.copy("_/core/", "@/core/")
    v1.copy("_/dist/core/", "@/dist/core/")
    v1.copy("_/main.py", "@/main.py")
