from quill.setup.versions import V1

def install():
    v1 = V1(__file__)
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

def uninstall():
    v1 = V1(__file__)
    v1.copy("wizard.py")
    v1.copy("package.json")

    v1.delete("@/bin/quill")
    v1.delete("@/bin/quill.cmd")
    v1.delete("@/bin/quillx")
    v1.delete("@/bin/quillx.cmd")

    v1.delete("bin/")
    v1.delete("quill/")
    v1.delete("bootstrap4j/")
    v1.delete("java/")

    v1.delete("@/core/")
    v1.delete("@/dist/core/")
    v1.delete("@/main.py")
