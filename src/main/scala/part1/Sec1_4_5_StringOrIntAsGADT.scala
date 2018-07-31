package part1

/**
  * TODO documentation is work-in-progress
  * I use GADT in the name to emphasise GADT-nes.
  * 
  * Refs:
  * https://github.com/rpeszek/IdrisTddNotes/wiki/Part1_Sec1_4_5
  * https://stackoverflow.com/questions/33818791/stringorint-from-idris-scala
  */
object Sec1_4_5_StringOrIntAsGADT {
  
  sealed trait StringOrIntGADT[T]
  case class S(s: String) extends StringOrIntGADT[String]
  case class I(i: Int) extends StringOrIntGADT[Int]

  /** this compiles with 2.12.6 (did not with 2.12.2)
       before I would have to use ah-hoc polymorphic extract */
  def extractStringOrInt[T](t: StringOrIntGADT[T]) : T =
    t match {
      case S(s) => s
      case I(i) => i
    }

  /** This is unique Scala, it allows me to convert values of type T to StringOrIntGADT[T] */
  sealed trait EncodeInStringOrInt[T] {
      def apply(t: T): StringOrIntGADT[T] 
  }
  object EncodeInStringOrInt {
     implicit val encodeString : EncodeInStringOrInt[String] = new EncodeInStringOrInt[String]{
       def apply(t: String) = S(t)
     }
     implicit val encodeInt : EncodeInStringOrInt[Int] = new EncodeInStringOrInt[Int]{
       def apply(t: Int) = I(t)
     }
  }
  
  /** Singleton-like construction, This is not needed in StringOrInt example
    but is good to examine */
  sealed trait IsStringOrIntGADT[T]
  case object IsS extends IsStringOrIntGADT[String]
  case object IsI extends IsStringOrIntGADT[Int]

  /** Again this is just to see Scala language capability */
  def defaultStuff[T](c: IsStringOrIntGADT[T]) : StringOrIntGADT[T] =
    c match {
      case IsS => S("Hello")
      case IsI => I(2)
    }

  /** Subtyping provides type level Boolean */
  sealed trait Bool
  case object True extends Bool
  case object False extends Bool
  
  /** Provides type level mapping between Bool and String/Int types. 
    * Somewhat mimicking type family concept in type theory or Haskell */
  sealed trait DecideStringOrIntGADT[B, T]
  case object PickS extends DecideStringOrIntGADT[False.type, String]
  case object PickI extends DecideStringOrIntGADT[True.type, Int]

  object DecideStringOrIntGADT {
    implicit val trueInt: DecideStringOrIntGADT[True.type, Int] = PickI
    implicit val falseString: DecideStringOrIntGADT[False.type, String] = PickS
  }
  
  def pickStringOrInt[B, T](c: DecideStringOrIntGADT[B, T]) : StringOrIntGADT[T] =
    c match {
      case PickS => S("Hello")
      case PickI => I(2)
    }

  /** Implements Idris book example getStringOrInt function using GADT approach and type 
    * level Boolean. */ 
  def getStringOrInt[T](b: Bool)(implicit ev: DecideStringOrIntGADT[b.type, T]): T =
    extractStringOrInt(pickStringOrInt(ev))

  /** Note, all this GADT wrapping is needed, this will not compile :( */
//  def getStringOrInt2[T](b: Bool)(implicit ev: DecideStringOrIntGADT[b.type, T]): T =
//    ev match {
//      case PickS => "Hello"
//      case PickI => 2
//    }

  /** Implements Idris book example using GADT approach combined with non-GADT EncodeInStringOrInt.
    * This uses implicits quite a bit. The disapointing part of this is that I cannot just pattern match on True/False
    * directly */
  def valToString[T](b: Bool)(v: T)(implicit ev: EncodeInStringOrInt[T], de: DecideStringOrIntGADT[b.type, T]): String =
    ev(v) match {
      case S(s) => s
      case I(i) => i.toString
    }
  
}
