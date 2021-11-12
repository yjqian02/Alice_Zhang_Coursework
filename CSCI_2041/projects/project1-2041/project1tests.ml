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

