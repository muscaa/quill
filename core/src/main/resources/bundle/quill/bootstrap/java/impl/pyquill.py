import jpype as j

@j.JImplements("quill.bootstrap.PyQuill")
class PyQuillImpl:

    @j.JOverride
    def schedule(self, args):
        print(f"typeof args: {type(args)}")
        print(f"actual args: {args}")

        arg0 = args[0]
        print(f"typeof arg0: {type(arg0)}")
        print(f"actual arg0: {arg0}")

        arg0s = str(args[0])
        print(f"typeof arg0s: {type(arg0s)}")
        print(f"actual arg0s: {arg0s}")
