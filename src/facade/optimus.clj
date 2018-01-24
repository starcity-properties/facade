(ns facade.optimus
  (:require [facade.html :as fhtml]
            [optimus.link :as link]))


(defn cache-bust-images
  "Helper that examines paths with the supplied prefix and either subs
  in their cache-busting URLs or returns them unchanged."
  [req prefix]
  (fn [^String src]
    (or (and (.startsWith src prefix)
             (not-empty (link/file-path req src)))
        src)))


(defn css-bundles
  "Given a request and names of bundles, generate links to the optimus css bundles."
  [req & bundle-names]
  (fhtml/css-links (link/bundle-paths req bundle-names)))


(defn js-bundles
  "Given a request and names of bundles, generate links to the optimus JS bundles."
  [req & bundle-names]
  (fhtml/js-links (link/bundle-paths req bundle-names)))
