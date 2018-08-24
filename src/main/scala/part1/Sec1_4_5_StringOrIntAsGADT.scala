package part1

/**
  * TODO documentation is work-in-progress
  * I use GADT in the name to emphasise GADT-nes.
  *
  * Scala version of
  * https://github.com/rpeszek/IdrisTddNotes/wiki/Part1_Sec1_4_5
  * Refs:
  * https://stackoverflow.com/questions/33818791/stringorint-from-idris-scala
  */
object Sec1_4_5_StringOrIntAsGADT {

 // kind Bool
 sealed trait KBool
 sealed trait True extends KBool
 sealed trait False extends KBool

 // singleton for kind Bool
 sealed trait SBool[B <: KBool]
 case object STrue extends SBool[True]
 case object SFalse extends SBool[False]

 // type constructor
 trait StringOrInt[B <: KBool] {
   type Out
 }

 object StringOrInt {

   type Aux[B <: KBool, Out0] = StringOrInt[B] { type Out = Out0 }

   implicit val trueToInt = new StringOrInt[True] { type Out = Int }
   implicit val falseToString = new StringOrInt[False] { type Out = String }
 }

 object getStringOrInt {

   sealed trait Case[A] { val a: A }

   implicit val intCase    = new Case[Int] { val a = 10 }
   implicit val stringCase = new Case[String] { val a = "Hello" }

   def apply[B <: KBool, O](bool: SBool[B])
                           (implicit ev: StringOrInt.Aux[B, O], value: Case[O]): O = value.a
 }

 object valToString{

   sealed trait Mapping[A] extends (A => String)

   implicit val intMap = new Mapping[Int] {
      def apply(a: Int): String = a.toString
   }

   implicit val stringMap = new Mapping[String] {
     def apply(a: String): String = a
   }

   def apply[B <: KBool, A](bool: SBool[B], a: A)
                           (implicit ev: StringOrInt.Aux[B, A], map: Mapping[A]): String =
     map(a)
 } 
}
