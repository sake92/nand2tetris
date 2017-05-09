////////// BOOTSTRAP CODE //////////
@256
D=A
@SP
M=D

/// call Sys.init 0 ///
@RETURN_0
D=A
@SP
A=M
M=D
@SP            // SP++
M=M+1



// pushing segment LCL on the stack !
@LCL
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment ARG on the stack !
@ARG
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THIS on the stack !
@THIS
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THAT on the stack !
@THAT
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1



// ARG = SP-n-5
@SP
D=M
@5
D=D-A
@0
D=D-A
@ARG
M=D

// LCL = SP
@SP
D=M
@LCL
M=D

// goto f
@Sys.init
0;JMP

(RETURN_0)

////////// BOOTSTRAP CODE END //////////

/// function Class1.set 0 ///
(Class1.set)

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

/// pop static 0 ///
@Class1.0
D=A

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

/// push argument 1 ///
@ARG
D=M       // indirect, pointer
@1
A=D+A
D=M

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// pop static 1 ///
@Class1.1
D=A

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

/// push constant 0 ///
@0
D=A

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// return ///
// FRAME = LCL       // FRAME  is a temporary variable R7
@LCL
D=M
@R7
M=D

// RET = *(FRAME-5)  // store RETURN-address in a temp var R14
@5
A=D-A
D=M
@R14
M=D

// *ARG = pop()      // Reposition the return value for the caller
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



// SP = ARG+1        // Restore SP of the caller
@ARG
D=M
@SP
M=D+1

// THAT = *(FRAME-1) // Restore THAT of the caller
@R7
D=M
@1
A=D-A
D=M

@THAT
M=D

// THIS = *(FRAME-2) // Restore THIS of the caller
@R7
D=M
@2
A=D-A
D=M

@THIS
M=D

// ARG = *(FRAME-3)  // Restore ARG of the caller
@R7
D=M
@3
A=D-A
D=M

@ARG
M=D

// LCL = *(FRAME-4)  // Restore LCL of the caller
@R7
D=M
@4
A=D-A
D=M

@LCL
M=D


// goto RET // Goto return-address (in the caller’s code)
@R14
A=M
0;JMP

/// function Class1.get 0 ///
(Class1.get)

/// push static 0 ///
@Class1.0
D=M       // indirect, pointer, VARIABLE! :)

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// push static 1 ///
@Class1.1
D=M       // indirect, pointer, VARIABLE! :)

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

/// return ///
// FRAME = LCL       // FRAME  is a temporary variable R7
@LCL
D=M
@R7
M=D

// RET = *(FRAME-5)  // store RETURN-address in a temp var R14
@5
A=D-A
D=M
@R14
M=D

// *ARG = pop()      // Reposition the return value for the caller
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



// SP = ARG+1        // Restore SP of the caller
@ARG
D=M
@SP
M=D+1

// THAT = *(FRAME-1) // Restore THAT of the caller
@R7
D=M
@1
A=D-A
D=M

@THAT
M=D

// THIS = *(FRAME-2) // Restore THIS of the caller
@R7
D=M
@2
A=D-A
D=M

@THIS
M=D

// ARG = *(FRAME-3)  // Restore ARG of the caller
@R7
D=M
@3
A=D-A
D=M

@ARG
M=D

// LCL = *(FRAME-4)  // Restore LCL of the caller
@R7
D=M
@4
A=D-A
D=M

@LCL
M=D


// goto RET // Goto return-address (in the caller’s code)
@R14
A=M
0;JMP

/// function Class2.set 0 ///
(Class2.set)

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

/// pop static 0 ///
@Class2.0
D=A

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

/// push argument 1 ///
@ARG
D=M       // indirect, pointer
@1
A=D+A
D=M

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// pop static 1 ///
@Class2.1
D=A

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

/// push constant 0 ///
@0
D=A

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// return ///
// FRAME = LCL       // FRAME  is a temporary variable R7
@LCL
D=M
@R7
M=D

// RET = *(FRAME-5)  // store RETURN-address in a temp var R14
@5
A=D-A
D=M
@R14
M=D

// *ARG = pop()      // Reposition the return value for the caller
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



// SP = ARG+1        // Restore SP of the caller
@ARG
D=M
@SP
M=D+1

// THAT = *(FRAME-1) // Restore THAT of the caller
@R7
D=M
@1
A=D-A
D=M

@THAT
M=D

// THIS = *(FRAME-2) // Restore THIS of the caller
@R7
D=M
@2
A=D-A
D=M

