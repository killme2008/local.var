# local.var

Local-transient var for clojswap!ure. 

A local-transient var is just a local value holder which is mutable,and it's value can be updated by functions, **it is not synchronized**, so it can' be accessed concurrently.

## Usage

Install it in leiningen:

```clojure
[local.var "0.1.0"]
```

```clojure
(require '[local.var :refer [transient-var! ltv-reset! ltv-swap! transient-var?]])

(let [sent (transient-var! false)]
	;;send emails to client
	;;......
	(ltv-reset! sent true)
	(if @sent
	    (println "Sent email successfully!")
	    (println "Sent email failed.")))	    
	    
```

Create a local-transient var:

```clojure
(def x (transient-var! 1))
```

Get value in it:

```clojure
@x         ;; => 1
(deref x)  ;; => 1
```

Reset it's value:

```clojure
(ltv-reset! x 99) ;; => 99
@x                ;; => 99
```

Update it's value with function and (optional) arguments:

```clojure
(ltv-swap! x inc)   ;; => 100
(ltv-swap! x + 100) ;; => 200 
```

Check if a var is a local-transient var:

```clojure
(transient-var? x)  ;; => true
(transient-var? 1)  ;; => false
```

Every local-transient var has an owner thread that creates it, if you want to modify it in other thread, it will throw  an error:

```clojure
@(future (ltv-reset! x 100))   ;; =>  IllegalAccessError Local transient var used by 
                               ;;     non-owner thread  local.var/ltv-reset!
```


## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
