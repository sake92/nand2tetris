package ba.sake.nand2tetris.assembler

/**
 * Contains a list of tests. (.asm file as string, and expected .hack output)
 */
object TestData {

  val add = (
    "Add.asm",
    """|// Computes R0 = 2 + 3  (R0 refers to RAM[0])
       |@2
       |D=A
       |@3
       |D=D+A
       |@0
       |M=D
       |""".stripMargin,
    """|0000000000000010
       |1110110000010000
       |0000000000000011
       |1110000010010000
       |0000000000000000
       |1110001100001000
       |""".stripMargin
  )

  val max = (
    "Max.asm",
    """|// Computes R2 = max(R0, R1)  (R0,R1,R2 refer to RAM[0],RAM[1],RAM[2])
       |   @R0
       |   D=M              // D = first number
       |   @R1
       |   D=D-M            // D = first number - second number
       |   @OUTPUT_FIRST
       |   D;JGT            // if D>0 (first is greater) goto output_first
       |   @R1
       |   D=M              // D = second number
       |   @OUTPUT_D
       |   0;JMP            // goto output_d
       |(OUTPUT_FIRST)
       |   @R0
       |   D=M              // D = first number
       |(OUTPUT_D)
       |   @R2
       |   M=D              // M[2] = D (greatest number)
       |(INFINITE_LOOP)
       |   @INFINITE_LOOP
       |   0;JMP            // infinite loop
       |""".stripMargin,
    """|0000000000000000
       |1111110000010000
       |0000000000000001
       |1111010011010000
       |0000000000001010
       |1110001100000001
       |0000000000000001
       |1111110000010000
       |0000000000001100
       |1110101010000111
       |0000000000000000
       |1111110000010000
       |0000000000000010
       |1110001100001000
       |0000000000001110
       |1110101010000111
       |""".stripMargin
  )

  val tests = Seq(add, max)
}