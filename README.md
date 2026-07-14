# merkle-sum

A **Merkle sum tree** (Maxwell-style) with inclusion proofs — the
shared primitive behind proof-of-liabilities / proof-of-reserves.

Every internal node commits to the **sum** of its children as well as
their hashes, so the root's `:sum` *is* the total and any leaf's
inclusion proof re-derives a slice of it. Hiding the total is
impossible without contradicting some leaf's proof, and the verifier
rejects any negative intermediate sum (the sum-shrinking trick).

- **Zero deps, portable `.cljc`** (JVM / ClojureScript / kotoba-WASM).
- **Injected hasher** — you pass a `hash-hex : String -> hex String`
  (e.g. SHA-256), so the lib carries no crypto (same host-injected
  style as `kotoba.lang.crypto`). The library owns the node preimage
  `"node|<lh>|<ls>|<rh>|<rs>"`; the domain owns the leaf preimage.

```clojure
(require '[merkle-sum.core :as ms])

(def leaves [{:id "alice" :hash (sha "leaf|alice|300") :sum 300}
             {:id "bob"   :hash (sha "leaf|bob|200")   :sum 200}])
(def tree (ms/build-tree sha leaves))          ; {:leaves .. :levels .. :root {:hash :sum}}
(def proof (ms/inclusion-proof tree "alice"))  ; sibling path
(ms/verify sha (sha "leaf|alice|300") 300 proof (:root tree))  ; => true
```

## Provenance

Extracted from `cloud-itonami-isic-6611-cryptoexchange`'s
`cryptoexchange.attest` (superproject ADR-2607141200) so any actor
needing PoR/PoL reuses one audited implementation rather than a copy.

## Test

```sh
clojure -M:test                                     # JVM compat gate
clojure -Sdeps '{:paths ["src" "test"]}' -M:cljs \  # CLJS primary gate
  -m cljs.main --target node -m merkle-sum.cljs-runner
```

AGPL-3.0-or-later.
