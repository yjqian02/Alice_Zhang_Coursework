(* Project 2. 2041
      Alice Zhang
      Dec 8, 2021
*)

(* THING. Types of the usual Lisp objects. *)

type
  thing =
    Closure of thing * thing * environment |
    Cons of thing * thing |
    Nil |
    Number of int |
    Primitive of (thing -> environment -> thing) |
    Symbol of string
and
  environment = (string * thing) list ;;

(* SCANNER. Lexical scanner for Lisp from Lab 9. It also ignores comments. *)

module Scanner =
struct

  (* TOKEN. A token for an expression in a subset of Lisp. *)

  type token =
    CloseParenToken |
    EndToken |
    NumberToken of int |
    OpenParenToken |
    SymbolToken of string ;;

(* MAKE SCANNER. Return a version of the scanner function NEXT TOKEN that reads
   TOKENs from a file whose pathname is the string PATH. INPUT is a channel
   connected to the file. CH holds the most recently read CHAR from INPUT. *)

  let makeScanner path =
    let input = open_in path
    in let ch = ref ' '
       in

(* NEXT CHAR. Advance CH to the next CHAR from INPUT. If there is no next CHAR,
   then set CH to '\000'. We use this CHAR to represent the end of a file. We'd
   like to give this CHAR a name, but then we couldn't MATCH on that name. *)

  let nextChar () =
    try ch := input_char input
    with End_of_file ->
           ch := '\000'
  in

(* NEXT CLOSE PAREN TOKEN. Read a CLOSE PAREN TOKEN. *)

  let nextCloseParenToken () =
    nextChar () ;
    CloseParenToken
  in

(* NEXT COMMENT. Skip a comment. It starts with a ';' and ends with a newline
   '\n' or an end of file '\000'. We skip the '\n', but not the '\000'. *)

  let rec nextComment () =
    match ! ch
    with '\000' ->
           () |
         '\n' ->
           nextChar () |
         _ ->
           nextChar () ;
           nextComment ()
  in

(* NEXT END TOKEN. Read an END TOKEN. We don't skip a CHAR because there are no
   more CHARs to skip. *)

  let nextEndToken () =
    EndToken
  in

(* NEXT NUMBER TOKEN. Read a NUMBER TOKEN that starts with PREFIX. *)

  let nextNumberToken prefix =
    let rec nextNumbering chars =
      match ! ch
      with '\000' | '\n' | ' ' | '(' | ')' ->
             NumberToken (int_of_string chars) |
           _ ->
             let otherChars = Char.escaped ! ch
             in nextChar () ;
                nextNumbering (chars ^ otherChars)
    in nextNumbering prefix
  in

(* NEXT OPEN PAREN TOKEN. Read an OPEN PAREN TOKEN. *)

  let nextOpenParenToken () =
    nextChar () ;
    OpenParenToken
  in

(* NEXT SYMBOL TOKEN. Read a SYMBOL TOKEN that starts with PREFIX. *)

  let nextSymbolToken prefix =
    let rec nextSymboling chars =
      match ! ch
      with '\000' | '\n' | ' ' | '(' | ')' ->
             SymbolToken chars |
           _ ->
             let otherChars = Char.escaped ! ch
             in nextChar () ;
                nextSymboling (chars ^ otherChars)
    in nextSymboling prefix
  in

(* NEXT NUMBER OR SYMBOL TOKEN. We've just read a '-', but we don't know yet if
   it starts a NUMBER TOKEN or a SYMBOL token. Skip the '-'. If we see a digit,
   then it starts a NUMBER TOKEN, otherwise it starts a SYMBOL TOKEN. *)

  let nextNumberOrSymbolToken () =
    nextChar () ;
    match ! ch
    with '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' ->
           nextNumberToken "-" |
         _ ->
           nextSymbolToken "-"
  in

(* NEXT TOKEN. Look at CH to tell what TOKEN is coming next. Dispatch to the
   function that will read that TOKEN and return it. *)

  let rec nextToken () =
    match ! ch
    with '\000' ->
           nextEndToken () |
         ' ' | '\n' ->
           nextChar () ;
           nextToken () |
         '(' ->
           nextOpenParenToken () |
         ')' ->
           nextCloseParenToken () |
         ';' ->
           nextComment () ;
           nextToken () |
         '-' ->
           nextNumberOrSymbolToken () |
         '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' ->
           nextNumberToken "" |
         _ ->
           nextSymbolToken ""

(* Lost? This is MAKE SCANNER's body. Initialize CH by reading the NEXT CHAR,
   and return (but do not call!) NEXT TOKEN. *)

  in nextChar () ;
     nextToken ;;

end ;;

(* (5 points.) Signature. This signature must describe the function makeParser and the exception Can'tParse, but nothing else. *)
module type Parsers =
sig
   exception Can'tParse of string
   val makeParser: string -> unit -> thing
end ;;

(* (10 points.) Module, with the type Parsers (see above). 
It must contain definitions for the exception Can'tParse, along with the functions makeParser, nextThing, and nextThings. 
It may also contain definitions of other objects, but only makeParser and Can'tParse must be visible outside Parser. *)
module Parser: Parsers = 
  struct
   (* (5 points.) Exception, where message is a string. This must be raised by the functions described below if they can’t parse a thing. *)
   exception Can'tParse of string ;;


(* (10 points.) Return a parser: a new function that takes the OCaml unit object () as its only argument.  *)
   let makeParser path =
      let returnNextToken = Scanner.makeScanner path (* nextToken is type unit -> token here *)
      in let token = ref (Scanner.SymbolToken "x") (* placeholder token to start with *)
      (* need to use Scanner.TokenType since the type Token is defined inside of the scanner module *)



   in let rec nextThing () = 
      match !token with 
      Scanner.CloseParenToken -> raise (Can'tParse "unexpected closed parenthesis")
      | Scanner.EndToken -> raise (Can'tParse "unexpected EOF")
      | Scanner.NumberToken n -> token := returnNextToken () ; Number n (* update the value of token to skip over current *) (* calls 179 function, NOT nextToken inside Scanner module *)
      | Scanner.OpenParenToken -> token := returnNextToken () ; nextThings () (* call helper nextThings for the Lisp list *)
      | Scanner.SymbolToken "nil" -> token := returnNextToken () ; Nil
      | Scanner.SymbolToken s -> token := returnNextToken () ; Symbol s



(* (10 points.) This is a helper that nextThing uses to parse a Lisp list. 
When it is called, token is the first token after the OpenParenToken that begins the list.  *)
      and nextThings () = 
         match !token with
         Scanner.CloseParenToken -> token := returnNextToken () ; Nil
         | Scanner.EndToken -> raise (Can'tParse "unexpected EOF")
         | _ -> let current = nextThing ()
            in let remaining = nextThings ()
            in Cons(current, remaining) (* use cons to put the list together *)
      
      in token := returnNextToken (); nextThing ;;
            
   end ;;
         







(* (10 points.) This function does all the work for the parser returned by makeParser. 
It examines token and uses it to decide what kind of thing it will read. 
Then it reads the thing, constructs an OCaml object which represents that thing, and returns the object.  *)




(*
   TESTS. Tests for Project 2.

     James Moen
     24 Nov 21

   Unlike the tests for a lab, these are not worth points. Instead, they help
   test if your parser works correctly.
*)

(* Make a parser NEXT THING that reads from the file "things.txt". *)

let nextThing = Parser.makeParser "things.txt" ;;

(* Each call to NEXT THING reads a Lisp expression, constructs an equivalent
   OCaml object, and returns that object. The comment following each call shows
   what OCaml will print if NEXT THING works correctly. These are the same
   Lisp expressions that were used to test your print function from Lab 10. *)

nextThing () ;;  (* nil *)

(* - : thing = Nil *)

nextThing () ;;  (* 7734 *)

(* - : thing = Number 7734 *)

nextThing () ;;  (* lobyms *)

(* - : thing = Symbol "lobyms" *)

nextThing () ;;  (* (a) *)

(* - : thing = Cons (Symbol "a", Nil) *)

nextThing () ;;  (* (a b) *)

(* - : thing = Cons (Symbol "a", Cons (Symbol "b", Nil)) *)

nextThing () ;;  (* (a b c) *)

(* - : thing = Cons (Symbol "a", Cons (Symbol "b", Cons (Symbol "c", Nil))) *)

nextThing () ;;  (* ((a) b c) *)

(* - : thing =
   Cons (Cons (Symbol "a", Nil), Cons (Symbol "b", Cons (Symbol "c", Nil))) *)

nextThing () ;;  (* ((a b) c) *)

(* - : thing =
   Cons (Cons (Symbol "a", Cons (Symbol "b", Nil)), Cons (Symbol "c", Nil)) *)

nextThing () ;;  (* a (b c) *)

(* - : thing =
   Cons (Symbol "a", Cons (Cons (Symbol "b", Cons (Symbol "c", Nil)), Nil)) *)

nextThing () ;;  (* ((a b c)) *)

(* - : thing =
   Cons (Cons (Symbol "a", Cons (Symbol "b", Cons (Symbol "c", Nil))), Nil) *)

nextThing () ;;  (* (define ! (lambda (n) (if (= n 0) 1 (∗ n (! (− n 1)))))) *)

(* - : thing =
   Cons (Symbol "define",
    Cons (Symbol "!",
     Cons
      (Cons (Symbol "lambda",
        Cons (Cons (Symbol "n", Nil),
         Cons
          (Cons (Symbol "if",
            Cons (Cons (Symbol "=", Cons (Symbol "n", Cons (Number 0, Nil))),
             Cons (Number 1,
              Cons
               (Cons (Symbol "*",
                 Cons (Symbol "n",
                  Cons
                   (Cons (Symbol "!",
                     Cons
                      (Cons (Symbol "-",
                        Cons (Symbol "n", Cons (Number 1, Nil))),
                      Nil)),
                   Nil))),
               Nil)))),
          Nil))),
      Nil))) *)

(* At this point, we've read all the Lisp expressions from "things.txt", so if
   you call NEXT THING again, it should raise the exception CAN'T PARSE. *)

nextThing () ;;

(* Exception: Parser.Can'tParse "Unexpected end of file". *)



(* MY TEST RESULTS *)
(* utop # #use "testsP2.ml";;
type thing =
    Closure of thing * thing * environment
  | Cons of thing * thing
  | Nil
  | Number of int
  | Primitive of (thing -> environment -> thing)
  | Symbol of string
and environment = (string * thing) list
module Scanner :
  sig
    type token =
        CloseParenToken
      | EndToken
      | NumberToken of int
      | OpenParenToken
      | SymbolToken of string
    val makeScanner : string -> unit -> token
  end
module type Parsers =
  sig
    exception Can'tParse of string
    val makeParser : string -> unit -> thing
  end
module Parser : Parsers
val nextThing : unit -> thing = <fun>
- : thing = Nil
- : thing = Number 7734
- : thing = Symbol "lobyms"
- : thing = Cons (Symbol "a", Nil)
- : thing = Cons (Symbol "a", Cons (Symbol "b", Nil))
- : thing = Cons (Symbol "a", Cons (Symbol "b", Cons (Symbol "c", Nil)))
- : thing =
Cons (Cons (Symbol "a", Nil), Cons (Symbol "b", Cons (Symbol "c", Nil)))
- : thing =
Cons (Cons (Symbol "a", Cons (Symbol "b", Nil)), Cons (Symbol "c", Nil))
- : thing =
Cons (Symbol "a", Cons (Cons (Symbol "b", Cons (Symbol "c", Nil)), Nil))
- : thing =
Cons (Cons (Symbol "a", Cons (Symbol "b", Cons (Symbol "c", Nil))), Nil)
- : thing =
Cons (Symbol "define",
 Cons (Symbol "!",
  Cons
   (Cons (Symbol "lambda",
     Cons (Cons (Symbol "n", Nil),
      Cons
       (Cons (Symbol "if",
         Cons (Cons (Symbol "=", Cons (Symbol "n", Cons (Number 0, Nil))),
          Cons (Number 1,
           Cons
            (Cons (Symbol "*",
              Cons (Symbol "n",
               Cons
                (Cons (Symbol "!",
                  Cons
                   (Cons (Symbol "-", Cons (Symbol "n", Cons (Number 1, Nil))),
                   Nil)),
                Nil))),
            Nil)))),
       Nil))),
   Nil)))
Exception: Parser.Can'tParse "unexpected EOF".*)