(ns
    ^{:doc "The local transient var core."
      :author "Dennis Zhuang"}
    local.var)

(defprotocol LocalVar
  (setValue! [this newval] "Set local-transient var with new value of newval.
   Don't use this method in your code, use 'ltv-reset!' instead."))

(deftype TransientVar [owner ^{:unsynchronized-mutable true} value]
  clojure.lang.IDeref
  (deref [this]
    value)
  LocalVar
  (setValue! [this newval]
    (set! value newval)))

(defn ltv-reset!
  "Sets the value of local-transient var to newval
  without regard for the current value. Returns newval."
  [^TransientVar ltv newval]
  (when-not (= (.owner ltv) (Thread/currentThread))
    (throw (IllegalAccessError.
            "Local transient var used by non-owner thread")))
  (setValue! ltv newval)
  newval)

(defn ltv-swap!
  "'Updates' a value in a local-transient var, where f is
  a function that will take the old value
  and any supplied args and return the new value, Then returns a new
  structure."
  ([ltv f]
   (ltv-reset! ltv (f @ltv)))
  ([ltv f arg1]
   (ltv-reset! ltv (f @ltv arg1)))
  ([ltv f arg1 arg2 & args]
   (ltv-reset! ltv (apply f (deref ltv)
                          arg1 arg2 args))))

(defn transient-var?
  "Returns true if x is a local-transient var."
  [ltv]
  (instance? TransientVar ltv))

(defn transient-var!
  "Create and returns a local transient var with initial value of val."
  [val]
  (TransientVar. (Thread/currentThread)
                 val))
