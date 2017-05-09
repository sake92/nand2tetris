Implementation tips:

1. Tests under folder `ProgramFlow` and `FunctionCalls/SimpleFunction` test are **standalone** VM files.  
This means that NO BOOTSTRAP CODE is required! (You'll get an error!)

2. Tests under folder `FunctionCalls` except `SimpleFunction` DO REQUIrE BOOTSTRAP CODE!  
And they are treated as **folder** of VM files. You should generate just one big ASM file! :)

Good luck!

------------

To run my `VMTranslator.jar` (from 07 folder) do one of following:

- if it's a file, run `java -jar VMTranslator.jar MyFunction.vm NO-BOOTSTRAP`  
You should get a `MyFunction.asm` file in the same folder.

- if it's a folder, run `java -jar VMTranslator.jar MyFolder`
You should get a `MyFolder.asm` file in that folder.

Make sure to **clear RAM** (in CPU emulator) before reloading a test script!
