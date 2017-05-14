resolvers += Resolver.typesafeRepo("releases")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")

// for packaging a FAT JAR, with all dependencies and Scala library...
// just run "sbt assembly" and check Assembler\target\scala-2.11 folder to see jar
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")
