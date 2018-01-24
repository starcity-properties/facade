(ns facade.html
  (:require [cheshire.core :as json]
            [net.cgrand.enlive-html :as html]))


(defn font-links
  "Generate HTML links to fonts."
  [fonts]
  (html/html
   (map (fn [href] [:link {:href href :rel "stylesheet"}]) fonts)))


(defn css-links
  "Generate HTML links to stylesheets."
  [stylesheets]
  (html/html
   (map
    (fn [href]
      [:link {:href  href
              :rel   "stylesheet"
              :type  "text/css"
              :media "screen, projection"}])
    stylesheets)))


(defn js-links
  "Generate HTML links to javascript files."
  [scripts]
  (html/html
   (map
    (fn [src] [:script {:src src :type "text/javascript"}])
    scripts)))


(defn json-objects
  "Create inline JSON objects out of each of `xs` by JSON encoding them within
  script tags."
  [xs]
  (html/html
   (map
    (fn [[name obj]]
      [:script (format "var %s=%s;" name (json/encode obj))])
    xs)))
