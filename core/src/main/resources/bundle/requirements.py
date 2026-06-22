from quill.setup.versions import V1

def resolve():
    v1 = V1(__file__)

    print(str(v1.resolve("@/db/cache")))
