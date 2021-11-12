(* 
  CSCI 2041
  Name: Alice Zhang
  Due: Nov 1, 2021
  Assignment: project1
  
*)

(*project1-2021 tests if a Boolean expression is a tautology, using rules about IF’s*)

type proposition = 
  False
  | True
  | Var of string
  | And of proposition * proposition
  | Or of proposition * proposition
  | Not of proposition
  | Imply of proposition * proposition
  | Equiv of proposition * proposition
  | If of proposition * proposition * proposition 
  ;;

(* (10 points.) 
Here p is a propositional expression. 
Using rules 1 through 5, translate p to an equivalent IF-expression, and return it. 
The resulting IF-expression is not normalized. *)

let rec ifify p = 
  match p with 
    False -> False (* base cases *)
    | True -> True
    | Var name -> p
    | Not a -> If (ifify a, False, True) (* rule 1 *)
    | And (a,b) -> If (ifify a, ifify b, False) (* rule 2 *)
    | Or (a,b) -> If (ifify a, True, ifify b) (* rule 3 *)
    | Imply (a,b) -> If (ifify a, ifify b, True) (* rule 4 *)
    | Equiv (a,b) -> If (ifify a, ifify b, If (ifify b, False, True)) (* rule 5 *)
    | _-> p
;;

(* (10 points.) Here c is an IF-expression as returned from ifify. 
Translate c to an equivalent normalized IF-expression, and return it. *)

let rec normalize c =
(* helper checks if c is a valid if statement, if not, it does a recursive call *)
  let rec isIf test a b =
    match test with 
    (* access the 3 elements of the if statement *) 
      If (l, m, r) -> isIf (normalize l) (If(m, a, b)) (If(r, a, b)) (* recursive calls *)
    | _ -> If (test, (normalize a), (normalize b)) in
  match c with 
    False -> False (* checking base cases, if test is just False/True/Var *)
  | True -> True
  | Var name -> c 
  | If (left, mid, right) -> isIf left mid right 
  | _ -> c (* base recursive cases *)
;;

(* (5 points.) 
This is a helper function for simplify. 
Here c is a normalized IF-expression. 
Also, v is a variable name, and b is a Boolean value, 
represented as instances of the OCaml type proposition. 
Return c{v ⇒ b}. In other words, return a new IF-expression 
that is like c, but in which each appearance of v is replaced by b. *)

let substitute c v b =
  let rec substituting c = (* recursive helper where c is a normalized IF-expression *)
    match c with 
      If(test,a,b) -> 
        If(substituting test, substituting a, substituting b) (* case for recursive calls *)
    | other_test -> (* if test is not an if expression *)
        if c = v (* case if normalized if expression is just a variable *)
        then b
        else other_test (* case if normalized if expression is false or true or anything else *)
  in substituting c ;;   

(* (10 points.) 
Here c is a normalized IF-expression, as returned from normalize. 
Simplify c using rules 7 through 11, and return the resulting IF-expression. *)

let rec simplify c = 
  match c with 
    If(True, a, b) -> simplify a (* rule 7 *)
  | If(False, a, b) -> simplify b (* rule 8 *)
  | If(test, True, False) -> test (* rule 9 *)
  | If(test, a, b) -> 
  (* rule 10 *)
      if simplify (substitute a test True) = simplify (substitute b test False) 
      then simplify (substitute a test True)
      else
        If(test, a, b) (* rule 11 *)
  | _-> c
;;

(* 
Here p is a propositional expression represented 
as an instance of the OCaml type proposition. 
Return OCaml if p is a tautology, and return false otherwise.  
This function must use ifify, normalize, and simplify as its helpers. *)

let tautology p = 
  let p = (simplify (normalize (ifify p))) in (* call all functions on p *)
  if p = True then True else False
;;

