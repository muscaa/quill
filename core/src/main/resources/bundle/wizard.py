from quill.setup import SetupWizard
from quill.setup.versions import V1

def install():
    wizard = SetupWizard.get(__file__)
    if not wizard:
        raise Exception("Unknown setup")
    v1 = V1(wizard)

    v1.replace("bin/")
    v1.replace("quill/")
    v1.replace("bootstrap4j/")
    v1.replace("java/")

    v1.replace("_/core/", "@/core/")
    v1.replace("_/dist/core/", "@/dist/core/")
    v1.replace("_/main.py", "@/main.py")
