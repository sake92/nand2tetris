/// push constant 0 ///
@0
D=A

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// pop local 0 ///
@LCL
D=M
@0
D=D+A

@R13
M=D

@SP            // SP--
M=M-1

@SP
A=M
D=M

@R13
A=M
M=D

/// label LOOP_START ///
($LOOP_START)

/// push argument 0 ///
@ARG
D=M       // indirect, pointer
@0
A=D+A
D=M

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// push local 0 ///
@LCL
D=M       // indirect, pointer
@0
A=D+A
D=M

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// add ///
@SP            // SP--
M=M-1

@SP
A=M
D=M
@SP            // SP--
M=M-1

@SP
A=M
M=M+D
@SP            // SP++
M=M+1

/// pop local 0 ///
@LCL
D=M
@0
D=D+A

@R13
M=D

@SP            // SP--
M=M-1

@SP
A=M
D=M

@R13
A=M
M=D

/// push argument 0 ///
@ARG
D=M       // indirect, pointer
@0
A=D+A
D=M

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// push constant 1 ///
@1
D=A

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// sub ///
@SP            // SP--
M=M-1

@SP
A=M
D=M
@SP            // SP--
M=M-1

@SP
A=M
M=M-D
@SP            // SP++
M=M+1

/// pop argument 0 ///
@ARG
D=M
@0
D=D+A

@R13
M=D

@SP            // SP--
M=M-1

@SP
A=M
D=M

@R13
A=M
M=D

/// push argument 0 ///
@ARG
D=M       // indirect, pointer
@0
A=D+A
D=M

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// if-goto LOOP_START ///
@SP            // SP--
M=M-1

@SP
A=M
D=M

@$LOOP_START
D;JNE

/// push local 0 ///
@LCL
D=M       // indirect, pointer
@0
A=D+A
D=M

@SP
A=M
M=D
@SP            // SP++
M=M+1