@THIS
M=D

// ARG = *(FRAME-3)  // Restore ARG of the caller
@R7
D=M
@3
A=D-A
D=M

@ARG
M=D

// LCL = *(FRAME-4)  // Restore LCL of the caller
@R7
D=M
@4
A=D-A
D=M

@LCL
M=D


// goto RET // Goto return-address (in the caller’s code)
@R14
A=M
0;JMP

/// function Class2.get 0 ///
(Class2.get)

/// push static 0 ///
@Class2.0
D=M       // indirect, pointer, VARIABLE! :)

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// push static 1 ///
@Class2.1
D=M       // indirect, pointer, VARIABLE! :)

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

/// return ///
// FRAME = LCL       // FRAME  is a temporary variable R7
@LCL
D=M
@R7
M=D

// RET = *(FRAME-5)  // store RETURN-address in a temp var R14
@5
A=D-A
D=M
@R14
M=D

// *ARG = pop()      // Reposition the return value for the caller
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



// SP = ARG+1        // Restore SP of the caller
@ARG
D=M
@SP
M=D+1

// THAT = *(FRAME-1) // Restore THAT of the caller
@R7
D=M
@1
A=D-A
D=M

@THAT
M=D

// THIS = *(FRAME-2) // Restore THIS of the caller
@R7
D=M
@2
A=D-A
D=M

@THIS
M=D

// ARG = *(FRAME-3)  // Restore ARG of the caller
@R7
D=M
@3
A=D-A
D=M

@ARG
M=D

// LCL = *(FRAME-4)  // Restore LCL of the caller
@R7
D=M
@4
A=D-A
D=M

@LCL
M=D


// goto RET // Goto return-address (in the caller’s code)
@R14
A=M
0;JMP

/// function Sys.init 0 ///
(Sys.init)

/// push constant 6 ///
@6
D=A

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// push constant 8 ///
@8
D=A

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// call Class1.set 2 ///
@RETURN_1
D=A
@SP
A=M
M=D
@SP            // SP++
M=M+1



// pushing segment LCL on the stack !
@LCL
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment ARG on the stack !
@ARG
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THIS on the stack !
@THIS
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THAT on the stack !
@THAT
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1



// ARG = SP-n-5
@SP
D=M
@5
D=D-A
@2
D=D-A
@ARG
M=D

// LCL = SP
@SP
D=M
@LCL
M=D

// goto f
@Class1.set
0;JMP

(RETURN_1)
/// pop temp 0 ///
@R5
D=A
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

/// push constant 23 ///
@23
D=A

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// push constant 15 ///
@15
D=A

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// call Class2.set 2 ///
@RETURN_2
D=A
@SP
A=M
M=D
@SP            // SP++
M=M+1



// pushing segment LCL on the stack !
@LCL
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment ARG on the stack !
@ARG
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THIS on the stack !
@THIS
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THAT on the stack !
@THAT
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1



// ARG = SP-n-5
@SP
D=M
@5
D=D-A
@2
D=D-A
@ARG
M=D

// LCL = SP
@SP
D=M
@LCL
M=D

// goto f
@Class2.set
0;JMP

(RETURN_2)
/// pop temp 0 ///
@R5
D=A
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

/// call Class1.get 0 ///
@RETURN_3
D=A
@SP
A=M
M=D
@SP            // SP++
M=M+1



// pushing segment LCL on the stack !
@LCL
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment ARG on the stack !
@ARG
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THIS on the stack !
@THIS
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THAT on the stack !
@THAT
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1



// ARG = SP-n-5
@SP
D=M
@5
D=D-A
@0
D=D-A
@ARG
M=D

// LCL = SP
@SP
D=M
@LCL
M=D

// goto f
@Class1.get
0;JMP

(RETURN_3)
/// call Class2.get 0 ///
@RETURN_4
D=A
@SP
A=M
M=D
@SP            // SP++
M=M+1



// pushing segment LCL on the stack !
@LCL
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment ARG on the stack !
@ARG
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THIS on the stack !
@THIS
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THAT on the stack !
@THAT
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1



// ARG = SP-n-5
@SP
D=M
@5
D=D-A
@0
D=D-A
@ARG
M=D

// LCL = SP
@SP
D=M
@LCL
M=D

// goto f
@Class2.get
0;JMP

(RETURN_4)
/// label WHILE ///
(Sys.init$WHILE)

/// goto WHILE ///
@Sys.init$WHILE
0;JMP