(*TESTS*)

  (*IFIFY*)
    (*Rules 1-5 in order, the last call is the example in part 2 of the writeup*)
    print_string "-----------------------------------IFIFY TESTS----------------------------------------\n";;
        
    ifify (Not(Var "a")) ;;

    ifify (And(Var "a", Var "b")) ;;

    ifify (Or(Var "a", Var "b")) ;; 

    ifify (Imply(Var "a", Var "b")) ;;

    ifify (Equiv(Var "a", Var "b")) ;;

    ifify (Imply((Not(And(Var "p", Var "q"))), Or((Not(Var "p")), (Not(Var "q"))))) ;;

    (* expected output *)
        (* -----------------------------------IFIFY TESTS----------------------------------------
      - : unit = ()
      - : proposition = If (Var "a", False, True)
      - : proposition = If (Var "a", Var "b", False)
      - : proposition = If (Var "a", True, Var "b")
      - : proposition = If (Var "a", Var "b", True)
      - : proposition = If (Var "a", Var "b", If (Var "b", False, True))
      - : proposition =
      If (If (If (Var "p", Var "q", False), False, True),
      If (If (Var "p", False, True), True, If (Var "q", False, True)), True) *)

  (*NORMALIZE*)

    print_string "-----------------------------------NORMALIZE TESTS----------------------------------------\n";;

      (*Example from handout for testing normalize*)

      (* normalize (ifify (Imply((Not(And(Var "a", Var "b"))), Or((Not(Var "a")), (Not(Var "b")))))) ;;  *)
      (* expected output *) 
      (* - : proposition =
        If (Var "a",
        If (Var "b",
          If (False,
          If (Var "a", If (False, True, If (Var "b", False, True)),
            If (True, True, If (Var "b", False, True))),
          True),
          If (True,
          If (Var "a", If (False, True, If (Var "b", False, True)),
            If (True, True, If (Var "b", False, True))),
          True)),
        If (False,
          If (False,
          If (Var "a", If (False, True, If (Var "b", False, True)),
            If (True, True, If (Var "b", False, True))),
          True),
          If (True,
          If (Var "a", If (False, True, If (Var "b", False, True)),
            If (True, True, If (Var "b", False, True))),
          True))) *)
      
      (* second test case for normalize *)

      (* normalize (If( (If(Var "a", True, False)), (If( (If(Var "b", True, False)), True, False)), False));; *)
      (* expected output *)
      (* - : proposition =
    If (Var "a",
        If (True, If (Var "b", If (True, True, False), If (False, True, False)),
            False),
        If (False, If (Var "b", If (True, True, False), If (False, True, False)),
            False)) *)

  (*SIMPLIFY *)
    print_string "-----------------------------------SIMPLIFY TESTS----------------------------------------\n";;

    simplify (Not(Var "a")) ;;

    simplify (And(Var "a", Var "b")) ;;

    simplify (Or(Var "a", Var "b")) ;; 

    simplify (Imply(Var "a", Var "b")) ;;

    simplify (Equiv(Var "a", Var "b")) ;;

    simplify (Imply((Not(And(Var "p", Var "q"))), Or((Not(Var "p")), (Not(Var "q"))))) ;;

    simplify (normalize (ifify (Imply((Not(And(Var "a", Var "b"))), Or((Not(Var "a")), (Not(Var "b"))))))) ;; 
    
    (* expected output *)
        (* -----------------------------------SIMPLIFY TESTS----------------------------------------
      - : unit = ()
      - : proposition = Not (Var "a")
      - : proposition = And (Var "a", Var "b")
      - : proposition = Or (Var "a", Var "b")
      - : proposition = Imply (Var "a", Var "b")
      - : proposition = Equiv (Var "a", Var "b")
      - : proposition =
      Imply (Not (And (Var "p", Var "q")), Or (Not (Var "p"), Not (Var "q")))
      - : proposition = True *)

  (*TAUTOLOGY *)
    print_string "-----------------------------------TAUTOLOGY TESTS----------------------------------------\n";; 

    (* tests for tautologies *)

    tautology (Imply((Not(And(Var "a", Var "b"))), Or((Not(Var "a")), (Not(Var "b"))))) ;; (* test from handout *)

    tautology (Imply((Var "p"), (Or((Var "p"), (Var "q"))))) ;; (* basic tautology *)

    tautology (Equiv( (Imply((And(Var "p", Var "q")), Var "r")), (Imply(Var "p", Imply(Var "q", Var "r"))))) ;; (* exportation law *)

    (* tests for non tautologies *)

    tautology (Imply(Not(Var "a"), (Var "a")));;

    tautology (Equiv(Var "a", Var "b")) ;;

    (* expected output *)
        (* -----------------------------------TAUTOLOGY TESTS----------------------------------------
      - : unit = ()
      - : proposition = True
      - : proposition = True
      - : proposition = True
      - : proposition = False
      - : proposition = False *)



  
  
  (* MY NOTES --DON'T GRADE *)
    (* another way to implement simplify with if-else statements *)
        (* let rec simplify c = 
        match c with
          If(test, a, b) -> 
            let a = simplify (substitute a test True) in (* helper function calls for rule 11 *)
            let b = simplify (substitute b test False) in
            if test = True || a = b then a (* rules 7 and 10 *)
            else if test = False then b (* rule 8 *)
            else if a = True && b == False then test (* rule 9 *)
            else If(test, a, b) (* rule 11 *)
        | _-> c 
      ;;  *)
