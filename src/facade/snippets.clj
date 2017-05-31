(ns facade.snippets
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]))

(defn- href->k [href]
  (when href (keyword (string/replace href "/" ""))))

(defn- maybe-activate
  [active]
  (letfn [(activate [node]
            (update-in node [:attrs :class] str " active"))]
    (if-let [k active]
      #(cond
         (= k (href->k (get-in % [:attrs :href])))       (activate %)
         (= (name k) (get-in % [:attrs :data-nav-item])) (activate %)
         :otherwise                                      %)
      identity)))

(html/defsnippet loading-fullscreen "templates/partials/loading-fs.html" [:section] [])

(html/defsnippet app-navbar "templates/partials/app/navbar.html" [:nav]
  [& {:keys [logout-href] :or {logout-href "/logout"}}]
  [:#logout] (html/set-attr :href logout-href))

(html/defsnippet public-header "templates/partials/header.html" [:header]
  [& [active]]
  [:.nav-item] (maybe-activate active))
